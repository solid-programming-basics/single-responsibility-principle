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

    public ValidationResults validateHeaders(Constraints validationConstraints) {
        ValidationResults.Builder validationResultsBuilder = new ValidationResults.Builder();
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null)
                if (constraint.isHeaderRequired())
                    validationResultsBuilder.addError(new MissingHeader(headerName));

            if (headerValue != null)
                if (!constraint.validate(headerValue))
                    validationResultsBuilder.addError(new InvalidValueFormatInHeader(headerName, headerValue));
        }

        return validationResultsBuilder.build();
    }


    public void setMissingHeadersToDefault(Constraints constraints){
        for (Constraint constraint : constraints.getHeaderConstraints())
            setMissingHeaderToDefault(constraint);
    }

    @Deprecated
    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            assertHeaderMeetConstraints(constraint);
            setMissingHeaderToDefault(constraint);
        }

        return muleMessage;
    }

    @Deprecated
    private void assertHeaderMeetConstraints(Constraint constraint) throws InvalidHeaderException {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null)
            if (constraint.isHeaderRequired())
                throw new InvalidHeaderException("Required header " + headerName + " not specified");

        if (headerValue != null)
            if (!constraint.validate(headerValue))
                throw new InvalidHeaderException(MessageFormat.format("Invalid value format for header {0}.", headerName));
    }

    private void setMissingHeaderToDefault(Constraint constraint){
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null && constraint.getDefaultValue() != null)
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
    }
}

abstract class HeaderValidationError {
    protected String headerName;

    HeaderValidationError(String headerName){
        this.headerName = headerName;
    }

    abstract public String getErrorMessage();
}

class MissingHeader extends HeaderValidationError {

    MissingHeader(String headerName){
        super(headerName);
    }

    @Override
    public String getErrorMessage() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidValueFormatInHeader extends HeaderValidationError {
    private String headerValue;

    InvalidValueFormatInHeader(String headerName, String headerValue){
        super(headerName);
        this.headerValue = headerValue;
    }

    @Override
    public String getErrorMessage() {
        return MessageFormat.format("Invalid value {0} for header {1}.", headerValue, headerName);
    }
}

class ValidationResults {
    private final List<HeaderValidationError> validationErrors;

    private ValidationResults(Builder builder){
        this.validationErrors = builder.validationErrors;
    }

    public static class Builder {
        private List<HeaderValidationError> validationErrors = new ArrayList<>();

        public Builder addError(HeaderValidationError error){
            this.validationErrors.add(error);
            return this;
        }

        public ValidationResults build(){
            return new ValidationResults(this);
        }
    }
}
