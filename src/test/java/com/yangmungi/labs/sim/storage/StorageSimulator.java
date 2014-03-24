package com.yangmungi.labs.sim.storage;

/**
 * Hello world!
 *
 */
public class StorageSimulator {
    public static void main(String[] args) {
        DiskEmulator d = new DiskEmulator(1200, DiskEmulator.RPM_7200);
        // 7200/60/1000*2^16 = 7864.32
        // diskAngles / milli for 7200 RPM
        d.setMaxDiskSpeed();
    }
}