package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * Test-only debug probe {@code !vip <user>}: VIPs the named user on the test channel. Migrated from
 * {@code vipUser}; open (no permission check) and gated to the sandbox via {@code isSandbox()}.
 */
@Component
public class VipUserTestCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return ctx.isSandbox() && event.getMessage().contains("!vip");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        UtilityCommandsTestChannel.vipUser(event.getMessage().split(" ")[1]);
    }
}
