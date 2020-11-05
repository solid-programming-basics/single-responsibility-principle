package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class HeaderValidationErrors extends InvalidHeaderException
{
    private List<HeaderValidationResult> errorList;
        
    public HeaderValidationErrors(String s)
    {
        super(s);
        errorList = null;
    }
        
    public static HeaderValidationErrors create(List<HeaderValidationResult> errorList)
    {   
        HeaderValidationErrors me = new HeaderValidationErrors(errorList.toString());
        me.errorList = errorList;
        return me;
    }

    public List<HeaderValidationResult> getErrorList()
    {
        return errorList;
    }
}
