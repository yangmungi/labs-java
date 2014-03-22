/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yangmungi.labs.sim.storage;

/**
 * Represents single-platter storage mechanism.
 *
 * @author Yangmun
 */
public class DiskEmulator {
    /**
     * Represents how far from center of disk the head is.
     */
    private int diskHeadLocation;
    
    private int diskSpeed;

    private final int diskSize;
    private final int maxDiskSpeed;

    public DiskEmulator(int diskSize, int maxDiskSpeed) {
        this.diskSize = diskSize;
        this.maxDiskSpeed = maxDiskSpeed;
    }

    public int getDiskSize() {
        return this.diskSize;
    }

    protected long setDiskHeadLocation(int location) {
        if (location < 0) {
            throw new IllegalArgumentException("offset cannot be less than zero");
        }

        if (location > this.getDiskSize()) {
            throw new IndexOutOfBoundsException("offset greater than disk size");
        }

        long delay = this.diskHeadDelay(location);
        this.diskHeadLocation = location;
        return delay;
    }

    protected int getDiskHeadLocation() {
        return this.diskHeadLocation;
    }
    
    protected long diskHeadDelay(int location) {
        return (this.getDiskHeadLocation() - location) * 4;
    }

    /**
     * Should incorporate disk-radius, disk-spin-speed, block density. Should
     * include buffer/SRAM?
     *
     * @return
     */
    public long diskDelay(int offset) {
        return 1000L;
    }

    /**
     * Activates read buffer and attempts to read a sector.
     *
     * @param offset
     * @param size
     * @return
     * @throws IndexOutOfBoundsException
     */
    public long read(int offset) throws IndexOutOfBoundsException,
            IllegalArgumentException {

        //if (size )
        long delay = diskDelay(offset);
        delay += this.setDiskHeadLocation(offset);

        int endingOffset = offset + 1;
        delay += this.diskDelay(endingOffset);
        this.setDiskHeadLocation(endingOffset);

        return delay;
    }
}
