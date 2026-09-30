package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** {@code !хадукен} — posts the hadouken ASCII. Migrated from spamMessagesCommand (both channels). */
@Component
public class HadoukenCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.startsWith("!хадукен");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("༼ つಠ益ಠ༽つ ─=≡ΣO))");
    }
}
