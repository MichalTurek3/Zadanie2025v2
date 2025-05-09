package org.app.exception;

public class LoyaltyPointsNotAcceptableException extends RuntimeException{
    public LoyaltyPointsNotAcceptableException() {
    }

    public LoyaltyPointsNotAcceptableException(String message) {
        super(message);
    }
}
