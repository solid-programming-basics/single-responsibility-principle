package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class HeaderValidationResult
{
    private int code;
    private String name;
    private Constraint constraint;
    
    static final int hvrOK = 0;
    static final int hvrHeaderNotSpecified = 1;
    static final int hvrInvalidValueFormat = 2;
    static final int hvrUnknown = -1;
    
    ///tworzenie dowolnych błędów - także tych niezawartych w drugim konstroktorze
    public HeaderValidationResult(int code, String name, Constraint constraint)
    {
        setValues(code, name, constraint);
    }
    
    ///automatyzacja już znanych błędów
    public HeaderValidationResult(int code, Constraint constraint)
    {
        switch (code) //tablica stałych i sprawdzanie warunku kodu w zakresie tablicy
        {
            case hvrOK:
                setValues(code, "OK", constraint); 
                break;
            case hvrHeaderNotSpecified: 
                setValues(code, "Required header not specified", constraint);
                break;
            case hvrInvalidValueFormat:
                setValues(code, "Invalid value format for the header", constraint); 
                break;
            default:
                setValues(code, "Unknown error", constraint); 
        }
    }
    
    private void setValues(int code, String name, Constraint constraint)
    {
        this.code = code;
        this.name = name;
        this.constraint = constraint;
    }
    
    public Integer getCode()
    {
        return code; 
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getValue()
    {
        return constraint.getHeaderName();
    }
    
    public Constraint getConstraint()
    {
        return constraint;
    }
    
    public String toString()
    {
        return "Error: " + getCode().toString() + " " + getName() + ": " + getValue();
    }
}
