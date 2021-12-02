package edu.agh.wfiis.solid.srp.example1.model;

import java.util.ArrayList;
import java.util.List;

public class DefaultValues {

    private final List<DefaultValue> defaultValueList;

    public DefaultValues() {
        defaultValueList = new ArrayList<>();
        defaultValueList.add(new DefaultValue("Content-Type", "application/json"));
    }

    public List<DefaultValue> getDefaultValueList() {
        return defaultValueList;
    }
}
