package com.bot.springboottwitchbot.timers;

import java.util.Timer;

public class GlobalRouletteTimer {
    public static Timer rouletteTimerToAccept = null;
    public static Timer rouletteCooldownTimer = null;
    public static long rouletteCoolDownTimerLeft = 0;

    /** the way to create timer with TimerLeft feature:
     * GlobalRouletteTimer.rouletteCooldownTimer = new Timer("rouletteCoolDownTimer");
     * //TODO change delay to
     * long delay = 30 * 1000L;
     * GlobalRouletteTimer.rouletteCooldownTimer.schedule(new TimerTask() {
     *       @Override
     *       public void run() {
     *           GlobalRouletteTimer.rouletteCooldownTimer = null;
     *       }
     * }, delay); // delay of roulette cooldown
     * GlobalRouletteTimer.setRouletteCoolDownTimerLeft(delay / 1000);
     */

    /** the way to create timer for roulette AcceptTimer
     * TimerTask timerTask = new TimerTask() {
     *      @Override
     *      public void run() {
     *          twitchClient.getChat().sendMessage(event.getChannel().getName(), "сходка клуба любителей пострелять не состоялась FeelsBadMan");
     *          GlobalRouletteTimer.rouletteTimerToAccept = null;
     *      }
     * };
     * if (GlobalRouletteTimer.rouletteTimerToAccept == null) {
     *      GlobalRouletteTimer.rouletteTimerToAccept = new Timer("rouletteTimerToAccept");
     *      //TODO change delay to
     *      long delay = 5 * 1000L; // delay for "shodka ne sostoyalas"
     *      GlobalRouletteTimer.rouletteTimerToAccept.schedule(timerTask, delay);
     * }
     */
    public static void setRouletteCoolDownTimerLeft(long rouletteCoolDownTimerLeft) {
        GlobalRouletteTimer.rouletteCoolDownTimerLeft = rouletteCoolDownTimerLeft;
        final long rouletteTimerCoolDownTimerLeftFinal = rouletteCoolDownTimerLeft;
        Thread rouletteCdTimerLeftThread = new Thread() {
            @Override
            public void run() {
                for (long rouletteCoolDownTimerLeft = rouletteTimerCoolDownTimerLeftFinal ; rouletteCoolDownTimerLeft >=0; rouletteCoolDownTimerLeft-- ){
                    GlobalRouletteTimer.rouletteCoolDownTimerLeft = rouletteCoolDownTimerLeft;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        rouletteCdTimerLeftThread.start();
    }
}
