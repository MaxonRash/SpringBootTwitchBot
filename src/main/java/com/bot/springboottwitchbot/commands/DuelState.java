package com.bot.springboottwitchbot.commands;

/**
 * Per-channel pending-duel state: who challenged whom while a {@code !duel} invitation is open.
 * Re-homed from the {@code firstDuelName}/{@code secondDuelName} instance fields that used to live on
 * each event handler, so the single shared {@link DuelCommand} bean keeps test and main independent
 * (one {@link DuelState} instance per {@link ChannelContext}). The accept/cooldown timers themselves
 * stay JVM-wide in {@code GlobalDuelTimer}, matching the pre-refactor behavior.
 */
public class DuelState {

    /** The user who issued the challenge (was {@code firstDuelName}). */
    private String challenger;
    /** The user being challenged (was {@code secondDuelName}). */
    private String target;

    public String getChallenger() {
        return challenger;
    }

    public void setChallenger(String challenger) {
        this.challenger = challenger;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void clear() {
        this.challenger = null;
        this.target = null;
    }
}
