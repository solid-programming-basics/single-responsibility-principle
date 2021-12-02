package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.*;

import java.text.MessageFormat;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
        setDefaultHeaders();
    }

    private void setDefaultHeaders() {
        for (DefaultValue defaultValue : new DefaultValues().getDefaultValueList()) {
            String headerName = defaultValue.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null) muleMessage.setHeader(headerName, defaultValue.getValue());
        }
    }

    public HeadersValidationResult validateHeaders(Constraints validationConstraints) {
        HeadersValidationResult headersValidationResult = new HeadersValidationResult();

        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                headersValidationResult.addHeaderException(
                        new InvalidHeaderException("Required header " + headerName + " not specified")
                );
            }

            if (headerValue != null && !constraint.validate(headerValue)) {
                headersValidationResult.addHeaderException(
                        new InvalidHeaderException(
                                MessageFormat.format("Invalid value format for header {0}.", headerName)
                        )
                );
            }
        }

        return headersValidationResult;
    }
}
