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

    /**
     * Zdeprecjonowana metoda zachowana ze względu na kompatybilność wsteczną. Metoda jest równocześnie odpowiedzialna
     * za walidację headerów, ustawienie brakujących headerów oraz służy jako getter dla pola muleMessage
     */
    @Deprecated
    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        assertHeadersMeetConstraints(validationConstraints);
        setMissingHeadersToDefaultInternal(validationConstraints);
        return muleMessage;
    }

    /**
     * Ustawiamy brakujące nagłówki na domyślne wartości. Metoda ta powinna być wywoływana z zewnątrz klasy (po zdeprecjonowaniu
     * metody validate)
     */
    public void setMissingHeadersToDefault(Constraints validationConstraints){
        setMissingHeadersToDefaultInternal(validationConstraints);
    }

    /**
     * Zwracamy listę błędów, pusta lista posłuży nam do stwierdzenia, że żądanie jest poprawne. Po zdeprecjonowaniu metody validate,
     * metoda ta jest zalecana do walidacji żądania pod kątem headerów.
     * @param validationConstraints
     * @return - lista błędów (być może pusta)
     */
    public List<HeaderValidationError> validateRequestHeaders(Constraints validationConstraints)
    {
        return validateHeaders(validationConstraints);
    }
    
    /**
     * getter dla muleMessage, zalecane jest jego używanie po zdeprecjonowaniu metody validate()
     */
    public MuleMessage getMuleMessage()
    {
        return muleMessage;
    }

    private void setMissingHeadersToDefaultInternal(Constraints validationConstraints){
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (isHeaderStringNull(headerValue) && constraint.getDefaultValue() != null) {
                muleMessage.setHeader(headerName, constraint.getDefaultValue());
            }
        }
    }

    private List<HeaderValidationError> validateHeaders(Constraints validationConstraints) {
        var validationErrorsBuilder = ValidationResults.Builder.getBuilderInstance(); 

        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (isHeaderStringNull(headerValue) && constraint.isHeaderRequired()) {
                MissingHeader missingHeaderError = MissingHeader.fromHeaderName(headerName);
                validationErrorsBuilder.addError(missingHeaderError);
            }

            if (isHeaderStringNotNull(headerValue) && !constraint.validate(headerValue)) {
                HeaderValidationError headerValidationError = HeaderValidationError.fromHeaderNameAndValue(headerName, headerValue);
                validationErrorsBuilder.addError(headerValidationError);
            }
        }

        return validationErrorsBuilder.build().getValidationErrors();
    }

    /** Zdeprecjonowana metoda, mogąca wyrzucać wyjątek InvalidHeaderException.
     * Jej obecność w klasie wynika z konieczności kompatybilności wstecznej dla publicznej metody validate
     */
    @Deprecated
    private final void assertHeadersMeetConstraints(Constraints validationConstraints) throws InvalidHeaderException {
        for(Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (isHeaderStringNull(headerValue) && constraint.isHeaderRequired()) {
                MissingHeader missingHeaderError = MissingHeader.fromHeaderName(headerName);
                throw new InvalidHeaderException(missingHeaderError.getErrorMessage());
            }

            if (isHeaderStringNotNull(headerValue) && !constraint.validate(headerValue)) {
                HeaderValidationError headerValidationError = HeaderValidationError.fromHeaderNameAndValue(headerName, headerValue);
                throw new InvalidHeaderException(headerValidationError.getErrorMessage());
            }
        }
    }

    private final boolean isHeaderStringNull(String headerString)
    {
        return (headerString == null);
    }

    private final boolean isHeaderStringNotNull(String headerString)
    {
        return !isHeaderStringNull(headerString);
    }
}

abstract class HeaderValidationError {
    protected String headerName;

    abstract public String getErrorMessage();
}

class MissingHeader extends HeaderValidationError {

    public static MissingHeader fromHeaderName(String headerName)
    {
        var missingHeader = new MissingHeader();
        missingHeader.headerName = headerName;

        return missingHeader;
    }

    @Override
    public String getErrorMessage() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidValueFormatInHeader extends HeaderValidationError {
    private String headerValue;

    public static InvalidValueFormatInHeader fromHeaderNameAndValue(String headerName, String headerValue)
    {
        var invalidValueFormatInHeader = new InvalidValueFormatInHeader();
        invalidValueFormatInHeader.headerName = headerName;
        invalidValueFormatInHeader.headerValue = headerValue;

        return invalidValueFormatInHeader;
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

    public List<HeaderValidationError> getValidationErrors()
    {
        return validationErrors;
    }

    public static class Builder {
        private List<HeaderValidationError> validationErrors = new ArrayList<>();

        public static Builder getBuilderInstance()
        {
            return new Builder();
        }

        public Builder addError(HeaderValidationError headerValidationError){
            this.validationErrors.add(headerValidationError);
            return this;
        }

        public ValidationResults build(){
            return new ValidationResults(this);
        }
    }
}
