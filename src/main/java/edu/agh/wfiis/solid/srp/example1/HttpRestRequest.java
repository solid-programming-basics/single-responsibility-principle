package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;

public class HttpRestRequest {

    protected MuleMessage muleMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    //////////////////////////////////////////// VALIDATION //////////////////////////////////////////////////////////

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints.getHeaderConstraints();
        List<HeaderValidationException> headerValidationExceptions = validateHeaders(validationConstraints);

        if(!headerValidationExceptions.isEmpty()) {
            throw new InvalidHeaderException(headerValidationExceptions.get(0).getException());
        }

        fillMissingHeaders();

        return muleMessage;
    }

    private void fillMissingHeaders(){
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerValue = muleMessage.getHeader(constraint.getHeaderName());

            if (headerValue == null && constraint.getDefaultValue() != null) {
                muleMessage.setHeader(constraint.getHeaderName(), constraint.getDefaultValue());
            }
        }
    }

    private List<HeaderValidationException> validateHeaders(List<Constraint> headerConstraints) {
        List<HeaderValidationException> exceptionsArray = new ArrayList<>();

        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                exceptionsArray.add(new MissingValueHeaderValidationException(headerName));
            }

            if (headerValue != null) {
                if (!constraint.validate(headerValue)) {
                    exceptionsArray.add(new InvalidValueHeaderValidationException(constraint.getPattern(), headerValue));
                }
            }
        }

        return exceptionsArray;
    }
}

//////////////////////////////////////////// EXCEPTIONS //////////////////////////////////////////////////////////

interface HeaderValidationException {
    String getException();
}

class MissingValueHeaderValidationException  implements HeaderValidationException {
    private final String headerName;

    public MissingValueHeaderValidationException(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String getException() {
        return MessageFormat.format("No value specified for required header: \"{0}\"", headerName);
    }
}

class InvalidValueHeaderValidationException  implements HeaderValidationException {
    private final String headerName;
    private final String headerValue;
    private final Constraint constraint;

    public InvalidValueHeaderValidationException(String pattern, String headerValue) {
        this.pattern = pattern;
        this.headerValue = headerValue;
        this.headerName = constraint.getHeaderName();
    }

    @Override
    public String getException() {
        return MessageFormat.format("Invalid value: \"{0}\" in header {1} [Pattern: {2}]", headerValue, headerName, pattern);
    }
}