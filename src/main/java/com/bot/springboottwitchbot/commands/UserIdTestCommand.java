package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * Test-only debug probe {@code !test}: echoes the resolved user id for "maximuz666" back to chat.
 * Migrated from {@code getUserIdTest}; open (no permission check) and gated to the sandbox via
 * {@code isSandbox()} since it only existed on the test handler.
 */
@Component
public class UserIdTestCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return ctx.isSandbox() && event.getMessage().contains("!test");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        ctx.send("new bot: " + UtilityCommandsGlobal.getUserIdByName("maximuz666"));
    }
}
