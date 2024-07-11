package com.trainee.planner.domain.trip.exception;

public class TripNotFoundException extends RuntimeException{
    public TripNotFoundException(String message) {
        super(message);
    }
}
