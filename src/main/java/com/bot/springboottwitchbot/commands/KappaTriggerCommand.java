package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** Trigger word {@code лфзза} → replies "Kappa". Migrated from spamMessagesCommand (both channels). */
@Component
public class KappaTriggerCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.contains("лфзза");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("@" + event.getUser().getName() + " Kappa");
    }
}
