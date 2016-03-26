package com.yangmungi.labs.learning;

/**
 * Created by Yangmun on 3/9/2016.
 */
public class DependentGeneric<Dependent, ExtendsRequires extends Requires<Dependent>> {
    private final ExtendsRequires extendsRequires;

    public DependentGeneric(ExtendsRequires extendsRequires) {
        this.extendsRequires = extendsRequires;
    }

    public ExtendsRequires getExtendsRequires() {
        return extendsRequires;
    }

    public static void main(String[] args) {
        final ProvidesObjecter extendsReqDep = new ProvidesObjecter();
        new DependentGeneric<Objecter, ProvidesObjecter>(extendsReqDep);
    }
}