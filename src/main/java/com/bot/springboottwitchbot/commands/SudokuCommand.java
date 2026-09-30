package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/** {@code !судоку} — links sudoku.com. Migrated from spamMessagesCommand (both channels identical). */
@Component
public class SudokuCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.startsWith("!судоку");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("@" + event.getUser().getName() + " https://sudoku.com/ 4Head");
    }
}
