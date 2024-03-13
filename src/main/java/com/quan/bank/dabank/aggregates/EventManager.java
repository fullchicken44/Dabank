package com.quan.bank.dabank.aggregates;

import com.google.common.eventbus.EventBus;
import com.quan.bank.dabank.aggregates.events.RootEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventManager {
    private EventBus eventBus;
    private final EventStorage eventStorage;

    public EventManager(EventBus eventBus, EventStorage eventStorage) {
        this.eventBus = eventBus;
        this.eventStorage = eventStorage;
    }


    private void checkAggregateExist(RootEvent event) {
        if (eventStorage.exists(event.getAggregateUUID())) {
            eventStorage.save(event);
        } else {
            throw new AggregateNotExist(event.toString());
        }
    }
}
