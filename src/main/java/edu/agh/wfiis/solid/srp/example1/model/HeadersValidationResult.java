package edu.agh.wfiis.solid.srp.example1.model;

import java.util.ArrayList;
import java.util.List;

public class HeadersValidationResult {
    private final List<InvalidHeaderException> headerExceptionList;


    public HeadersValidationResult() {
        headerExceptionList = new ArrayList<>();
    }

    public boolean isValid() {
        return headerExceptionList.size() == 0;
    }

    public void addHeaderException(InvalidHeaderException headerException) {
        headerExceptionList.add(headerException);
    }

    public List<InvalidHeaderException> getHeaderExceptionList() {
        return headerExceptionList;
    }
}
