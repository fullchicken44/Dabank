package com.quan.bank.dabank.config;

import com.quan.bank.dabank.aggregates.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DummyData implements CommandLineRunner {
    private final AccountService accountService;

    @Autowired
    public DummyData(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        createDummyAccounts();
    }

    private void createDummyAccounts() {
        final var accountId1 = accountService.createAccountCommand("Nguyen Van A");
        final var accountId2 = accountService.createAccountCommand("John Smith");
        final var accountId3 = accountService.createAccountCommand("Kim Jong Un");
        accountService.moneyTransferCommand(accountId1, accountId2, BigDecimal.TEN);
        accountService.moneyTransferCommand(accountId1, accountId2, BigDecimal.valueOf(100));
        accountService.changeNameCommand(accountId3, "Dum Dum");
    }
}
