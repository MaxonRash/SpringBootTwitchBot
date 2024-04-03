package com.bot.springboottwitchbot.timers;

import java.util.Timer;

public final class GlobalDuelTimer {
    public static Timer duelTimerToAccept = null;
    public static Timer duelCooldownTimer = null;
    public static long duelCoolDownTimerLeft = 0;

    /** the way to create timer with TimerLeft feature:
     * GlobalDuelTimer.duelCooldownTimer = new Timer("duelCoolDownTimer");
     * //TODO change delay to
     * long delay = 30 * 1000L;
     * GlobalDuelTimer.duelCooldownTimer.schedule(new TimerTask() {
     *      @Override
     *      public void run() {
     *          GlobalDuelTimer.duelCooldownTimer = null;
     *      }
     * }, delay); // delay of duel cooldown
     * GlobalDuelTimer.setDuelCoolDownTimerLeft(delay / 1000);
     */

    /** the way to create timer for duel AcceptTimer
     * TimerTask timerTask = new TimerTask() {
     *      @Override
     *      public void run() {
     *          twitchClient.getChat().sendMessage(event.getChannel().getName(), "@" + finalSecondDuelName + " намочил штанишки 4Head");
     *          GlobalDuelTimer.duelTimerToAccept = null;
     *      }
     * };
     * if (GlobalDuelTimer.duelTimerToAccept == null) {
     *      GlobalDuelTimer.duelTimerToAccept = new Timer("duelTimerToAccept");
     *      //TODO change delay to
     *      long delay = 5 * 1000L; // delay for "namochil shtanishki"
     *      GlobalDuelTimer.duelTimerToAccept.schedule(timerTask, delay);
     * }
     */
    public static void setDuelCoolDownTimerLeft(long duelCoolDownTimerLeft) {
        GlobalDuelTimer.duelCoolDownTimerLeft = duelCoolDownTimerLeft;
        final long duelTimerCoolDownTimerLeftFinal = duelCoolDownTimerLeft;
        Thread duelCdTimerLeftThread = new Thread() {
            @Override
            public void run() {
                for (long duelCoolDownTimerLeft = duelTimerCoolDownTimerLeftFinal ; duelCoolDownTimerLeft >=0; duelCoolDownTimerLeft-- ){
                    GlobalDuelTimer.duelCoolDownTimerLeft = duelCoolDownTimerLeft;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        duelCdTimerLeftThread.start();
    }
}
