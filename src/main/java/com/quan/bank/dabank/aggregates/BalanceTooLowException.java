package com.quan.bank.dabank.aggregates;

public class BalanceTooLowException extends RuntimeException{
    public BalanceTooLowException() {
        super("Balance too low");
    }

    public BalanceTooLowException(String e) {
        super(e);
    }
}
