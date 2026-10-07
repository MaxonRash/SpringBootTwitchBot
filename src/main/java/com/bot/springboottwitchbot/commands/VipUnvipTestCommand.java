package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Owner-only, main-only debug probe {@code !viptest}: VIPs a fixed test account, waits 5s, then un-VIPs
 * it — to verify the VIP/un-VIP round trip. Migrated from {@code vipAndUnVipTest}; gated on
 * {@code !isSandbox()}. The 5s sleep runs inline, as in the original.
 */
@Component
public class VipUnvipTestCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(VipUnvipTestCommand.class);

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return !ctx.isSandbox()
                && ctx.isOwner(event.getUser().getName())
                && event.getMessage().contains("!viptest");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        try {
            UtilityCommandsMainChannel.vipUser("steyro");
            Thread.sleep(5000);
            UtilityCommandsMainChannel.unVipUser("steyro");
        } catch (Exception e) {
            log.error("vipAndUnVipTest failed", e);
        }
    }
}
