package edu.agh.wfiis.solid.srp.task1;

import java.util.HashMap;
import java.util.Map;

public class MuleMessage {

    private Map<String, Object> inboundProperties;

    public String getHeader(String headerName) {
        return (String) inboundProperties.get(headerName);
    }

    public void setHeader(String headerName, String value) {
        if (inboundProperties == null) {
            inboundProperties = new HashMap<>();
        }
        inboundProperties.put(headerName, value);
    }

    public MuleMessage copy(){
        MuleMessage copy = new MuleMessage();
        copy.inboundProperties = new HashMap<>(this.inboundProperties);
        return copy;
    }
}
