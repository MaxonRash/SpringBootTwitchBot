package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.services.UsersService;
import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Test-only debug probe {@code !follow}: back-fills winretkristin's stored "following since" date from
 * Twitch. Migrated from {@code followedSinceTest}; open (no permission check) and gated to the sandbox.
 */
@Component
public class FollowSinceTestCommand implements ChatCommand {

    private final UsersService usersService;

    @Autowired
    public FollowSinceTestCommand(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return ctx.isSandbox() && event.getMessage().contains("!follow");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        User user = usersService.findOne("winretkristin");
        Date followingSince = UtilityCommandsTestChannel.getFollowingSinceDate(137335434);
        user.setFollowingSince(followingSince);
        usersService.save(user);
    }
}
