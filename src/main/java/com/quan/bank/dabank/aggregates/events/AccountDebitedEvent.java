package com.quan.bank.dabank.aggregates.events;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AccountDebitedEvent extends DomainEvent {

}
