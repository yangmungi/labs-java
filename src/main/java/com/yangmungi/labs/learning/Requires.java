package com.yangmungi.labs.learning;

/**
 * Created by Yangmun on 3/9/2016.
 */
public interface Requires<Dependent> {
    Dependent getDependent();
}
