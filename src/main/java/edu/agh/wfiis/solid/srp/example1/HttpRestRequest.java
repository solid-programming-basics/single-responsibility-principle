package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class HttpRestRequest {

    public HttpRestRequest() {}

    public List<HeaderValidationError> validateHeaders(MuleMessage muleMessage, List<Constraint> headerConstraints) {
        List<HeaderValidationError> errorMessages = new ArrayList<>();

        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                errorMessages.add(new MissingHeaderValidationError(headerName));
            }

            if (headerValue != null) {
                if (!constraint.validate(headerValue)) {
                    errorMessages.add(new InvalidHeaderValueValidationError(headerName, headerValue));
                }
            }
        }

        return errorMessages;
    }

    public void setMissingHeadersDefaultValues(MuleMessage muleMessage, List<Constraint> headerConstraints){
        for (Constraint constraint : headerConstraints) {
            setHeaderDefaultValueIfMissing(constraint, muleMessage);
        }
    }

    @Deprecated
    protected MuleMessage muleMessage;
    @Deprecated
    protected Constraints validationConstraints;
    
    @Deprecated
    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    @Deprecated
    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;

        List<HeaderValidationError> headerValidationErrors = validateHeaders(muleMessage, validationConstraints.getHeaderConstraints());

        if(!headerValidationErrors.isEmpty()) {
            throw new InvalidHeaderException(headerValidationErrors.get(0).getError());
        }

        setMissingHeadersDefaultValues(muleMessage, validationConstraints.getHeaderConstraints());
        return muleMessage;
    }

    private void setHeaderDefaultValueIfMissing(Constraint constraint, MuleMessage muleMessage){
        if (muleMessage.getHeader(constraint.getHeaderName()) == null && constraint.getDefaultValue() != null) {
            setDefaultHeaderValue(constraint, muleMessage);
        }
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