package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validateHeaders(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            validateHeader(constraint, headerName, headerValue);
            setMissingHeaderValueToDefault(constraint, headerName, headerValue);
        }
        return muleMessage;
    }

    private void validateHeader(Constraint constraint, String headerName, String headerValue) throws InvalidHeaderException {
        if (headerValue == null) {
            if (constraint.isHeaderRequired()) {
                throw new InvalidHeaderException("Required header " + headerName + " not specified");
            }
        } else {
            if (!constraint.validate(headerValue)) {
                throw new InvalidHeaderException(MessageFormat.format("Invalid value format for header {0}.", headerName));
            }
        }
    }

    private void setMissingHeaderValueToDefault(Constraint constraint, String headerName, String headerValue) {
        if (headerValue == null && constraint.getDefaultValue() != null) {
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
        }
    }

}
