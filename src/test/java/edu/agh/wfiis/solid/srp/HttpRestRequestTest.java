package edu.agh.wfiis.solid.srp;


import edu.agh.wfiis.solid.srp.example1.HttpRestRequest;
import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.util.ArrayList;

public class HttpRestRequestTest {

    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private static final Constraints VALIDATION_CONTRACT = prepareValidationContract();

    private MuleMessage testMessage;

    @org.junit.Before
    public void setUp() throws Exception {
        testMessage = new MuleMessage();
    }

    @org.junit.Test
    public void validate() throws Exception {
        testMessage.setHeader(CONTENT_TYPE_HEADER_NAME, "application/json");
        new HttpRestRequest(testMessage).validateHeaders(VALIDATION_CONTRACT);
    }

    private static Constraints prepareValidationContract() {
        Constraint contentType = new Constraint();
        contentType.setHeaderName(CONTENT_TYPE_HEADER_NAME);
        contentType.setHeaderRequired(true);

        Constraint accept = new Constraint();
        accept.setHeaderName(ACCEPT_HEADER_NAME);
        accept.setHeaderName("application/json;q=0.9,*/*;q=0.8");

        Constraints constraints = new Constraints();
        constraints.add(new ArrayList<Constraint>() {{
            add(contentType);
            add(accept);
        }});
        return constraints;
    }

}