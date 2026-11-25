package edu.agh.wfiis.solid.srp.task1;

class HeaderValueResolver {

    String resolve(MuleMessage muleMessage, Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue != null) {
            return headerValue;
        }

        String defaultValue = constraint.getDefaultValue();
        if (defaultValue != null) {
            muleMessage.setHeader(headerName, defaultValue);
        }

        return defaultValue;
    }
}
