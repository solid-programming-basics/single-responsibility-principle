package edu.agh.wfiis.solid.srp.task1;

public class HttpRestRequest {

    private final HeaderValueResolver headerValueResolver;
    private final HeaderValidator headerValidator;

    public HttpRestRequest() {
        headerValueResolver = new HeaderValueResolver();
        headerValidator = new HeaderValidator();
    }

    public MuleMessage process(MuleMessage muleMessage, Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerValue = headerValueResolver.resolve(muleMessage, constraint);
            headerValidator.validate(headerValue, constraint);
        }
        return muleMessage;
    }
}
