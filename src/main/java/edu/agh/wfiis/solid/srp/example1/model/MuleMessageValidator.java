package edu.agh.wfiis.solid.srp.example1.model;
import java.text.MessageFormat;

public class MuleMessageValidator {
    public void validateRequiredHeaders(MuleMessage muleMessage, Constraints constraints) throws InvalidHeaderException
    {
        for (Constraint constraint : constraints.getHeaderConstraints())
        {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            Boolean compliesToRequirement = constraint.isHeaderRequired() ? headerValue != null : true ;
            
            if(!compliesToRequirement)
            {
                throw new InvalidHeaderException("Required header " + headerName + " not specified");
            }
        }
    }

    public void validateHeaderPattern(MuleMessage muleMessage, Constraints constraints) throws InvalidHeaderException
    {
        for (Constraint constraint : constraints.getHeaderConstraints())
        {
            String headerPattern = constraint.getHeaderPattern();
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            
            Boolean doesHeaderPatternMatch = headerPattern == null ? true : headerPattern.matches(headerValue);

            if(!doesHeaderPatternMatch)
            {
                throw new InvalidHeaderException(
                    MessageFormat.format("Invalid value format for header {0}.", headerName));
            }
        }
    }
}