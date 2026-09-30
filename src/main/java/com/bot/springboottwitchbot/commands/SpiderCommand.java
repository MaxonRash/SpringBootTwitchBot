package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** {@code !паук} — posts the spider ASCII. Migrated from the spamMessagesCommand block (both channels). */
@Component
public class SpiderCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.startsWith("!паук");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("ВИКТОР - /\\/\\╭( ͡° ͡° ͜ʖ ͡° ͡°)╮/\\╱\\");
    }
}
