package edu.agh.wfiis.solid.srp.task1;

class HeaderValidator {

    void validate(String headerValue, Constraint constraint) throws InvalidHeaderException {
        if (headerValue == null && constraint.isHeaderRequired()) {
            throw new InvalidHeaderException("Required header " + constraint.getHeaderName() + " not specified");
        }

        if (headerValue != null && !constraint.validate(headerValue)) {
            throw new InvalidHeaderException(
                    "Invalid value format for header " + constraint.getHeaderName() + "."
            );
        }
    }
}
