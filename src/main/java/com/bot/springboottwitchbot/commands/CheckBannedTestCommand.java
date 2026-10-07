package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * Test-only debug probe {@code !checkbanned}: reports whether a fixed test account is currently banned.
 * Migrated from {@code checkIfUserIsBanned}; open (no permission check) and gated to the sandbox.
 */
@Component
public class CheckBannedTestCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return ctx.isSandbox() && event.getMessage().contains("!checkbanned");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        boolean isBanned = UtilityCommandsTestChannel.isBannedUser("72903124");
        ctx.send("@" + event.getUser().getName() + " " + isBanned);
    }
}
