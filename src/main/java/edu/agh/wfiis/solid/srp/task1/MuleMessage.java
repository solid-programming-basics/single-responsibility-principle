package edu.agh.wfiis.solid.srp.task1;

import java.util.HashMap;
import java.util.Map;

public class MuleMessage {

    private Map<String, String> inboundProperties;

    public String getHeader(String headerName) {
        return inboundProperties.get(headerName);
    }

    public void setHeader(String headerName, String value) {
        if (inboundProperties == null) {
            inboundProperties = new HashMap<>();
        }
        inboundProperties.put(headerName, value);
    }
}
