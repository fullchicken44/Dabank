package com.quan.bank.dabank.controllers.dto;

import com.quan.bank.dabank.aggregates.MoneyTransaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class MoneyTransactionDTO {
    private UUID transactionUUID;
    private UUID fromAccountUUID;
    private UUID toAccountUUID;
    private BigDecimal value;
    private MoneyTransaction.State state;
    private MoneyTransaction.Type type;
    private Date lastUpdatedAt;
    private Date createdAt;
}
