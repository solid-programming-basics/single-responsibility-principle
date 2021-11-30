package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validateHeaders(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            checkHeaderMeetsConstraints(constraint);
            setDefaultValueForMissingHeader(constraint);
        }

        return muleMessage;
    }

    private void checkHeaderMeetsConstraints(Constraint constraint) throws InvalidHeaderException {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null && constraint.isHeaderRequired()) {
            throw new MissingRequiredHeaderException(headerName);
        }

        if (headerValue != null) {
            if (!constraint.validate(headerValue)) {
                throw new InvalidHeaderValueFormatException(headerValue, headerName);
            }
        }
    }

    private void setDefaultValueForMissingHeader(Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null && constraint.getDefaultValue() != null) {
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
        }
    }
}

class MissingRequiredHeaderException extends InvalidHeaderException {
    private final String headerName;

    public MissingRequiredHeaderException(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Required header {0} not specified", headerName);
    }
}

class InvalidHeaderValueFormatException extends InvalidHeaderException {
    private final String headerValue;
    private final String headerName;

    public InvalidHeaderValueFormatException(String headerValue, String headerName) {
        this.headerValue = headerValue;
        this.headerName = headerName;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Invalid value {0} format for header {1}.", headerValue, headerName);
    }
}