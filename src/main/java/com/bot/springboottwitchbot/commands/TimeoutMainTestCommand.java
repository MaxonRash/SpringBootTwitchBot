package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Owner-only, main-only debug probe {@code !time1outnewtest}: issues a 10s timeout (against the test
 * channel's broadcaster id) via the main-channel moderation endpoint to verify the request path.
 * Migrated from {@code timeoutMainTest}; gated on {@code !isSandbox()}.
 */
@Component
public class TimeoutMainTestCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(TimeoutMainTestCommand.class);

    private final BotBuilderUtil botBuilderUtil;

    @Autowired
    public TimeoutMainTestCommand(BotBuilderUtil botBuilderUtil) {
        this.botBuilderUtil = botBuilderUtil;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return !ctx.isSandbox()
                && ctx.isOwner(event.getUser().getName())
                && event.getMessage().contains("!time1outnewtest");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        try {
            UtilityCommandsMainChannel.timeoutUser(botBuilderUtil.getTestChannelId(), 10, "no reason");
            ctx.send("new request sent");
        } catch (IOException e) {
            log.error("timeoutMainTest failed", e);
        }
    }
}
