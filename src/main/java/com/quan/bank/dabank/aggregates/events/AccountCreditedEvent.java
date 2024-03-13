package com.quan.bank.dabank.aggregates.events;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountCreditedEvent extends RootEvent {
    private UUID transactionUUID;
    private UUID fromUUID;
    private UUID toUUID;
    private BigDecimal transferValue;

    public AccountCreditedEvent(UUID aggregateUUID, UUID fromUUID, UUID toUUID, UUID transactionUUID, BigDecimal transferValue) {
        super(aggregateUUID, new Date());
        this.transactionUUID = transactionUUID;
        this.fromUUID = fromUUID;
        this.toUUID = toUUID;
        this.transferValue = transferValue;
    }

}
