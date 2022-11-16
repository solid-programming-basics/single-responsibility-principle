package edu.agh.wfiis.solid.srp.example1.validator;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Header;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;

import java.text.MessageFormat;

public class HeaderValidator {

    public static void validate(Header header) throws InvalidHeaderException {
        Constraint constraint = header.getConstraint();
        if (doesNotHaveRequiredValue(header)) {
            throw new InvalidHeaderException("Required header " + constraint.getHeaderName() + " not specified");
        }
        if (hasInvalidValue(header)) {
            throw new InvalidHeaderException(MessageFormat.format("Invalid value format for header {0}.", constraint.getHeaderName()));
        }
    }

    private static boolean doesNotHaveRequiredValue(Header header) {
        Constraint constraint = header.getConstraint();
        return header.getValue() == null && constraint.isHeaderRequired();
    }

    private static boolean hasInvalidValue(Header header) {
        Constraint constraint = header.getConstraint();
        String value = header.getValue();
        return value != null && !constraint.validate(value);
    }
}
