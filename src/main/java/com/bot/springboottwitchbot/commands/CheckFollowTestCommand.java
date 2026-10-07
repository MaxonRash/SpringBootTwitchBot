package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * Test-only debug probe {@code !checkfollow}: reports when the caller started following, or that they
 * aren't a follower. Migrated from {@code BobFollowTest}; open (no permission check) and gated to sandbox.
 */
@Component
public class CheckFollowTestCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return ctx.isSandbox() && event.getMessage().contains("!checkfollow");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        Date date = UtilityCommandsTestChannel.getFollowingSinceDate(
                Integer.parseInt(Objects.requireNonNull(UtilityCommandsGlobal.getUserIdByName(event.getUser().getName()))));
        if (date != null) {
            ctx.send(date.toString());
        } else {
            ctx.send("TI NE FOLLOWER");
        }
    }
}
