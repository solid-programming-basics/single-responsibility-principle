package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HttpRestRequest {

    protected MuleMessage muleMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public void setValidationConstraints(Constraints validationConstraints) {
        this.validationConstraints = validationConstraints;
    }

    public void validate() throws InvalidHeaderException {
        validateHeaders();
    }

    public void setMissingHeadersToDefault() {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.getDefaultValue() != null) {
                muleMessage.setHeader(headerName, constraint.getDefaultValue());
            }
        }

    }

    private boolean validateHeaderValues(String incomingValue, Constraint constraint) {
        return constraint.getPattern() == null ? true : constraint.getPattern().matches(incomingValue);
    }

    private void validateHeaders() throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                throw new InvalidHeaderException("Required header " + headerName + " not specified");
            }

            if (headerValue != null && !validateHeaderValues(headerValue, constraint)) {
                throw new InvalidHeaderException(MessageFormat.format("Invalid value format for header {0}.", headerName));
            }
        }
    }
}