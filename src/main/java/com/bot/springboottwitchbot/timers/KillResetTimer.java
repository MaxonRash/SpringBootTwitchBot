package com.bot.springboottwitchbot.timers;

import java.util.Timer;

public class KillResetTimer {
    public static Timer killResetTimer = null;
    public static Timer killResetCooldownTimer = null;
    public static long killResetCoolDownTimerLeft = 0;
    private static Thread killResetCdTimerLeftThread = null; // separate Thread for interruption feature (like reset)

    /** the way to create timer with TimerLeft feature:
     * KillResetTimer.killResetCooldownTimer = new Timer("killResetCoolDownTimer");
     * //TODO change delay to
     * long delay = 30 * 1000L;
     * KillResetTimer.killResetCooldownTimer.schedule(new TimerTask() {
     *      @Override
     *      public void run() {
     *          KillResetTimer.killResetCooldownTimer = null;
     *      }
     * }, delay); // delay of kill reset cooldown
     * KillResetTimer.setKillResetCoolDownTimerLeft(delay / 1000);
     */
    public static void setKillResetCoolDownTimerLeft(long killResetCoolDownTimerLeft) {
        KillResetTimer.killResetCoolDownTimerLeft = killResetCoolDownTimerLeft;
        final long killResetTimerCoolDownTimerLeftFinal = killResetCoolDownTimerLeft;
        KillResetTimer.killResetCdTimerLeftThread = new Thread() {
            @Override
            public void run() {
                for (long killResetCoolDownTimerLeft = killResetTimerCoolDownTimerLeftFinal ; killResetCoolDownTimerLeft >=0; killResetCoolDownTimerLeft-- ){
                    KillResetTimer.killResetCoolDownTimerLeft = killResetCoolDownTimerLeft;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        killResetCdTimerLeftThread.start();
    }

}
