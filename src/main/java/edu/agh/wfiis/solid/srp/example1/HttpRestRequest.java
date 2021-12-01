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

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;
        
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

        isHeaderReq();
        getValue();
        validateHeader()
        }
        return muleMessage;
    }

   
private void isHeaderReq() throws InvalidHeaderException {
            if (headerValue == null && constraint.isHeaderRequired()) {
                throw new InvalidHeaderException("Required header " + headerName + " not specified");
            }
}

private void getValue() throws InvalidHeaderException {
            if (headerValue == null && constraint.getDefaultValue() != null) {
                muleMessage.setHeader(headerName, constraint.getDefaultValue());
            }
}

private void validateHeader() throws InvalidHeaderException {
            if (headerValue != null) {
                if (!constraint.validate(headerValue)) {
                    throw new InvalidHeaderException(MessageFormat.format("Invalid value format for header {0}.", headerName));
                }
            }
        }
}
