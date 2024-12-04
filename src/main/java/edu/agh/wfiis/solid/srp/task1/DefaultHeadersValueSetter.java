package edu.agh.wfiis.solid.srp.task1;

import java.util.ArrayList;
import java.util.List;

public class DefaultHeadersValueSetter {

    public MuleMessage setDefaultValues(MuleMessage originalMuleMessage,List<Constraint> constraints) {
        MuleMessage muleMessage = originalMuleMessage.copy();
        List<Constraint> defaultValuesForHeaders = getDefaultValuesForHeaders(muleMessage, constraints);
        for (Constraint defaultValueProvider : defaultValuesForHeaders) {
            String headerName = defaultValueProvider.getHeaderName();
            String headerValue = defaultValueProvider.getDefaultValue();
            muleMessage.setHeader(headerName, headerValue);
        }
        return muleMessage;
    }

    private  List<Constraint> getDefaultValuesForHeaders(MuleMessage muleMessage, List<Constraint> constraints) {
        List<Constraint> headerConstraints = new ArrayList<>();

        for (Constraint constraint : constraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            if (headerValue == null && constraint.getDefaultValue() != null) {
                headerConstraints.add(constraint);
            }
        }
        return headerConstraints;
    }
}
