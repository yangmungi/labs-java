package com.yangmungi.labs.sim.storage;

/**
 * Hello world!
 *
 */
public class StorageSimulator {
    public static void main(String[] args) {
        DiskEmulator d = new DiskEmulator(1200, DiskEmulator.RPM_7200);
        
        d.setMaxDiskSpeed();
    }
}