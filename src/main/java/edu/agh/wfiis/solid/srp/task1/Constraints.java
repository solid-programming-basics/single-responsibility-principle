package edu.agh.wfiis.solid.srp.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Constraints {

    private final List<Constraint> headersConstraints = new ArrayList<>();

    List<Constraint> getHeaderConstraints() {
        return Collections.unmodifiableList(headersConstraints);
    }

    void add(List<Constraint> constraints) {
        headersConstraints.addAll(constraints);
    }
}