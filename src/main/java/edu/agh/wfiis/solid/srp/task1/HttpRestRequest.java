package edu.agh.wfiis.solid.srp.task1;

import java.text.MessageFormat;

public class HttpRestRequest {

    public HttpRestRequest() {
    }

    public MuleMessage process(MuleMessage muleMessage, Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.getDefaultValue() != null) {
                headerValue = constraint.getDefaultValue();
                muleMessage.setHeader(headerName, headerValue);
            }

            ContraintValidator.validateForHttpReq(headerValue, constraint);
        }
        return muleMessage;
    }
}
