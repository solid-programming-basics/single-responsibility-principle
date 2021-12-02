package edu.agh.wfiis.solid.srp.example1.model;

import java.util.ArrayList;
import java.util.List;

public class Validator {
    Constraints validationConstraints;

    public Validator(Constraints validationConstraints) {
        this.validationConstraints = validationConstraints;
    }

    public List<ConstraintViolation> validate(MuleMessage muleMessage) {
        List<ConstraintViolation> violations = new ArrayList<>();

        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                violations.add(new ConstraintViolation("Required header " + headerName + " not specified"));
            }

            if (headerValue != null && !constraint.validate(headerValue)) {
                violations.add(new ConstraintViolation("Invalid value format for header " + headerName));
            }
        }

        return violations;
    }
}
