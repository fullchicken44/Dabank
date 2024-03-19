package com.quan.bank.dabank.aggregates;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.quan.bank.dabank.aggregates.events.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventManager {
    private final EventBus eventBus;
    private final EventStorage eventStorage;

    @Autowired
    public EventManager(EventBus eventBus, EventStorage eventStorage) {
        this.eventBus = eventBus;
        this.eventStorage = eventStorage;
    }

    // Handle create account
    @Subscribe
    void handleEvent(AccountCreatedEvent event) {
        log.info(String.valueOf(event));
        eventStorage.save(event);
    }

    // Handle change name event
    @Subscribe
    void handleEvent(NameChangedEvent event) {
        log.info(String.valueOf(event));
        checkAggregateExist(event); // Have to check if account aggregate exist to change name
    }

    @Subscribe
    void handleEvent(MoneyTransferredEvent event) {
        log.info(String.valueOf(event));
        checkAggregateExist(event);
        eventBus.post(
                new AccountDebitedEvent(
                        event.getAggregateUUID(),
                        event.getFromUUID(),
                        event.getToUUID(),
                        event.getTransactionUUID(),
                        event.getTransferValue()
                )
        );
    }

    @Subscribe
    void handleEvent(AccountDebitedEvent event) {
        log.info(String.valueOf(event));
        AccountAggregate aggregate = eventStorage.get(event.getFromUUID());
        if (aggregate == null) {
            throw new AggregateNotExist(event.toString());
        }

        try {
            aggregate.apply(event);
        } catch (BalanceTooLowException e) {
            // If not enough money, this event will only be emitted to the sender
            eventBus.post(new MoneyTransferredCancelled(
                    event.getTransactionUUID(),
                    event.getFromUUID(),
                    event.getToUUID(),
                    event.getAggregateUUID(),
                    event.getTransferValue(),
                    MoneyTransferredCancelled.Reason.LOW_BALANCE
            ));
        }
        // If enough money, persist the event and continue further
        checkAggregateExist(event);

        // If enough money, emit AccountCreditedEvent to the receiver
        checkAggregateExist(new AccountCreditedEvent(
                event.getToUUID(),
                event.getFromUUID(),
                event.getToUUID(),
                event.getTransactionUUID(),
                event.getTransferValue()
        ));

    }

    @Subscribe
    void handleEvent(AccountCreditedEvent event) {
        log.info(String.valueOf(event));
        checkAggregateExist(event);

        // Issue the transfer as SUCCESS in the sender's aggregate
        eventBus.post(new MoneyTransferredSucceeded(
                event.getFromUUID(),
                event.getFromUUID(),
                event.getToUUID(),
                event.getTransactionUUID(),
                event.getTransferValue()
        ));

        // Emit the transfer as SUCCESS in the receiver's aggregate
        eventBus.post(new MoneyTransferredSucceeded(
                event.getToUUID(),
                event.getFromUUID(),
                event.getToUUID(),
                event.getTransactionUUID(),
                event.getTransferValue()
        ));
    }

    @Subscribe
    void handleEvent(MoneyTransferredCancelled event) {
        log.info(String.valueOf(event));
        checkAggregateExist(event);
    }

    @Subscribe
    void handleEvent(MoneyTransferredSucceeded event) {
        log.info(String.valueOf(event));
        checkAggregateExist(event);
    }
    // Persist if aggregate exist, then save the event to events list
    private void checkAggregateExist(RootEvent event) {
        if (eventStorage.exists(event.getAggregateUUID())) {
            eventStorage.save(event);
        } else {
            throw new AggregateNotExist(event.toString());
        }
    }
}
