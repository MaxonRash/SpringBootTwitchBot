package com.bot.springboottwitchbot.timers;

public class GlobalTsyaTimer {
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
                    System.out.println("сейчас таймер на ться: " + GlobalTsyaTimer.getTimerLeft());
                }
            };
            countDown.start();
        }
    }

    public static long getTimerLeft() {
        return TIMER_LEFT;
    }
}