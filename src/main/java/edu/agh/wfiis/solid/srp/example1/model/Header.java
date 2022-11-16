package edu.agh.wfiis.solid.srp.example1.model;

public interface Header {
    Constraint getConstraint();
    String getValue();
    default boolean hasOnlyDefaultValue() {
        return false;
    }
}
