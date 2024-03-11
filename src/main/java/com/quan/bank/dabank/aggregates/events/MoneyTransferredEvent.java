package com.quan.bank.dabank.aggregates.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MoneyTransferredEvent extends DomainEvent {
    private UUID transactionUUID;
    private UUID fromUUID;
    private UUID toUUID;
    private BigDecimal transferValue;

    public MoneyTransferredEvent(UUID aggregateUUID, UUID fromUUID, UUID toUUID, UUID transasctionUUID, BigDecimal transferValue) {
        this (aggregateUUID, fromUUID, toUUID, transasctionUUID, transferValue, new Date());
    }

    public MoneyTransferredEvent(UUID aggregateUUID, UUID fromUUID, UUID toUUID, UUID transactionUUID, BigDecimal transferValue, Date dateCreated) {
        super(aggregateUUID, dateCreated);
        this.transactionUUID = transactionUUID;
        this.fromUUID = fromUUID;
        this.toUUID = toUUID;
        this.transferValue = transferValue;
    }
}
