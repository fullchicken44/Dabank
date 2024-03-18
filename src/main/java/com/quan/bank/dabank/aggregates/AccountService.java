package com.quan.bank.dabank.aggregates;

import com.google.common.eventbus.EventBus;
import com.quan.bank.dabank.aggregates.events.AccountCreatedEvent;
import com.quan.bank.dabank.aggregates.events.MoneyTransferredCancelled;
import com.quan.bank.dabank.aggregates.events.MoneyTransferredEvent;
import com.quan.bank.dabank.aggregates.events.NameChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {
    private final EventBus eventBus;

    @Autowired
    public AccountService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public UUID createAccountCommand(String name) {
        UUID uuid = UUID.randomUUID();
        eventBus.post(new AccountCreatedEvent(uuid, name));
        return uuid;
    }

    public void changeNameCommand(UUID uuid, String name) {
        eventBus.post(new NameChangedEvent(uuid, name));
    }

    public void moneyTransferCommand(UUID fromUUID, UUID toUUID, BigDecimal transferValue) {
        eventBus.post(new MoneyTransferredEvent(fromUUID, fromUUID, toUUID, UUID.randomUUID(), transferValue));
    }

    public void moneyCancelCommand(
            UUID aggregateUUID,
            UUID fromUUID,
            UUID toUUID,
            UUID transactionUUID,
            BigDecimal transferValue,
            MoneyTransferredCancelled.Reason reason) {
        eventBus.post(new MoneyTransferredCancelled(transactionUUID, fromUUID, toUUID, aggregateUUID, transferValue, reason));
    }
}
