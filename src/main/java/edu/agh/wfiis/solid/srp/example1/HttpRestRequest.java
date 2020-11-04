package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class HttpRestRequest {

    public HttpRestRequest(){ }

    public HeaderValidationResult validateMuleMessageHeaders(MuleMessage muleMessage, Constraints headerValidationConstraints){
        BiPredicate<String, Constraint> isRequiredHeaderMissing = (headerValue, constraint) -> headerValue == null && constraint.isHeaderRequired();
        BiPredicate<String, Constraint> isExistingHeaderValueInvalid = (headerValue, constraint) -> headerValue != null && (!constraint.validate(headerValue));

        return HeaderValidationResult.ofList(headerValidationConstraints.getHeaderConstraints().stream().map(constraint -> {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if(isRequiredHeaderMissing.test(headerValue, constraint)){
                return new RequiredHeaderMissingError(headerName);
            }
            if(isExistingHeaderValueInvalid.test(headerValue, constraint)){
                return new InvalidHeaderValueError(headerName, headerValue);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }


    /**
     * @param muleMessage - WARNING this argument will be modified by the function
     * Usage of output argument is forced by MuleMessage API - MuleMessage does not allow to easily clone instance.
     */
    public void setMissingHeadersDefaultValuesInMuleMessage(MuleMessage muleMessage, Map<String, String> defaultHeaderValuesByHeaderNames){
        BiPredicate<String, String> shouldSetDefaultHeaderValue = (headerValue, defaultHeaderValue) -> headerValue == null && defaultHeaderValue != null;

        defaultHeaderValuesByHeaderNames.entrySet().stream()
                .filter(entry -> shouldSetDefaultHeaderValue.test(muleMessage.getHeader(entry.getKey()), entry.getValue()))
                .forEach(entry -> muleMessage.setHeader(entry.getKey(), entry.getValue()));
    }

    /**
     * this method is deprecated as it violates SRP
     * to get similar effect using new API use:
     * httpRestRequest.validateMuleMessageHeaders(muleMessage, headerValidationConstraints);
     * httpRestRequest.setMissingHeadersDefaultValuesInMuleMessage(muleMessage, defaultHeaderValuesByHeaderNames);
     */
    @Deprecated
    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;

        final HeaderValidationResult validationResult = validateMuleMessageHeaders(muleMessage, validationConstraints);
        System.out.println(validationResult.getStringifiedHeaderValidationErrorByHeaderName());
        if(!validationResult.isValidationPassed()) throw new InvalidHeaderException(validationResult.getValidationError(0).getError());

        Map<String, String> defaultHeaderValuesByHeaderNames = validationConstraints.getHeaderConstraints().stream().collect(Collectors.toMap(Constraint::getHeaderName, Constraint::getDefaultValue));
        setMissingHeadersDefaultValuesInMuleMessage(muleMessage, defaultHeaderValuesByHeaderNames);

        return muleMessage;
    }


    @Deprecated
    protected MuleMessage muleMessage;

    @Deprecated
    protected Constraints validationConstraints;

    @Deprecated
    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }
}

interface HeaderValidationError{
    String getHeaderName();
    String getError();
}

class RequiredHeaderMissingError implements HeaderValidationError{

    private final String headerName;

    public RequiredHeaderMissingError(String headerName){
        this.headerName = headerName;
    }

    @Override
    public String getHeaderName() {
        return headerName;
    }

    @Override
    public String getError() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidHeaderValueError implements  HeaderValidationError{

    private final String headerName;
    private final String headerValue;

    public InvalidHeaderValueError(String headerName,String headerValue){
        this.headerName=headerName;
        this.headerValue=headerValue;
    }

    @Override
    public String getHeaderName() {
        return headerName;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Invalid value {0} format for header {1}.",headerValue, headerName);
    }
}

class HeaderValidationResult {
    private final List<HeaderValidationError> validationErrors;

    public static HeaderValidationResult ofList(List<HeaderValidationError> validationErrors){
        return new HeaderValidationResult(validationErrors);
    }

    private HeaderValidationResult(List<HeaderValidationError> validationErrors){
        this.validationErrors = validationErrors;
    }

    public boolean isValidationPassed(){
        return validationErrors.isEmpty();
    }

    public HeaderValidationError getValidationError(int index){
        if(index>=validationErrors.size()) throw new IndexOutOfBoundsException();
        return validationErrors.get(index);
    }

    public String getStringifiedHeaderValidationErrorByHeaderName(){
        return validationErrors.stream().map(error -> error.getHeaderName() + ": " + error.getError()).collect(Collectors.joining("\n"));
    }

    public Map<String, HeaderValidationError> getHeaderValidationErrorByHeaderName(){
        return validationErrors.stream().collect(Collectors.toMap(HeaderValidationError::getHeaderName, error->error));
    }

}
