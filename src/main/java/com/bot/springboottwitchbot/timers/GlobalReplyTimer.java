package com.bot.springboottwitchbot.timers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalReplyTimer {
    private static final Logger log = LoggerFactory.getLogger(GlobalReplyTimer.class);
    private static volatile AtomicInteger TIMER_LEFT = new AtomicInteger();

    public static void setTimer() {
        if (TIMER_LEFT.get() == 0) {
            Thread countDown = new Thread() {
                @Override
                public void run() {
//                    long timer = 300;
                    TIMER_LEFT.set(300);
                    while (TIMER_LEFT.get() != 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        TIMER_LEFT.decrementAndGet();
                    }
                    TIMER_LEFT.set(0);
                    log.debug("сейчас таймер на ответ: {}", GlobalReplyTimer.getTimerLeft());
                }
            };
            countDown.start();
        }
    }

    public static long getTimerLeft() {
        return TIMER_LEFT.get();
    }
}
