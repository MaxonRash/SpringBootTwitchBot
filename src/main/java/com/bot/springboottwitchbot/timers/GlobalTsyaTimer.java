package com.bot.springboottwitchbot.timers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalTsyaTimer {
    private static final Logger log = LoggerFactory.getLogger(GlobalTsyaTimer.class);
    private static volatile long TIMER_LEFT = 0;

    public static void setTimer() {
        if (TIMER_LEFT == 0) {
            Thread countDown = new Thread() {
                @Override
                public void run() {
                    long timer = 300;
                    TIMER_LEFT = 300;
                    while (timer != 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        timer--;
                    }
                    TIMER_LEFT = 0;
                    log.debug("сейчас таймер на ться: {}", GlobalTsyaTimer.getTimerLeft());
                }
            };
            countDown.start();
        }
    }

    public static long getTimerLeft() {
        return TIMER_LEFT;
    }
}