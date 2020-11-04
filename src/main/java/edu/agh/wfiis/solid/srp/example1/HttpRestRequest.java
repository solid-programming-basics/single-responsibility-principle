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

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {

        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
    
            validateHeader(constraint, headerValue, headerName);
            setMissingHeaderValueToDefault(constraint, headerValue, headerName);
        }

        return muleMessage;
    }

    private void setMissingHeaderValueToDefault(Constraint constraint, String headerValue, String headerName) {
        if (headerValue == null && constraint.getDefaultValue() != null) 
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
    }

    private void validateHeader(Constraint constraint, String headerValue, String headerName) throws InvalidHeaderException {
        String missingErrorMsg = String.format("Required header is not specified: %s", headerName);
        String invalidValueMsg = String.format("Invalid value format for header: %s", headerName);

        if (headerValue == null) {
            if (constraint.isHeaderRequired() && constraint.isHeaderRequired())
                throw new InvalidHeaderException(missingErrorMsg);
        } else {
            if (!constraint.validate(headerValue))
                throw new InvalidHeaderException(invalidValueMsg);
        }
    }

    // optional API
    public void validateAllHeaders(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            
            validateHeader(constraint, headerValue, headerName);
        }
    }

    public void fixMissingHeaders(Constraints validationConstraints) {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            setMissingHeaderValueToDefault(constraint, headerValue, headerName);
        }
    }
}