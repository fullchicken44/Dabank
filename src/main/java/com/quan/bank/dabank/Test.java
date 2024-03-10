package com.quan.bank.dabank;

public class Test {
    private boolean optionA;
    private int optionB;
    private String optionC;

    public Test setOptionA(boolean value) {
        this.optionA = value;
        return this; // Enable method chaining
    }

    public Test setOptionB(int value) {
        this.optionB = value;
        return this; // Enable method chaining
    }

    public Test setOptionC(String value) {
        this.optionC = value;
        return this; // Enable method chaining
    }

    public Test apply() {
        // Here you might apply some complex configuration logic based on the options set.
        // For example, if optionA is true, modify optionB and optionC in some manner.

        if (optionA) {
            optionB *= 2; // Just an example operation
            optionC = optionC.toUpperCase(); // Another example operation
        }

        return this; // Return the current instance to allow further configuration or to finalize configuration.
    }

    @Override
    public String toString() {
        return "Configurator{" +
                "optionA=" + optionA +
                ", optionB=" + optionB +
                ", optionC='" + optionC + '\'' +
                '}';
    }
}
