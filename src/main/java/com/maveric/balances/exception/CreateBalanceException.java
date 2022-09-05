package com.maveric.balances.exception;

import org.springframework.stereotype.Component;

@Component
public class CreateBalanceException  extends  RuntimeException{
    public CreateBalanceException() {
    }

    public CreateBalanceException(String message) {
        super(message);
    }

    public CreateBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
