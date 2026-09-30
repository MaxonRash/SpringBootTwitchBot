package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** Trigger word {@code амиго} → replies with the Amigo browser copypasta. Migrated from spamMessagesCommand (both channels). */
@Component
public class AmigoTriggerCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.contains("амиго");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("@" + event.getUser().getName()
                + " Вот не надо на Амиго гнать, вполне обычный браузер. Репутацию сломал потому что вместе с вирусами ставился. С офф сайта он нормальный");
    }
}
