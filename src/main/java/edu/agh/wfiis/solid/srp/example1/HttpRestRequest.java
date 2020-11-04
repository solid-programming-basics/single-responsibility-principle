package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class HttpRestRequest 
{
    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) 
    {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException 
    {
        List<HeaderValidationResult> errorList = validateHeaders(validationConstraints);
        if (errorList.size() != 0)
            throw HeaderValidationErrors.create(errorList);
        setMissingDefaultHeaders(validationConstraints);
        return muleMessage;
    }

    private List<HeaderValidationResult> validateHeaders(Constraints validationConstraints)
    {        
        List<HeaderValidationResult> errorList = new ArrayList<>();
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) 
        {
            HeaderValidationResult result = validateHeader(constraint); 
            if (result.getCode() != 0)
                errorList.add(result);
        }
        return errorList;
    }
    
    private HeaderValidationResult validateHeader(Constraint constraint)
    {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null)
        {
            if (constraint.isHeaderRequired()) 
                return new HeaderValidationResult(1, constraint);
        }
        else
        {
            if (!constraint.validate(headerValue)) 
                return new HeaderValidationResult(2, constraint);
        }
        return new HeaderValidationResult(0, constraint);
    }
    
    private boolean isMissingHeaderValue(String headerValue, Constraint constraint)
    {
        return ((headerValue == null) && (constraint.getDefaultValue() != null));
    }
    
    private void setMissingDefaultHeaders(Constraints validationConstraints)
    {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) 
            setMissingDefaultHeader(constraint);
    }
        
    private void setMissingDefaultHeader(Constraint constraint)
    {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);
        
        if (isMissingHeaderValue(headerValue, constraint)) 
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
    }
}

