package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.Header;
import edu.agh.wfiis.solid.srp.example1.validator.HeaderValidator;
import edu.agh.wfiis.solid.srp.example1.model.RestRequestHeader;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage getMuleMessage(Constraints validationConstraints) throws InvalidHeaderException {
        return buildMuleMessage(validationConstraints);
    }

    private MuleMessage buildMuleMessage(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            addPropertyToMuleMessage(constraint);
        }
        return muleMessage;
    }

    private void addPropertyToMuleMessage(Constraint constraint) throws InvalidHeaderException {
        Header header = buildValidHeader(constraint);
        addHeader(header);
    }

    private Header buildValidHeader(Constraint constraint) throws InvalidHeaderException {
        Header header = buildRestRequestHeader(constraint);
        HeaderValidator.validate(header);
        return header;
    }

    private Header buildRestRequestHeader(Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);
        return RestRequestHeader.builder()
                .constraint(constraint)
                .value(headerValue)
                .build();
    }

    private void addHeader(Header header) {
        if (header.hasOnlyDefaultValue()) {
            Constraint constraint = header.getConstraint();
            muleMessage.setHeader(constraint.getHeaderName(), constraint.getDefaultValue());
        }
    }
}