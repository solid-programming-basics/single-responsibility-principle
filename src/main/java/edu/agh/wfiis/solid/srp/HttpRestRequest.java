package edu.agh.wfiis.solid.srp;

import edu.agh.wfiis.solid.srp.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.model.Message;
import edu.agh.wfiis.solid.srp.model.raml.Constraints;
import edu.agh.wfiis.solid.srp.model.raml.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpRestRequest {

    private Message requestMessage;

    HttpRestRequest(Message requestMessage) {
        this.requestMessage = requestMessage;
    }

    public void validate(Constraints validationConstraints) throws InvalidHeaderException {
        validateHeaders(validationConstraints);
    }

    private void validateHeaders(Constraints validationConstraints) throws InvalidHeaderException {
        Map<String, Header> expectedHeaders = validationConstraints.getHeaders();
        Set<String> expectedHeadersKeySet = expectedHeaders.keySet();
        Map<String, String> incomingHeaders = getIncomingHeaders(requestMessage);

        for (String expectedKey : expectedHeadersKeySet) {
            Header expectedHeader = expectedHeaders.get(expectedKey);
            String actualHeaderValue = incomingHeaders.get(expectedKey);

            checkIfRequiredHeaderValueSpecified(expectedKey, expectedHeader, actualHeaderValue);
            checkIfHeaderDefaultValueSpecified(requestMessage, expectedKey, expectedHeader, actualHeaderValue);
            checkIfHeaderValueMatchesPattern(expectedHeader, actualHeaderValue);
        }
    }

    private void checkIfHeaderValueMatchesPattern(Header expectedHeader, String actualHeaderValue) throws InvalidHeaderException {
        if ((actualHeaderValue != null) && (!expectedHeader.isMatchToPattern(actualHeaderValue))) {
            String msg = String.format("Invalid value '%s' for header %s.", actualHeaderValue, expectedHeader);
            throw new InvalidHeaderException(msg);
        }
    }

    private void checkIfHeaderDefaultValueSpecified(Message requestMessage, String expectedKey, Header expected, String actual) {
        if ((actual == null) && (expected.getDefaultValue() != null)) {
            setHeader(requestMessage, expectedKey, expected.getDefaultValue());
        }
        // what's about no actual value && no default one? Would it be optional?
    }

    private void checkIfRequiredHeaderValueSpecified(String expectedKey, Header expected, String actual) throws InvalidHeaderException {
        if ((actual == null) && (expected.isRequired())) {
            throw new InvalidHeaderException("Required header " + expectedKey + " not specified");
        }
    }

    private void setHeader(Message requestMessage, String key, String value) {
        requestMessage.setInboundProperty(key, value);
//          was:
//        if (requestMessage.getInboundProperty("http.headers") != null) {
//            ((Map) requestMessage.getInboundProperty("http.headers")).put(key, value);
//        }
    }

    private Map<String, String> getIncomingHeaders(Message message) {
        Map<String, String> incomingHeaders = new HashMap<>();
        try {
            incomingHeaders = message.getInboundProperty("http.headers");
        } catch (NullPointerException ex) {
            ex.getStackTrace();
        }
        return incomingHeaders;
    }

//    public static void main(String[] args) {
//        Message message = null;
//        HttpRestRequest request = new HttpRestRequest(message);
//        Map<String, String> incomingHeaders = request.getIncomingHeaders(message);
//        System.out.println(incomingHeaders);
//
//    }
}
