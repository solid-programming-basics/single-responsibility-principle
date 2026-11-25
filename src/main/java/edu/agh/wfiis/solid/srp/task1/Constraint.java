package edu.agh.wfiis.solid.srp.task1;

public class Constraint {

    private final boolean required;
    private final String pattern;
    private final String defaultValue;
    private final String name;

    public Constraint(boolean required, String pattern, String defaultValue, String name) {
        this.required = required;
        this.pattern = pattern;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean validate(String incomingValue) {
        return pattern == null || incomingValue.matches(pattern);
    }

    public String getHeaderName() {
        return name;
    }

    public boolean isHeaderRequired() {
        return required;
    }

    public String getPattern() {
        return pattern;
    }
}
