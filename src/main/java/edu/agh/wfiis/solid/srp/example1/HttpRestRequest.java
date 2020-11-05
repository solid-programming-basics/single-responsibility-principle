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
    
    @Deprecated
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    @Deprecated
    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;
        List<Constraint> headerConstraints = validationConstraints.getHeaderConstraints();

        ValidationResult headersValidationResult = validateHeaders(headerConstraints);
        if (!headersValidationResult.isValid()) {
            HeaderValidationError firstValidationError = headersValidationResult.getValidationErrors().get(0);
            throw new InvalidHeaderException(firstValidationError.getErrorMessage());
        }

        setMissingHeadersDefaultValues(headerConstraints);
        
        return muleMessage;
    }

    public ValidationResult<HeaderValidationError> validateHeaders(List<Constraint> headerConstraints) {
        ValidationResult<HeaderValidationError> validationResult = new ValidationResult<HeaderValidationError>();

        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                validationResult.addError(new MissingHeaderValidationError(headerName));
            }

            if (headerValue != null && !constraint.validate(headerValue)) {
                validationResult.addError(new InvalidHeaderValueValidationError(headerName, headerValue));
            }
        }

        return validationResult;
    }

    public void setMissingHeadersDefaultValues(List<Constraint> headerConstraints) {
        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerDefaultValue = constraint.getDefaultValue();
            if (muleMessage.getHeader(headerName) == null && headerDefaultValue != null) {
                muleMessage.setHeader(headerName, headerDefaultValue);
            }
        }
    }
}

class ValidationResult<T> {
    private List<T> errors = new ArrayList<T>();

    ValidationResult() {}

    public List<T> getValidationErrors() {
        return this.errors;
    }

    public addError(T error) {
        this.errors.add(error);
    }

    public isValid() {
        return this.errors.isEmpy();
    }
}


abstract class HeaderValidationError {
    protected final String headerName;
    
    HeaderValidationError(String headerName) {
        this.headerName = headerName;
    }
    public getHeaderName() {
        return this.headerName;
    }

    abstract String getErrorMessage();
}


class MissingHeaderValidationError extends HeaderValidationError {
    public MissingHeaderValidationError(String headerName) {
        super(headerName);
    }

    @Override
    public String getErrorMessage() {
        return "Required header " + headerName + " not specified";
    }
}


class InvalidHeaderValueValidationError extends HeaderValidationError {
    private final String headerValue;

    public InvalidHeaderValueValidationError(String headerName, String headerValue) {
        super(headerName);
        this.headerValue = headerValue;
    }

    @Override
    public String getErrorMessage() {
        return MessageFormat.format("Invalid value {0} format for header {1}.", headerValue, headerName);
    }
}