package edu.agh.wfiis.solid.srp.example1.model;

public class ConstraintViolation {
    private String message;

    @Override
    public String toString() {
        return "ConstraintViolation{" +
                "message='" + message + '\'' +
                '}';
    }

    public ConstraintViolation(String message) {
        this.message = message;
    }
}
