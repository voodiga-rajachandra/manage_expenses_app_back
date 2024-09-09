package com.daurenassanbaev.firstpetproject.validation.group;

import com.daurenassanbaev.firstpetproject.database.entity.Account;

public class ExistsException extends RuntimeException{
    public ExistsException(String message) {
        super(message);
    }
}
