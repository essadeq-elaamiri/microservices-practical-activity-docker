package me.elaamiri.customerservice.exceptions;

public class CustomerNotFountException extends RuntimeException{
    public CustomerNotFountException(String message) {
        super(message);
    }
}
