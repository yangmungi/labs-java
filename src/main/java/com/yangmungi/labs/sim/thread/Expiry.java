package com.yangmungi.labs.sim.thread;

import java.util.Calendar;

/**
 * Created by Yangmun on 7/6/2014.
 */
public class Expiry {
    public static long DEFAULT_EXPIRE = 1000L;

    private Calendar expireTime;
    private final Object target;

    public Expiry(Object target) {
        this(target, Calendar.getInstance());
        getExpireTime().add(Calendar.SECOND, (int) DEFAULT_EXPIRE);
    }

    public Expiry(Object target, Calendar expireTime) {
        this.target = target;
        this.expireTime = expireTime;
    }

    public Calendar getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Calendar calendar) {
        this.expireTime = calendar;
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expiry expiry = (Expiry) o;

        if (target != null ? !target.equals(expiry.target) : expiry.target != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return target != null ? target.hashCode() : 0;
    }
}
