package edu.agh.wfiis.solid.srp.task1;

class Constraint {

    private final boolean required;

    private final String pattern;

    private final String defaultValue;

    private final String headerName;

    Constraint(boolean required, String pattern, String defaultValue, String headerName) {
        this.required = required;
        this.pattern = pattern;
        this.defaultValue = defaultValue;
        this.headerName = headerName;
    }

    public boolean validate(String incomingValue) {
        return pattern == null || pattern.matches(incomingValue);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getHeaderName() {
        return headerName;
    }

    public boolean isHeaderRequired() {
        return required;
    }
}
