package edu.agh.wfiis.solid.srp.task1;

public class DefaultHeaderPopulator {

    public String resolveHeaderValue(MuleMessage message, Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = message.getHeader(headerName);

        if (headerValue == null && constraint.getDefaultValue() != null) {
            headerValue = constraint.getDefaultValue();
            message.setHeader(headerName, headerValue);
        }

        return headerValue;
    }
}
