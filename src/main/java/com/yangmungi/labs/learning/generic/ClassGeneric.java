package com.yangmungi.labs.learning.generic;

/**
 * Created by Yangmun on 3/9/2016.
 */
public class ClassGeneric<Dependent> {
    private final Class<Requires<Dependent>> classRequiresDependent;

    public ClassGeneric(Class<Requires<Dependent>> classRequiresDependent) {
        this.classRequiresDependent = classRequiresDependent;
    }

    public Class<Requires<Dependent>> getClassRequiresDependent() {
        return classRequiresDependent;
    }

    public static void main(String[] args) {
        final ProvidesObjecter providesObjecter = new ProvidesObjecter();
//        final Class<Requires<Objecter>> basicGeneric = (Class<Requires<Objecter>>) providesObjecter.getClass();
//        new ClassGeneric<Objecter>(basicGeneric);
    }
}
