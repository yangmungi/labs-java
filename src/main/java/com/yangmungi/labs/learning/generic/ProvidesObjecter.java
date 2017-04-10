package com.yangmungi.labs.learning.generic;

/**
 * Created by Yangmun on 3/9/2016.
 */
public class ProvidesObjecter implements Requires<Objecter> {
    @Override
    public Objecter getDependent() {
        return new Objecter();
    }
}
