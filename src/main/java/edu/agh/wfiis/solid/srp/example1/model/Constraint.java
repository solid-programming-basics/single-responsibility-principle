package edu.agh.wfiis.solid.srp.example1.model;

public class Constraint {

    private boolean required;

    private String pattern;

    private String name;

    public boolean validate(String incomingValue) {
        return pattern == null ? true : pattern.matches(incomingValue);
    }

    public String getHeaderName() {
        return name;
    }

    public boolean isHeaderRequired() {
        return required;
    }

    public void setHeaderRequired(boolean required) {
        this.required = required;
    }

    public void setHeaderName(String name) {
        this.name = name;
    }
}
