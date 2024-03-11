package com.quan.bank.dabank.aggregates;

import com.quan.bank.dabank.aggregates.events.AccountCreatedEvent;
import com.quan.bank.dabank.aggregates.events.DomainEvent;
import com.quan.bank.dabank.aggregates.events.MoneyTransferredEvent;
import com.quan.bank.dabank.aggregates.events.NameChangedEvent;
import io.vavr.API;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@ToString
@Getter
@EqualsAndHashCode
public class AccountAggregate {
    private static final double INITIAL_BALANCE = 2000;
    private UUID uuid;
    private String fullName;
    private BigDecimal balance;
    private Map<UUID, BigDecimal> transactionToReverseBalance;
    private final Collection<DomainEvent> domainEvents;
    private Map<UUID, MoneyTransaction> transactions;
    private Date createdAt;
    private Date lastUpdatedAt;

    AccountAggregate(Collection<DomainEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }

    AccountAggregate apply(DomainEvent event) {
        return API.Match(event).of(
        );
    }

    // Account aggregate event for creating account,
    AccountAggregate apply(AccountCreatedEvent event) {
        uuid = event.getAggregateUUID();
        transactionToReverseBalance = new TreeMap<>();
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

    AccountAggregate applyA(Account)



}
