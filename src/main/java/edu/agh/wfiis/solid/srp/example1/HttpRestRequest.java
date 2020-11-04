package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class HttpRestRequest {
    protected MuleMessage muleMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;
        List<Constraint> headerConstraints = validationConstraints.getHeaderConstraints();

        List<HeaderValidationError> headerValidationErrors = validateHeaders(headerConstraints);
        if (!headerValidationErrors.isEmpty()) {
            throw new InvalidHeaderException(headerValidationErrors.get(0).getError());
        }

        setMissingHeadersDefaultValues(headerConstraints);
        
        return muleMessage;
    }

    private List<HeaderValidationError> validateHeaders(List<Constraint> headerConstraints) {
        List<HeaderValidationError> errorMessages = new ArrayList<>();

        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                errorMessages.add(new MissingHeaderValidationError(headerName));
            }

            if (headerValue != null && !constraint.validate(headerValue)) {
                errorMessages.add(new InvalidHeaderValueValidationError(headerName, headerValue));
            }
        }

        return errorMessages;
    }

    private void setMissingHeadersDefaultValues(List<Constraint> headerConstraints) {
        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerDefaultValue = constraint.getDefaultValue();
            if (muleMessage.getHeader(headerName) == null && headerDefaultValue != null) {
                muleMessage.setHeader(headerName, headerDefaultValue);
            }
        }
    }
}

interface HeaderValidationError {
    String getError();
}

class MissingHeaderValidationError  implements HeaderValidationError {
    private final String headerName;

    public MissingHeaderValidationError(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String getError() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidHeaderValueValidationError  implements HeaderValidationError {
    private final String headerName;
    private final String headerValue;

    public InvalidHeaderValueValidationError(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Invalid value {0} format for header {1}.", headerValue, headerName);
    }
}