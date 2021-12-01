package edu.agh.wfiis.solid.srp.example1.model;

public class Constraint {

    private boolean required;

    private String pattern;

    private String defaultValue;

    private String name;

    public String getPattern() {
        return pattern;
    }

    public String getDefaultValue() {
        return defaultValue;
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
