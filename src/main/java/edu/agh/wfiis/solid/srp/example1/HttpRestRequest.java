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

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    @Deprecated
    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        List<HeaderValidationError> headerValidationErrors = checkForHeaderValidationErrors(validationConstraints.getHeaderConstraints());
        if (!headerValidationErrors.isEmpty()) {
            throw new InvalidHeaderException(headerValidationErrors.get(0).getError());
        }
        setMissingHeadersDefaultValues(validationConstraints);
        return muleMessage;
    }

    public void setMissingHeadersDefaultValues(Constraints validationConstraints) {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            tryToSetDefaultHeaderValue(constraint, muleMessage);
        }
    }

    public MuleMessage getMuleMessage() {return muleMessage;}

    public List<HeaderValidationError> validateMessageAgainstConstraints(Constraints validationConstraints) {
        List<HeaderValidationError> headerValidationErrors = checkForHeaderValidationErrors(validationConstraints.getHeaderConstraints());
        return headerValidationErrors;
    }

    private List<HeaderValidationError> checkForHeaderValidationErrors(List<Constraint> headerConstraints) {
        List<HeaderValidationError> errorMessages = new ArrayList<>();

        for (Constraint constraint : headerConstraints) {
            HeaderValidationError error = checkMessageHeaderAgainstConstraint(constraint);
            if (error != null) {
                errorMessages.add(error);
            }
        }

        return errorMessages;
    }

    private HeaderValidationError checkMessageHeaderAgainstConstraint(Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null && constraint.isHeaderRequired()) {
            return new MissingHeaderValidationError(headerName);
        }
        else if (!constraint.validate(headerValue)) {
            return new InvalidHeaderValueValidationError(headerName, headerValue);
        }
        else {
            return null;
        }
    }

    private boolean tryToSetDefaultHeaderValue(Constraint constraint, MuleMessage muleMessage){
        if (muleMessage.getHeader(constraint.getHeaderName()) == null && constraint.getDefaultValue() != null) {
            setDefaultHeaderValue(constraint, muleMessage);
            return true;
        }
        return false;
    }

    private void setDefaultHeaderValue(Constraint constraint, MuleMessage muleMessage){
        muleMessage.setHeader(constraint.getHeaderName(), constraint.getDefaultValue());
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
