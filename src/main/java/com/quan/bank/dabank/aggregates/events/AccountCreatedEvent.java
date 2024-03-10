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
public class AccountCreatedEvent extends DomainEvent {
    private String name;

    public AccountCreatedEvent(UUID aggregateUUID, String name) {
        super(aggregateUUID, new Date());
        this.name = name;
    }

}
