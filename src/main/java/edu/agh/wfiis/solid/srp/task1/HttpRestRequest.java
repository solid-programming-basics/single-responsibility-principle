package edu.agh.wfiis.solid.srp.task1;

import java.text.MessageFormat;

public class HttpRestRequest {

    private final DefaultHeaderPopulator headerPopulator;
    private final HeaderValidator headerValidator;

    public HttpRestRequest() {
        this(new DefaultHeaderPopulator(), new HeaderValidator());
    }

    public HttpRestRequest(DefaultHeaderPopulator headerPopulator, HeaderValidator headerValidator) {
        this.headerPopulator = headerPopulator;
        this.headerValidator = headerValidator;
    }

    public MuleMessage process(MuleMessage muleMessage, Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerValue = headerPopulator.resolveHeaderValue(muleMessage, constraint);
            headerValidator.validate(headerValue, constraint);
        }
        return muleMessage;
    }
}
