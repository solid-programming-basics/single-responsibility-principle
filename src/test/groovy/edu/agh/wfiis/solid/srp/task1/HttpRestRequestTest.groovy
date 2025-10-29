package edu.agh.wfiis.solid.srp.task1

import spock.lang.Specification

class HttpRestRequestTest extends Specification {

    static final String HEADER_NAME = "X-Header"

    HttpRestRequest underTest

    MuleMessage muleMessage = Mock()
    Constraints constraints = Mock()
    Constraint constraint = Mock()

    def setup() {
        underTest = new HttpRestRequest()

        constraint.getHeaderName() >> HEADER_NAME
        constraint.validate("defaultValue") >> true
        constraints.getHeaderConstraints() >> [constraint]
    }

    def "should process headers when header is valid"() {
        given: "header with valid value is set"
        muleMessage.getHeader(HEADER_NAME) >> "validValue"
        constraint.validate("validValue") >> true

        when: "request is processed"
        def result = underTest.process(muleMessage, constraints)

        then: "operation completes successfully"
    }

    def "should set default value when required header is missing"() {
        given: "required header is missing in request"
        constraint.isHeaderRequired() >> true
        muleMessage.getHeader(HEADER_NAME) >> null

        and: "default value for this header is defined"
        constraint.getDefaultValue() >> "defaultValue"

        when: "request is processed"
        def result = underTest.process(muleMessage, constraints)

        then: "header is set to default value"
        1 * muleMessage.setHeader(HEADER_NAME, "defaultValue")
    }

    def "should throw exception when required header is missing and no default value is provided"() {
        given: "required header is missing in request"
        constraint.isHeaderRequired() >> true
        muleMessage.getHeader(HEADER_NAME) >> null

        and: "default value for this header is not defined"
        constraint.getDefaultValue() >> null

        when: "request is processed"
        underTest.process(muleMessage, constraints)

        then: "error is raised"
        def ex = thrown(InvalidHeaderException)
        ex.message == "Required header $HEADER_NAME not specified"
    }

    def "should throw exception when header value is invalid"() {
        given: "header with invalid value is set"
        muleMessage.getHeader(HEADER_NAME) >> "invalidValue"
        constraint.validate("invalidValue") >> false

        when: "request is processed"
        underTest.process(muleMessage, constraints)

        then: "error is raised"
        def ex = thrown(InvalidHeaderException)
        ex.message == "Invalid value format for header $HEADER_NAME."
    }
}
