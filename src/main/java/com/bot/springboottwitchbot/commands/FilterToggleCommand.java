package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.FilterMode;
import com.bot.springboottwitchbot.utilities.TwitchText;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@code !filter ai|old} (moderators only) — switches the swear filter between the GPT-backed "AI" mode
 * and the regex "OLD" mode (see {@link FilterCommand}). Migrated from {@code changeFilterMode}. The mode is
 * now shared across all channels via {@link FilterModeState}, so the toggle works from either channel and
 * affects both (originally it was main-only).
 */
@Component
public class FilterToggleCommand implements ChatCommand {

    private final FilterModeState filterModeState;

    @Autowired
    public FilterToggleCommand(FilterModeState filterModeState) {
        this.filterModeState = filterModeState;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase().replace(TwitchText.INVISIBLE_TAG, "");
        return ctx.isModerator(event.getUser().getName()) && newMessage.startsWith("!filter");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase().replace(TwitchText.INVISIBLE_TAG, "");
        String user = event.getUser().getName();
        String[] splitMessage = newMessage.split(" ");
        if (splitMessage.length > 1) {
            String mode = splitMessage[1];
            if (mode.toUpperCase().equals(FilterMode.AI.getName())) {
                filterModeState.setMode(FilterMode.AI);
                ctx.send("@" + user + " мат фильтр теперь - " + filterModeState.getMode().getName());
            } else if (mode.toUpperCase().equals(FilterMode.OLD.getName())) {
                filterModeState.setMode(FilterMode.OLD);
                ctx.send("@" + user + " мат фильтр теперь - " + filterModeState.getMode().getName());
            } else {
                ctx.send("@" + user + " нужно указать способ фильтрации (ai или old), сейчас - " + filterModeState.getMode().getName());
            }
        } else {
            ctx.send("@" + user + " нужно указать способ фильтрации (ai или old), сейчас - " + filterModeState.getMode().getName());
        }
    }
}
