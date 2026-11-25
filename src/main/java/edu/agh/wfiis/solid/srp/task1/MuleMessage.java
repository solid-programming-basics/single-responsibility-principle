package edu.agh.wfiis.solid.srp.task1;

import java.util.HashMap;
import java.util.Map;

public class MuleMessage {

    private final Map<String, Object> inboundProperties = new HashMap<>();

    public String getHeader(String headerName) {
        return (String) inboundProperties.get(headerName);
    }

    public void setHeader(String headerName, String value) {
        inboundProperties.put(headerName, value);
    }
}
