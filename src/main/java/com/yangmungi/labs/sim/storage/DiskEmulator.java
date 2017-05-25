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
    private int headTrack;

    // Some approximations
    // 7200/60/1000*2^16 = 7864.32
    // diskAngles / milli for 7200 RPM
    public static final int RPM_7200 = 7865;

    /**
     * Represents where the original north-point of the disk is now.
     *
     * NORTH = 0 
     * WEST = Short.MIN_VALUE / 2 
     * SOUTH = Short.MIN_VALUE or SHORT.MAX_VALUE 
     * EAST = Shot.MAX_VALUE / 2
     */
    private short angle;

    /**
     * Angular speed, represents 1 angle per millisecond
     */
    private int speed;

    /**
     * Effectively represents the radius (track)
     */
    private final int maxTrack;
    private final int maxSpeed;

    public DiskEmulator(int diskSize, int maxDiskSpeed) {
        this.maxTrack = diskSize;
        this.maxSpeed = maxDiskSpeed;

        this.angle = 0;
        this.headTrack = 0;
        this.speed = 0;
    }

    /**
     * Changing disk speed takes no time at all.
     *
     * @param diskSpeed
     */
    public void setDiskSpeed(int diskSpeed) {
        if (diskSpeed > this.maxSpeed) {
            throw new IllegalArgumentException("disk speed > max speed");
        }

        this.speed = diskSpeed;
    }

    public void setMaxDiskSpeed() {
        this.setDiskSpeed(this.maxSpeed);
    }

    protected int getDiskSpeed() {
        return this.speed;
    }

    protected int getHeadTrack() {
        return this.headTrack;
    }

    protected short getAngle() {
        return this.angle;
    }

    protected void setAngle(short angle) {
        this.angle = angle;
    }

    protected long setHeadTrack(int location) {
        if (location < 0) {
            throw new IllegalArgumentException("offset cannot be less than zero");
        }

        if (location > this.maxTrack) {
            throw new IndexOutOfBoundsException("offset greater than disk getStatistic");
        }

        long delay = this.diskHeadDelay(location);
        this.headTrack = location;

        this.applyDiskRotation(delay);

        return delay;
    }

    /**
     * Represents how long it should take for the disk head to move to a certain
     * position.
     *
     * @param location
     * @return
     */
    protected long diskHeadDelay(int location) {
        return (this.getHeadTrack() - location) * 4;
    }

    /**
     * Due to a delay, the disk must spin, this will provide that.
     *
     * @param delay
     */
    protected void applyDiskRotation(long delay) {
        this.setAngle((short) (this.getAngle()
                + this.getDiskSpeed() * delay));
    }

    /**
     * Basically calculates how long for disk head to read particular sector.
     * Disk Head must be in the correct place before this is called.
     *
     * @return
     */
    protected long seekDiskSector(int sector) {
        if (this.getHeadTrack() != this.getTrackForSector(sector)) {
            throw new IllegalStateException(
                    "cannot seek for sector if head is not on correct track"
            );
        }

        // Figure out lowest sector on track
        int lowestSector = this.getLowestSector();

        // Figure out what the angle of the disk has to be
        short diskAngle = this.getAngle();
        int trackSectorOffset = sector - lowestSector;

        // Calculate time based off current angle and RPM of 
        return 1000L;
    }

    /**
     * Gets the lowest available sector on our current track.
     *
     * @return lowest sector available on current track
     */
    protected int getLowestSector() {
        int sectors = 0;
        int track = this.getHeadTrack();

        for (int trackI = 0; trackI < track; trackI++) {
            sectors += this.getSectorsForTrack(trackI);
        }

        return sectors;
    }

    /**
     * A.K.A. get Track sector is in
     *
     * @param sector
     * @return track for given sector
     */
    protected int getTrackForSector(int sector) {
        int sectors = 0;
        int track = 0;
        
        while (sectors < sector) {
            sectors += this.getSectorsForTrack(track);
            track++;
        }
        track -= 1;

        return track;
    }
    
    protected int getSectorsForTrack(int track) {
        return 8 + track;
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

        delay += this.setHeadTrackAndSeekSector(sector);
        delay += this.setHeadTrackAndSeekSector(sector + 1);

        return delay;
    }

    private long setHeadTrackAndSeekSector(int sector) {
        return this.setHeadTrack(
                this.getTrackForSector(sector)
        ) + this.seekDiskSector(sector);
    }
}
