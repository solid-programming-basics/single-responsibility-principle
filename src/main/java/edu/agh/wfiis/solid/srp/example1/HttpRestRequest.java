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

    public Boolean validateHeaders(Constraints validationConstraints){
        Boolean isHeaderValid = true;
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            try {
                validateHeaderExists(headerName, headerValue, constraint);
            } catch (InvalidHeaderException e){
                isHeaderValid = false;
                // log warning
            }
            try {
                validateHeaderFormat(headerName, headerValue, constraint);
            } catch (InvalidHeaderException e){
                isHeaderValid = false;
                // log warning
            }
        }
        return isHeaderValid;
    }
    private void validateHeaderExists(headerName, headerValue, constraint) throws InvalidHeaderException {
        if (headerValue == null && constraint.isHeaderRequired()) {
            throw new InvalidHeaderException("Required header " + headerName + " not specified");
        }
    }
    private void validateHeaderFormat(headerName, headerValue, constraint) throws InvalidHeaderException {
        if (headerValue != null && !constraint.validate(headerValue)) {
            throw new InvalidHeaderException(MessageFormat.format("Invalid value format for header {0}.", headerName));
        }
    }
    private void setMissingHeaderValues(){
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            if (headerValue == null && constraint.getDefaultValue() != null) {
                muleMessage.setHeader(headerName, constraint.getDefaultValue());
            }
        }
    }
}