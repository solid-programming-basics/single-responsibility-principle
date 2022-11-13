package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.DefaultValuesProvider;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

public class HttpRestRequestBuilder {

    public HttpRestRequest createHttpRestRequest(MuleMessage muleMessage, Constraints constraints) {
        muleMessage = applyDefaultValues(muleMessage, constraints, new DefaultValuesProvider());
        return new HttpRestRequest(muleMessage);
    }

    private MuleMessage applyDefaultValues(MuleMessage muleMessage, Constraints constraints, DefaultValuesProvider defaultValuesProvider) {
        for (Constraint constraint : constraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            Boolean shouldApplyDefaultValue = headerValue == null
                    && defaultValuesProvider.getDefaultHeaderValueFor(headerName) != null;

            if (shouldApplyDefaultValue) {
                muleMessage.setHeader(headerName, defaultValuesProvider.getDefaultHeaderValueFor(headerName));
            }
        }

        return muleMessage;
    }
}
