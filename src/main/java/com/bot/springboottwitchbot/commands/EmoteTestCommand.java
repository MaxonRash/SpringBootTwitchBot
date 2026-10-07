package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * Test-only debug probe {@code !emotetest}: turns on emote-only mode on the test channel. Migrated from
 * {@code timeoutHappaTest}; open (no permission check) and gated to the sandbox via {@code isSandbox()}.
 */
@Component
public class EmoteTestCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return ctx.isSandbox() && event.getMessage().contains("!emotetest");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        UtilityCommandsTestChannel.emoteOnlyMode(true);
    }
}
