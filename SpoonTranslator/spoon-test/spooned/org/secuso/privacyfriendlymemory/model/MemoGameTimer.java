package org.secuso.privacyfriendlymemory.model;


public class MemoGameTimer {
    private java.util.Timer internalTimer;

    private java.util.concurrent.atomic.AtomicBoolean timerRunning = new java.util.concurrent.atomic.AtomicBoolean(false);

    private int time = 0;

    private final int WAIT_TIME = 1000;

    private void init() {
        internalTimer = new java.util.Timer();
        internalTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @java.lang.Override
            public void run() {
                if (timerRunning.get()) {
                    (time)++;
                }
            }
        }, 0, WAIT_TIME);
    }

    public void stop() {
        timerRunning.set(false);
    }

    public void start() {
        timerRunning.set(true);
        if ((internalTimer) == null) {
            init();
        }
    }

    public int getTime() {
        return time;
    }
}

