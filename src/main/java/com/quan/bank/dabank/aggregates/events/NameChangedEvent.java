package com.quan.bank.dabank.aggregates.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NameChangedEvent extends DomainEvent {
    private String fullName;

    public NameChangedEvent(UUID aggregateUUID, String fullName) {
        super(aggregateUUID, new Date());
        this.fullName = fullName;
    }

}
