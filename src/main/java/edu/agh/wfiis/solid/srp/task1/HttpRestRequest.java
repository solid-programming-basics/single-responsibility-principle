package edu.agh.wfiis.solid.srp.task1;

import java.text.MessageFormat;
import java.util.List;

public class HttpRestRequest {

    public HttpRestRequest() {
    }

    public MuleMessage process(MuleMessage muleMessage, List<Constraint> validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.getDefaultValue() != null) {
                headerValue = constraint.getDefaultValue();
                muleMessage.setHeader(headerName, headerValue);
            }

            if (headerValue == null && constraint.isHeaderRequired()) {
                throw new InvalidHeaderException("Required header " + headerName + " not specified");
            }

            if (headerValue != null && !constraint.validate(headerValue)) {
                throw new InvalidHeaderException(
                        MessageFormat.format("Invalid value format for header {0}.", headerName)
                );
            }
        }
        return muleMessage;
    }
}
