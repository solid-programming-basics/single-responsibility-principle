package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

interface HeaderValidationError {
    String getError();
}

public class HttpRestRequest {
    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {

        final List<HeaderValidationError> headerValidationErrors = validateHeaders(validationConstraints.getHeaderConstraints());

        if (!headerValidationErrors.isEmpty()) {
            throw new InvalidHeaderException(headerValidationErrors.get(0).getError());
        }

        setMissingHeadersDefaultValues(validationConstraints.getHeaderConstraints());
        return muleMessage;
    }

    private List<HeaderValidationError> validateHeaders(List<Constraint> headerConstraints) {
        final List<HeaderValidationError> errorMessages = new ArrayList<>();

        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (Objects.isNull(headerValue) && constraint.isHeaderRequired()) {
                errorMessages.add(new MissingHeaderValidationError(headerName));
            }

            if (Objects.nonNull(headerValue) && !constraint.validate(headerValue)) {
                errorMessages.add(new InvalidHeaderValueValidationError(headerName, headerValue));
            }
        }

        return errorMessages;
    }

    private void setMissingHeadersDefaultValues(List<Constraint> headerConstraints) {
        for (Constraint constraint : headerConstraints) {
            tryToSetDefaultHeaderValue(constraint, muleMessage);
        }
    }

    private void tryToSetDefaultHeaderValue(Constraint constraint, MuleMessage muleMessage) {
        if (Objects.isNull(muleMessage.getHeader(constraint.getHeaderName())) && Objects.nonNull(constraint.getDefaultValue())) {
            setDefaultHeaderValue(constraint, muleMessage);
        }
    }

    private void setDefaultHeaderValue(Constraint constraint, MuleMessage muleMessage) {
        muleMessage.setHeader(constraint.getHeaderName(), constraint.getDefaultValue());
    }
}

class MissingHeaderValidationError implements HeaderValidationError {
    private final String headerName;

    public MissingHeaderValidationError(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String getError() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidHeaderValueValidationError implements HeaderValidationError {
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