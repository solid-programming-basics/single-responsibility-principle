package edu.agh.wfiis.solid.srp.example1.model;

import java.util.Arrays;
import java.util.List;

public class ConstraintViolationException extends Throwable {
    public ConstraintViolationException(List<ConstraintViolation> violations) {
        super(Arrays.toString(violations.toArray()));
    }
}
