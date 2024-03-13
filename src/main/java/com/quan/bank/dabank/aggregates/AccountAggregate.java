package com.quan.bank.dabank.aggregates;

import com.quan.bank.dabank.aggregates.events.*;
import io.vavr.API;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static io.vavr.API.Case;
import static io.vavr.API.Match.Pattern0.of;

// Consider AccountAggregate as the Bank account for each person
@ToString
@Getter
@EqualsAndHashCode
public class AccountAggregate {
    private static final double INITIAL_BALANCE = 2000;
    private UUID uuid;
    private String fullName;
    private BigDecimal balance; // Current balance
    // transactionTempReserve is a temporary placeholder for the ongoing transaction, this var here should be empty once all transactions have been completed
    private Map<UUID, BigDecimal> transactionTempReserve;
    private final Collection<RootEvent> rootEvents; // Store all the events
    private Map<UUID, MoneyTransaction> transactions; // Store all the transactions, 1 transaction can have multiple events
    private Date createdAt;
    private Date lastUpdatedAt;

    AccountAggregate(Collection<RootEvent> rootEvents) {
        this.rootEvents = rootEvents;
    }

    AccountAggregate apply(RootEvent event) {
        return API.Match(event).of(
                Case(of(AccountCreatedEvent.class), this::apply),
                Case(of(NameChangedEvent.class), this::apply),
                Case(of(MoneyTransferredEvent.class), this::apply),
                Case(of(AccountDebitedEvent.class), this::apply),
                Case(of(AccountCreditedEvent.class), this::apply),
                Case(of(MoneyTransferredCancelled.class), this::apply),
                Case(of(MoneyTransferredSucceeded.class), this::apply));
    }

    // Account aggregate event for creating account,
    AccountAggregate apply(AccountCreatedEvent event) {
        uuid = event.getAggregateUUID();
        transactionTempReserve = new TreeMap<>();
        balance = BigDecimal.valueOf(INITIAL_BALANCE).setScale(2, RoundingMode.HALF_EVEN);
        fullName = event.getName();
        transactions = new TreeMap<>();
        createdAt = event.getCreatedAt();
        return this;
    }

    // Account aggregate event for changing the full name
    AccountAggregate apply(NameChangedEvent event) {
        uuid = event.getAggregateUUID();
        fullName = event.getFullName();
        return this;
    }

    AccountAggregate apply(MoneyTransferredEvent event) {
        BigDecimal transferValue;
        MoneyTransaction.Type type;
        lastUpdatedAt = event.getCreatedAt();
        // If the current UUID is the same as the fromUUID => outgoing money transfer
        if (event.getAggregateUUID().equals(event.getFromUUID())) {
            transferValue = event.getTransferValue().negate(); // negate money to the balance as reserve for the transaction
            type = MoneyTransaction.Type.OUTGOING;
        } else { // Incoming money transfer
            transferValue = event.getTransferValue();
            type = MoneyTransaction.Type.INCOMING;
        }

        // Account aggregate will hold List map of transactions
        transactions.put(
                event.getTransactionUUID(),
                new MoneyTransaction(
                        event.getTransactionUUID(),
                        event.getFromUUID(),
                        event.getToUUID(),
                        transferValue,
                        MoneyTransaction.State.NEW,
                        type,
                        event.getCreatedAt(),
                        event.getCreatedAt()
                )
        );
        return this;
    }

    // This one occur when an account about to have its balance sent to another account
    // Subtract the transferredValue to the balance
    // It will check if the balance is valid, the create a PENDING event
    AccountAggregate apply(AccountDebitedEvent event) {
        // Check to make sure if the balance not go negative after the transaction
        // Using subtract and compareTo to make sure it is extremely accurate for a financial system
        if (balance.subtract(event.getTransferValue()).compareTo(BigDecimal.ZERO) >= 0) {
            lastUpdatedAt = event.getCreatedAt();
            balance = balance.subtract(event.getTransferValue());
            transactionTempReserve.put(event.getTransactionUUID(), event.getTransferValue().negate());
            if (transactions.containsKey(event.getTransactionUUID())) {
                changeTransactionState(event.getTransactionUUID(), MoneyTransaction.State.PENDING, event.getCreatedAt());
            }
            return this;
        } else {
            throw new BalanceTooLowException();
        }
    }

    private void changeTransactionState(UUID transactionUUID, MoneyTransaction.State state, Date lastUpdatedAt) {
        var transaction = transactions.get(transactionUUID);
        transaction.setState(state);
        transaction.setLastUpdatedAt(lastUpdatedAt);
    }

    // CreditedEvent is an event for the account that's about to have its balance increased.
    AccountAggregate apply(AccountCreditedEvent event) {
        lastUpdatedAt = event.getCreatedAt();
        transactionTempReserve.put(event.getTransactionUUID(), event.getTransferValue());
        if (transactions.containsKey(event.getTransactionUUID())) {
            changeTransactionState(event.getTransactionUUID(), MoneyTransaction.State.PENDING, event.getCreatedAt());
        }
        return this;
    }

    AccountAggregate apply(MoneyTransferredSucceeded event) {
        lastUpdatedAt = event.getCreatedAt();
        changeTransactionState(event.getTransactionUUID(), MoneyTransaction.State.SUCCEEDED, event.getCreatedAt());
        if (transactionTempReserve.containsKey(event.getTransactionUUID())) {
            if (event.getToUUID().equals(event.getAggregateUUID())) { // Add the value to the destination account
                balance = balance.add(transactionTempReserve.remove(event.getTransactionUUID()));
            }
        }
        return this;
    }

    AccountAggregate apply(MoneyTransferredCancelled event) {
        lastUpdatedAt = event.getCreatedAt();
        changeTransactionState(event.getTransactionUUID(), MoneyTransaction.State.CANCELLED, event.getCreatedAt());
        if (event.getToUUID().equals(event.getAggregateUUID())) { // Cancel money send to receiver
            transactionTempReserve.remove(event.getTransactionUUID());
        } else if (transactionTempReserve.containsKey(event.getTransactionUUID())) { // Add money back to the sender balance
            balance = balance.add(transactionTempReserve.get(event.getTransactionUUID()).negate());
            transactionTempReserve.remove(event.getTransactionUUID());
        }
        return this;
    }

}
