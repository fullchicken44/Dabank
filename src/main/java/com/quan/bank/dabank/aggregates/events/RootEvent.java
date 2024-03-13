package com.quan.bank.dabank.aggregates.events;

import com.google.common.base.CaseFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

// Base event for all other events
@ToString
@Getter
@EqualsAndHashCode(exclude = {"createdAt"})
@AllArgsConstructor
public class RootEvent {
    Date createdAt;
    private String eventType;
    private UUID aggregateUUID;

    RootEvent() {
        this.eventType = classNameToUpperCase();
    }

    RootEvent(UUID aggregateUUID, Date createdAt) {
        this.aggregateUUID = aggregateUUID;
        this.createdAt = createdAt;
        this.eventType = classNameToUpperCase();
    }

    private String classNameToUpperCase() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass().getSimpleName());
    }
}
