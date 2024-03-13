package com.quan.bank.dabank.aggregates;

public class AggregateNotExist extends RuntimeException {
    AggregateNotExist(String message) {super(message);}
}
