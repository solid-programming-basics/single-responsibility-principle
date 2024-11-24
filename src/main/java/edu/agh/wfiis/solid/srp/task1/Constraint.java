package edu.agh.wfiis.solid.srp.task1;

class Constraint {

    private final boolean required;

    private final String pattern;

    private final String defaultValue;

    private final String name;

    Constraint(boolean required, String pattern, String defaultValue, String name) {
        this.required = required;
        this.pattern = pattern;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    public boolean validate(String incomingValue) {
        return pattern == null ? true : pattern.matches(incomingValue);
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
}
