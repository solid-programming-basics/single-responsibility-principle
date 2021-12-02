package edu.agh.wfiis.solid.srp.example1.model;

public class DefaultValue {

    private final String headerName;
    private final String value;

    public DefaultValue(String header, String value) {
        this.headerName = header;
        this.value = value;
    }

    public String getHeaderName() {
        return headerName;
    }

    public String getValue() {
        return value;
    }
}
