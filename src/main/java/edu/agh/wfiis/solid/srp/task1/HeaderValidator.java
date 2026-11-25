package edu.agh.wfiis.solid.srp.task1;

import java.text.MessageFormat;

public class HeaderValidator {

    public void validate(String headerValue, Constraint constraint) throws InvalidHeaderException {
        String headerName = constraint.getHeaderName();

        if (headerValue == null && constraint.isHeaderRequired()) {
            throw new InvalidHeaderException("Required header " + headerName + " not specified");
        }

        if (headerValue != null && !constraint.validate(headerValue)) {
            throw new InvalidHeaderException(
                    MessageFormat.format("Invalid value format for header {0}.", headerName)
            );
        }
    }
}
