package com.bank.cards.domain.exception;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String roleName) { 
        super("Role already exists: " + roleName); 
    }
}