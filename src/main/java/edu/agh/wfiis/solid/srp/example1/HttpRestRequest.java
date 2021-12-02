package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.util.List;

public class HttpRestRequest {

    protected MuleMessage muleMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage, Constraints validationConstraints) {
        this.muleMessage = muleMessage;
        this.validationConstraints = validationConstraints;
        this.validationConstraints.setMissingHeadersWithDefaultValue(this.muleMessage);
    }

    public MuleMessage validate() throws InvalidHeaderException {
        List<String> missingRequiredHeaders = validationConstraints.validateRequiredHeaders(muleMessage);
        if(!missingRequiredHeaders.isEmpty()) {
            throw new InvalidHeaderException("Required headers " + String.join(", ", missingRequiredHeaders) + " are not specified");
        }

        List<String> invalidHeaders = validationConstraints.validateHeadersValue(muleMessage);
        if(!invalidHeaders.isEmpty()) {
            throw new InvalidHeaderException("Invalid value format for headers: " + String.join(", ", invalidHeaders));
        }

        return muleMessage;
    }
}