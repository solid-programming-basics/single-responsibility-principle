package edu.agh.wfiis.solid.srp.task1

import spock.lang.Specification

class HttpRestRequestRegressionTest extends Specification {

    def request = new HttpRestRequest()
    MuleMessage message = Mock()
    Constraints constraints = new Constraints()

    def "should return the original message when no constraints are defined"() {
        expect:
        request.process(message, constraints).is(message)
    }

    def "should skip validation when an optional header is absent"() {
        given:
        Constraint constraint = Mock()
        constraint.getHeaderName() >> "X-Optional"
        constraints.add([constraint])

        when:
        def result = request.process(message, constraints)

        then:
        result.is(message)
        0 * constraint.validate(_)
        0 * message.setHeader(_, _)
    }

    def "should preserve an existing header even when a default is defined"() {
        given:
        constraints.add([new Constraint(true, null, "fallback", "X-Header")])
        message.getHeader("X-Header") >> "existing"

        when:
        def result = request.process(message, constraints)

        then:
        result.is(message)
        0 * message.setHeader(_, _)
    }

    def "should apply a default to an optional header and then validate it"() {
        given:
        Constraint constraint = Mock()
        constraint.getHeaderName() >> "X-Optional"
        constraint.getDefaultValue() >> "invalid-default"
        constraints.add([constraint])

        when:
        request.process(message, constraints)

        then:
        1 * message.setHeader("X-Optional", "invalid-default")

        then:
        1 * constraint.validate("invalid-default") >> false
        def ex = thrown(InvalidHeaderException)
        ex.message == "Invalid value format for header X-Optional."
    }

    def "should stop processing further headers after a validation failure"() {
        given:
        constraints.add([
                new Constraint(true, null, null, "X-Required"),
                new Constraint(false, null, "fallback", "X-Later")
        ])

        when:
        request.process(message, constraints)

        then:
        def ex = thrown(InvalidHeaderException)
        ex.message == "Required header X-Required not specified"
        0 * message.getHeader("X-Later")
        0 * message.setHeader(_, _)
    }
}
