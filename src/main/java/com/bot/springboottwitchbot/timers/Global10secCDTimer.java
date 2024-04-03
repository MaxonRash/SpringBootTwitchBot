package com.bot.springboottwitchbot.timers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Cooldown for spam (paste) messages
 */
public final class Global10secCDTimer {
    private static Timer global10secTimer = null;

    private Global10secCDTimer(Timer global10secTimer) {
        Global10secCDTimer.global10secTimer = global10secTimer;
    }

    public static Timer getGlobal10secTimer() {
        return global10secTimer;
    }

    public static void setGlobal10secTimer() {
        Global10secCDTimer.global10secTimer = new Timer("global10secTimer");
        Global10secCDTimer.global10secTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Global10secCDTimer.global10secTimer = null;
            }
        }, 10*1000L);
    }
}
