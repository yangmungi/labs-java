package com.yangmungi.labs;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple Permuter.
 */
public class PermuterTest
        extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PermuterTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PermuterTest.class);
    }

    /**
     * Rigorous Test :-)
     */
    public void testApp() {
        int targetSize = 8;
        ArrayList<Integer> source = new ArrayList<Integer>(targetSize);
        for (int i = 1; i <= targetSize; i++) {
            source.add(i);
        }
        
        int testedSize = 1;
        for (int i = 2; i <= targetSize; i++) {
            testedSize *= i;
        }
        
        Permuter p = new Permuter(source);
        
        p.setCollection(new ArrayList());
        
        p.execute();
        
        Collection result = p.getCollection();
        
        assertEquals(testedSize, result.size());
        
        //System.out.println(result);
    }
}
