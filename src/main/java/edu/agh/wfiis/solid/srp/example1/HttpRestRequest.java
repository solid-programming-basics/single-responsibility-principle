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

    public void validateHeaders(Constraints validationConstraints) throws InvalidHeaderException{
        List<String> errors = new ArrayList<>();
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            if (headerValue == null && constraint.isHeaderRequired()) {
                errors.add("Required header " + headerName + " not specified");
            }
            if (headerValue != null && !constraint.validate(headerValue)) {
                errors.add(MessageFormat.format("Invalid value format for header {0}.", headerName));
            }
        }
        if (!errors.empty()){
            throw new InvalidHeaderException(String.join("\n", errors));
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