package edu.agh.wfiis.solid.srp.model.raml;

public class Header {

    private boolean required;

    private String pattern;

    private String defaultValue;

    private String displayName;

    public boolean isMatchToPattern(String incomingValue) {
        return pattern == null || pattern.matches(incomingValue);
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
