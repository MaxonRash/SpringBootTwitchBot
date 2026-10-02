package com.bot.springboottwitchbot.commands;
import com.bot.springboottwitchbot.utilities.TwitchText;

import com.bot.springboottwitchbot.gpt.TsyaMode;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * {@code !tsya on|off} (moderators only) — toggles the per-channel тся/ться nag ({@link TsyaMistakeCommand}).
 * Migrated from {@code changeTsyaMode}; the on/off state now lives per-channel in {@link ChannelContext}.
 * Drift preserved via {@link ChannelContext#isSandbox()}: main strips the invisible tag char before
 * parsing the argument, the test channel did not.
 */
@Component
public class TsyaToggleCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (event.getUser().getName().equals(ctx.getBotAccountName())) {
            return false;
        }
        return ctx.isModerator(event.getUser().getName())
                && event.getMessage().toLowerCase().startsWith("!tsya");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase();
        if (!ctx.isSandbox()) {
            newMessage = newMessage.replace(TwitchText.INVISIBLE_TAG, "");
        }
        String user = event.getUser().getName();
        String[] splitMessage = newMessage.split(" ");
        if (splitMessage.length > 1) {
            String mode = splitMessage[1];
            if (mode.toUpperCase().equals(TsyaMode.ON.getName())) {
                ctx.setTsyaMode(TsyaMode.ON);
                ctx.send("@" + user + " ться триггер теперь - " + ctx.getTsyaMode().getName());
            } else if (mode.toUpperCase().equals(TsyaMode.OFF.getName())) {
                ctx.setTsyaMode(TsyaMode.OFF);
                ctx.send("@" + user + " ться триггер теперь - " + ctx.getTsyaMode().getName());
            } else {
                ctx.send("@" + user + " нужно указать ON или OFF, сейчас - " + ctx.getTsyaMode().getName());
            }
        } else {
            ctx.send("@" + user + " нужно указать ON или OFF, сейчас - " + ctx.getTsyaMode().getName());
        }
    }
}
