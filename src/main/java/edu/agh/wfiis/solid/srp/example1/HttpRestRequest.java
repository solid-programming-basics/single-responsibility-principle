package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;
import edu.agh.wfiis.solid.srp.example1.validate.HeaderValidator;

import java.text.MessageFormat;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage getMuleMessage(){
        return this.muleMessage;
    }

    public void validate(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = this.muleMessage.getHeader(headerName);

            HeaderValidator.validateHeaderRequired(headerValue, constraint, headerName);
            HeaderValidator.validateHeaderDefaultValue(headerValue, headerName, constraint, this.muleMessage);
            HeaderValidator.validateHeaderFormat(headerValue, headerName, constraint);
        }
    }

}