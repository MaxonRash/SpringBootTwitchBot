package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** {@code !джошукен} — posts the joshuken ASCII. Migrated from spamMessagesCommand (both channels). */
@Component
public class JoshukenCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.startsWith("!джошукен");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("༼ つ happaDans ༽つ ─=≡Σ happaDjosh ))");
    }
}
