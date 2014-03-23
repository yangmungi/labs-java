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
    
    /**
     * Represents where the original north-point of the disk is now.
     * NORTH = 0, 
     * WEST = Short.MIN_VALUE / 2,
     * SOUTH = Short.MIN_VALUE or SHORT.MAX_VALUE
     * EAST = Shot.MAX_VALUE / 2
     * 
     */
    private short diskNorthAngle;
    
    /**
     * Angular speed, represents 1 diskNorthAngle per millisecond
     */
    private int diskSpeed;

    /**
     * Effectively represents the radius (track)
     */
    private final int maxDiskHeadLocation;
    private final int maxDiskSpeed;

    public DiskEmulator(int diskSize, int maxDiskSpeed) {
        this.maxDiskHeadLocation = diskSize;
        this.maxDiskSpeed = maxDiskSpeed;
        
        this.diskNorthAngle = 0;
        this.diskHeadLocation = 0;
        this.diskSpeed = 0;
    }
       
    public int getMaxDiskHeadLocation() {
        return this.maxDiskHeadLocation;
    }
    
     /**
     * Changing disk speed takes no time at all.
     * @param diskSpeed 
     */
    public void setDiskSpeed(int diskSpeed) {
        this.diskSpeed = diskSpeed;
    }
    
    protected int getDiskSpeed() {
        return this.diskSpeed;
    }
    
    protected int getDiskHeadLocation() {
        return this.diskHeadLocation;
    }
    
    protected int getDiskNorthAngle() {
        return this.diskNorthAngle;
    }
    
    protected void setDiskNorthAngle(short diskNorthAngle) {
        this.diskNorthAngle = diskNorthAngle;
    }

    protected long setDiskHeadLocation(int location) {
        if (location < 0) {
            throw new IllegalArgumentException("offset cannot be less than zero");
        }

        if (location > this.getMaxDiskHeadLocation()) {
            throw new IndexOutOfBoundsException("offset greater than disk size");
        }

        long delay = this.diskHeadDelay(location);
        this.diskHeadLocation = location;
        
        this.applyDiskRotation(delay);
        
        return delay;
    }

    /**
     * Represents how long it should take for the disk head to move to
     * a certain position.
     * @param location
     * @return 
     */
    protected long diskHeadDelay(int location) {
        return (this.getDiskHeadLocation() - location) * 4;
    }
    
    /**
     * Due to a delay, the disk must spin, this will provide that.
     * @param delay 
     */
    protected void applyDiskRotation(long delay) {
        this.setDiskNorthAngle((short)(this.getDiskNorthAngle() 
                + this.getDiskSpeed() * delay));        
    }

    /**
     * Basically calculates how long for disk head to read
     * particular sector.
     * Disk Head must be in the correct place before this is called.
     *
     * @return
     */
    protected long seekDiskSector(int sector) {
        if (this.getDiskHeadLocation() != this.getDiskHeadLocationForSector(sector)) {
            throw new IllegalStateException(
                    "cannot seek for sector if head is not on correct track"
                );            
        }
        
        // Figure out lowest sector on track
        // Figure out what the angle of the disk has to be
        // Calculate time based off current angle and RPM of 
        
        return 1000L;
    }
    
    /**
     * A.K.A. get Track sector is in
     * @param sector
     * @return 
     */
    protected int getDiskHeadLocationForSector(int sector) {
        return 1;        
    }

    /**
     *
     *
     * @param sector
     * @param size
     * @return block time, microseconds
     * @throws IndexOutOfBoundsException
     */
    public long read(int sector) throws IndexOutOfBoundsException {
        // Find out where the sector is
        // Strategize disk head location
        long delay = 0;
        
        delay += this.setDiskHeadAndSeekSector(sector);        
        delay += this.setDiskHeadAndSeekSector(sector + 1);        
        
        return delay;
    }
    
    private long setDiskHeadAndSeekSector(int sector) {
        return this.setDiskHeadLocation(
            this.getDiskHeadLocationForSector(sector)
        ) + this.seekDiskSector(sector);
    }
}
