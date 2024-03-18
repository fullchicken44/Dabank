package com.quan.bank.dabank.controllers.dto;

import com.quan.bank.dabank.aggregates.AccountAggregate;
import com.quan.bank.dabank.aggregates.events.RootEvent;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.quan.bank.dabank.controllers.dto.URLPath.getPathForAccount;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class AccountResponse {
    private String fullName;
    private UUID accountNumber;
    private BigDecimal balance;
    private Collection<RootEvent> events;
    private Map<UUID, BigDecimal> transactionTempReserve;
    private Map<UUID, MoneyTransactionDTO> transactions;
    private Date createdAt;
    private Date lastUpdatedAt;
    private List<URLPath> URLPath;

    public static AccountResponse from(AccountAggregate aggregate) {
        return AccountResponse.builder()
                .accountNumber(aggregate.getUuid())
                .fullName(aggregate.getFullName())
                .balance(aggregate.getBalance())
                .transactionTempReserve(aggregate.getTransactionTempReserve())
                .events(aggregate.getRootEvents())
                .transactions(
                        aggregate.getTransactions().values().stream()
                                .map(
                                        moneyTransaction -> MoneyTransactionDTO.builder()
                                                .transactionUUID(moneyTransaction.getTransactionUUID())
                                                .fromAccountUUID(moneyTransaction.getFromUUID())
                                                .toAccountUUID(moneyTransaction.getToUUID())
                                                .value(moneyTransaction.getValue())
                                                .state(moneyTransaction.getState())
                                                .type(moneyTransaction.getType())
                                                .lastUpdatedAt(moneyTransaction.getLastUpdatedAt())
                                                .createdAt(moneyTransaction.getCreatedAt())
                                                .build())
                                .collect(
                                        Collectors.toMap(
                                                MoneyTransactionDTO::getTransactionUUID,
                                                moneyTransactionDTO -> moneyTransactionDTO)))
                .createdAt(aggregate.getCreatedAt())
                .lastUpdatedAt(aggregate.getLastUpdatedAt())
                .URLPath(getPathForAccount(aggregate.getUuid()))
                .build();
    }
}
