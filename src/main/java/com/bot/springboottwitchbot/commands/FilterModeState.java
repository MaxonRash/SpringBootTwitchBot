package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.FilterMode;
import org.springframework.stereotype.Component;

/**
 * Shared, JVM-wide swear-filter mode. A single bean injected into both {@link FilterToggleCommand} (which
 * sets it, from either channel) and {@link FilterCommand} (which reads it), so one {@code !filter ai|old}
 * toggle affects every channel at once — unlike the other per-channel toggles (tsya/botreply) that live on
 * {@link ChannelContext}. {@code volatile} because it is read/written from the different channels' event
 * threads. Defaults to {@link FilterMode#OLD} (the previous main-channel default).
 */
@Component
public class FilterModeState {

    private volatile FilterMode mode = FilterMode.OLD;

    public FilterMode getMode() {
        return mode;
    }

    public void setMode(FilterMode mode) {
        this.mode = mode;
    }
}
