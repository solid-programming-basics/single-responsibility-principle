package edu.agh.wfiis.solid.srp.example1.validate;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HeaderValidator {
    public static void throwHeaderUnspecifiedError(String headerName) throws InvalidHeaderException{
        throw new InvalidHeaderException("Required header " + headerName + " not specified");
    }

    public static void throwHeaderInvalidFormat(String headerName) throws InvalidHeaderException{
        throw new InvalidHeaderException(MessageFormat.format("Invalid value format for header {0}.", headerName));
    }

    public static void validateHeaderRequired(String headerValue, Constraint constraint, String headerName)  throws InvalidHeaderException{
        if (checkFfHeaderValueIsNull(headerValue) && checkHeaderRequired(constraint)){throwHeaderUnspecifiedError(headerName);}
    }

    public static void validateHeaderDefaultValue(String headerValue, String headerName, Constraint constraint, MuleMessage muleMessage){
        if (checkFfHeaderValueIsNull(headerValue) && checkHeaderDefaultValue(constraint)){setHeaderDefaultValue(muleMessage, headerName, constraint);}
    }

    public static void validateHeaderFormat(String headerValue, String headerName, Constraint constraint) throws InvalidHeaderException{
        if (checkFfHeaderValueIsNotNull(headerValue) && checkHeaderFormat(constraint, headerValue)){throwHeaderInvalidFormat(headerName);}
    }

    public static boolean checkFfHeaderValueIsNotNull(String headerValue){
        return headerValue != null;
    }

    public static boolean checkFfHeaderValueIsNull(String headerValue){
        return headerValue == null;
    }

    public static boolean checkHeaderRequired(Constraint constraint){
        return constraint.isHeaderRequired();
    }

    public static boolean checkHeaderDefaultValue(Constraint constraint){
        return constraint.getDefaultValue() != null;
    }

    public static boolean checkHeaderFormat(Constraint constraint, String headerValue){
        return !constraint.validate(headerValue);
    }

    public static void setHeaderDefaultValue(MuleMessage muleMessage, String headerName, Constraint constraint){
        muleMessage.setHeader(headerName, constraint.getDefaultValue());
    }
}
