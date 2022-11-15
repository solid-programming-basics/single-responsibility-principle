package edu.agh.wfiis.solid.srp.example1.model;

public class RestRequestHeader implements Header {
    private final Constraint constraint;
    private final String value;

    private RestRequestHeader(Builder builder) {
        this.constraint = builder.constraint;
        this.value = builder.value;
    }

    @Override
    public Constraint getConstraint() {
        return constraint;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean hasOnlyDefaultValue() {
        return value == null && constraint.getDefaultValue() != null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Constraint constraint;
        private String value;

        public Builder constraint(Constraint constraint) {
            this.constraint = constraint;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public RestRequestHeader build() {
            return new RestRequestHeader(this);
        }
    }
}
