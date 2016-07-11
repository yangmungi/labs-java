package com.yangmungi.labs.learning;

/**
 * Created by Yangmun on 7/10/2016.
 */
public class BitShift {
    public static void main(String[] args) {
        int b = -1;
        for (int i = 0; i < 32; i++) {
            b = b << 1;
            System.out.println(b);
        }
    }
}
