package edu.agh.wfiis.solid.srp.example1.model;

import java.util.ArrayList;
import java.util.List;

public class Constraints {

    private List<Constraint> headersConstraints = new ArrayList<>();

    public void add(List<Constraint> constraints) {
        headersConstraints.addAll(constraints);
    }

    public List<String> validateRequiredHeaders(MuleMessage message) {
        List<String> missingRequiredHeaders =  new ArrayList<>();

        for (Constraint constraint : headersConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = message.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                missingRequiredHeaders.add(headerName);
            }
        }

        return missingRequiredHeaders;
    }

    public List<String> validateHeadersValue(MuleMessage message) {
        List<String> invalidHeaders =  new ArrayList<>();

        for (Constraint constraint : headersConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = message.getHeader(headerName);

            if (headerValue != null && !constraint.validate(headerValue)) {
                invalidHeaders.add(headerName);
            }
        }

        return invalidHeaders;
    }

    public void setMissingHeadersWithDefaultValue(MuleMessage message) {
        for (Constraint constraint : headersConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = message.getHeader(headerName);

            if (headerValue == null && constraint.getDefaultValue() != null) {
                message.setHeader(headerName, constraint.getDefaultValue());
            }
        }
    }
}