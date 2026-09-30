package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.TsyaMode;
import com.bot.springboottwitchbot.gpt.openai.GPT4o;
import com.bot.springboottwitchbot.timers.GlobalTsyaTimer;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Always-on (when {@code !tsya on}): checks messages containing "тся"/"ться" for a grammar mistake via GPT
 * and, if found, replies with a correction — throttled by {@link GlobalTsyaTimer}. Migrated from
 * {@code tellAboutTsyaMistake}; the redundant per-channel send branches collapse to {@link ChannelContext#send}.
 * Drift preserved via {@link ChannelContext#isSandbox()}: main only runs on messages with more than one word.
 */
@Component
public class TsyaMistakeCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(TsyaMistakeCommand.class);

    private final GPT4o gpt4o;

    @Autowired
    public TsyaMistakeCommand(GPT4o gpt4o) {
        this.gpt4o = gpt4o;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (event.getUser().getName().equals(ctx.getBotAccountName())) {
            return false;
        }
        if (ctx.getTsyaMode() != TsyaMode.ON || GlobalTsyaTimer.getTimerLeft() != 0) {
            return false;
        }
        String newMessage = event.getMessage().toLowerCase().replace("󠀀", "");
        if (!ctx.isSandbox() && newMessage.split(" ").length <= 1) {
            return false;
        }
        String trimmed = newMessage.replace(" ", "");
        return trimmed.contains("тся") || trimmed.contains("ться");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase().replace("󠀀", "");
        boolean hasTsyaMistakes = gpt4o.isTextContainingTsyaMistake(newMessage);
        log.debug("ошибки с ться: {}", hasTsyaMistakes);
        if (hasTsyaMistakes) {
            String textAboutTsyaMistakes = gpt4o.ResponseForTextContainingTsyaMistake(newMessage);
            ctx.send("@" + event.getUser().getName() + " " + textAboutTsyaMistakes);
            GlobalTsyaTimer.setTimer();
        }
    }
}
