package com.bot.springboottwitchbot.timers;

import java.util.Timer;

public class GlobalKillTimer {
    public static Timer killTimer = null;
    public static Timer killCooldownTimer = null;
    public static long killCoolDownTimerLeft = 0;
    private static Thread killCdTimerLeftThread = null; // separate Thread for interruption feature (like reset)

    /** the way to create timer with TimerLeft feature:
     * GlobalKillTimer.killCooldownTimer = new Timer("killCoolDownTimer");
     * //TODO change delay to
     * long delay = 30 * 1000L;
     * GlobalKillTimer.killCooldownTimer.schedule(new TimerTask() {
     *      @Override
     *      public void run() {
     *          GlobalKillTimer.killCooldownTimer = null;
     *      }
     * }, delay); // delay of kill cooldown
     * GlobalKillTimer.setKillCoolDownTimerLeft(delay / 1000);
     */
    public static void setKillCoolDownTimerLeft(long killCoolDownTimerLeft) {
        GlobalKillTimer.killCoolDownTimerLeft = killCoolDownTimerLeft;
        final long killTimerCoolDownTimerLeftFinal = killCoolDownTimerLeft;
        GlobalKillTimer.killCdTimerLeftThread = new Thread() {
            @Override
            public void run() {
                for (long killCoolDownTimerLeft = killTimerCoolDownTimerLeftFinal ; killCoolDownTimerLeft >=0; killCoolDownTimerLeft-- ){
                    GlobalKillTimer.killCoolDownTimerLeft = killCoolDownTimerLeft;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        killCdTimerLeftThread.start();
    }

    public static void resetKill() {
        GlobalKillTimer.killCooldownTimer = null;
        GlobalKillTimer.killCdTimerLeftThread.interrupt();
        GlobalKillTimer.killCoolDownTimerLeft = 0;
    }
}
