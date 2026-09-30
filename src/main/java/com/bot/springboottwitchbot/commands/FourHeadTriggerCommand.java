package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** Trigger word {@code 4руфв} → replies "4Head". Migrated from spamMessagesCommand (both channels). */
@Component
public class FourHeadTriggerCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.contains("4руфв");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("@" + event.getUser().getName() + " 4Head");
    }
}
