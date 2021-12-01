package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.*;
import edu.agh.wfiis.solid.srp.example1.model.ConstraintViolation;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.Validator;

import java.util.List;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public void validate(Constraints validationConstraints) throws ConstraintViolationException {
        Validator validator = new Validator(validationConstraints);
        List<ConstraintViolation> violations = validator.validate(muleMessage);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}