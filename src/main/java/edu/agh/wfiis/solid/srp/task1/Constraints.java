package edu.agh.wfiis.solid.srp.task1;

import java.util.ArrayList;
import java.util.List;

public class Constraints {

    private final List<Constraint> headerConstraints = new ArrayList<>();

    List<Constraint> getHeaderConstraints() {
        return headerConstraints;
    }

    void add(List<Constraint> constraints) {
        headerConstraints.addAll(constraints);
    }
}
