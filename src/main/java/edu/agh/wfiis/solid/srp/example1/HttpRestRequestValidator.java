package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessageValidator;

public class HttpRestRequestValidator {

    public void validate(HttpRestRequest request, Constraints constraints) throws InvalidHeaderException
    {
        MuleMessageValidator muleMessageValidator = new MuleMessageValidator();
        muleMessageValidator.validateRequiredHeaders(request.muleMessage, constraints);
        muleMessageValidator.validateHeaderPattern(request.muleMessage, constraints);
    }    
}
