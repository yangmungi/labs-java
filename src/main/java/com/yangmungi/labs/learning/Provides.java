package com.yangmungi.labs.learning;

/**
 * Created by Yangmun on 3/9/2016.
 */
public class Provides<Dependent> implements Requires<Dependent> {
    private final Dependent dependent;

    public Provides(Dependent dependent) {
        this.dependent = dependent;
    }

    @Override
    public Dependent getDependent() {
        return dependent;
    }
}
