package edu.agh.wfiis.solid.srp.task1;

import java.util.ArrayList;
import java.util.List;

public class Constraints {

    private List<Constraint> headersConstraints = new ArrayList<>();

    List<Constraint> getHeaderConstraints() {
        return headersConstraints;
    }

    void add(List<Constraint> constraints) {
        headersConstraints.addAll(constraints);
    }
}