package edu.agh.wfiis.solid.srp.example1.model;

import java.util.HashMap;
import java.util.Map;

public class DefaultValuesProvider
{ 
    protected Map<String, String> headerValueMap = new HashMap<String, String>();

    public DefaultValuesProvider()
    {
        headerValueMap.put("Content-Type", "application/json");
        headerValueMap.put("Accept", "application/json;q=0.9,*/*;q=0.8");
    }

    public String getDefaultHeaderValueFor(String headerName)
    {
        return headerValueMap.get(headerName);
    }
}