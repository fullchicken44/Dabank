package com.quan.bank.dabank.aggregates.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MoneyTransferredCancelled extends RootEvent {
    private UUID transactionUUID;
    private UUID fromUUID;
    private UUID toUUID;
    private BigDecimal transferValue;
    private Reason reason;

    public MoneyTransferredCancelled(UUID transactionUUID, UUID fromUUID, UUID toUUID, UUID aggregateUUID, BigDecimal transferValue, Reason reason) {
        this(transactionUUID, fromUUID, toUUID, aggregateUUID, transferValue, reason, new Date());
    }

    public MoneyTransferredCancelled(UUID transactionUUID, UUID fromUUID, UUID toUUID, UUID aggregateUUID, BigDecimal transferValue, Reason reason, Date date) {
        super(aggregateUUID, date);
        this.transactionUUID = transactionUUID;
        this.fromUUID = fromUUID;
        this.toUUID = toUUID;
        this.transferValue = transferValue;
        this.reason = reason;
    }

    public enum Reason {
        LOW_BALANCE,
        INTERNAL_SERVER_ERROR
    }
}
