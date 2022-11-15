package edu.agh.wfiis.solid.srp.validator;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Header;
import edu.agh.wfiis.solid.srp.example1.validator.HeaderValidator;
import edu.agh.wfiis.solid.srp.example1.model.RestRequestHeader;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;

public class HeaderValidatorTest {

    @org.junit.Test(expected = InvalidHeaderException.class)
    public void shouldNotHaveRequiredValue() throws InvalidHeaderException {
        Header restRequestHeaderWithoutRequiredValue = givenHeaderWithoutRequiredValue();

        HeaderValidator.validate(restRequestHeaderWithoutRequiredValue);
    }

    private Header givenHeaderWithoutRequiredValue() {
        return RestRequestHeader.builder()
                .constraint(buildConstraintWhichIsRequired())
                .build();
    }

    private Constraint buildConstraintWhichIsRequired() {
        Constraint constraint = new Constraint();
        constraint.setHeaderName("Test");
        constraint.setHeaderRequired(true);
        return constraint;
    }
}
