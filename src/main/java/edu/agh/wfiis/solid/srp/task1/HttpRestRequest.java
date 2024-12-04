package edu.agh.wfiis.solid.srp.task1;

import java.util.List;

public class HttpRestRequest {

    public HttpRestRequest() {
    }

    public MuleMessage process(MuleMessage muleMessage, List<Constraint> validationConstraints) throws InvalidHeaderException {
        HeaderValidator headerValidator = new HeaderValidator();
        DefaultHeadersValueSetter defaultHeadersValueSetter = new DefaultHeadersValueSetter();

        muleMessage = defaultHeadersValueSetter.setDefaultValues(muleMessage, validationConstraints);

        ValidationStatus status = headerValidator.validate(muleMessage, validationConstraints);
        if(!status.isValid()){
            throw new InvalidHeaderException(String.join(",", status.getErrorMessages()));
        }
        return muleMessage;
    }

}
