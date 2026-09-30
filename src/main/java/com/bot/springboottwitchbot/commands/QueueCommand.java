package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** {@code !в очередь} — random "queue position" flavor message. Migrated from spamMessagesCommand (both channels). */
@Component
public class QueueCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.startsWith("!в очередь");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        String firstNick = event.getUser().getName();
        int dice = (int) (Math.random() * 1000) + 1;
        if (dice == 1) {
            ctx.send("@" + firstNick + " Ваше место в очереди... Вы следующий! PogChamp скринь! SHTO");
        } else if (dice == 1000) {
            ctx.send("@" + firstNick + " Ваше место в очереди... Вы последний! АХАХА maaaaan");
        } else {
            if (dice < 100) {
                ctx.send("@" + firstNick + " Ваше место в очереди... " + dice + ", осталось немного peepoComfy");
            } else if (dice > 900) {
                ctx.send("@" + firstNick + " Ваше место в очереди... " + dice + ", это вооон за тем челом PepePoint");
            } else {
                ctx.send("@" + firstNick + " Ваше место в очереди... " + dice + " Tssk");
            }
        }
    }
}
