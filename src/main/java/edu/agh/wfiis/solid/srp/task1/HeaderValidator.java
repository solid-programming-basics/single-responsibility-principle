package edu.agh.wfiis.solid.srp.task1;

import java.util.LinkedList;
import java.util.List;

public class HeaderValidator {
    public ValidationStatus validate(MuleMessage muleMessage, List<Constraint> constraints) {
        ValidationStatus status = new ValidationStatus();
        for (Constraint constraint : constraints) {

            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                status.addError("Required header " + constraint.getHeaderName() + " not specified");
            }

            if (headerValue != null && !constraint.validate(headerValue)) {
                status.addError("Invalid value format for header "+ constraint.getHeaderName() +".");
            }
        }
        return status;
    }
}

class ValidationStatus {
    private final List<String> errors = new LinkedList<>();

    public ValidationStatus() {
    }

    public void addError(String error){
        errors.add(error);
    }

    public boolean isValid(){
        return errors.isEmpty();
    }

    public List<String> getErrorMessages() {
        return errors;
    }
}
