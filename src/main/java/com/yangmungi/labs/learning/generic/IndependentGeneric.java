package com.yangmungi.labs.learning.generic;

/**
 * Created by Yangmun on 3/9/2016.
 */
public class IndependentGeneric<Dependent> {
    private final Provides<Requires<Dependent>> providesRequiresDependent;

    public IndependentGeneric(Provides<Requires<Dependent>> providesRequires) {
        this.providesRequiresDependent = providesRequires;
    }

    public Provides<Requires<Dependent>> getProvidesRequiresDependent() {
        return providesRequiresDependent;
    }

    public static void main(String[] args) {
        final ProvidesObjecter providesObjecter = new ProvidesObjecter();
        final Provides<Requires<Objecter>> provides = new Provides<Requires<Objecter>>(providesObjecter);
        new IndependentGeneric<Objecter>(provides);
    }
}
