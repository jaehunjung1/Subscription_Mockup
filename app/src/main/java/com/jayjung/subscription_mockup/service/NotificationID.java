package com.jayjung.subscription_mockup.service;

import java.util.concurrent.atomic.AtomicInteger;

class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
