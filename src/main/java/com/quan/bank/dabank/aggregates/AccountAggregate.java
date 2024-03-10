package com.quan.bank.dabank.aggregates;

import com.quan.bank.dabank.aggregates.events.AccountCreatedEvent;
import com.quan.bank.dabank.aggregates.events.DomainEvent;
import io.vavr.API;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@ToString
@Getter
@EqualsAndHashCode
public class AccountAggregate {
    private static final double INITIAL_BALANCE = 2000;
    private UUID uuid;
    private String fullName;
    private BigDecimal balance;
    private Map<UUID, BigDecimal> transactionToReverseBalance;
    private final Collection<DomainEvent> domainEvents;
    private Map<UUID, MoneyTransaction> transactions;
    private Date createdAt;
    private Date lastUpdatedAt;

    AccountAggregate(Collection<DomainEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }

    AccountAggregate apply(DomainEvent event) {
        return API.Match(event).of(
        );
    }

    AccountAggregate apply(AccountCreatedEvent event) {

    }

}
