package com.bot.springboottwitchbot.commands;
import com.bot.springboottwitchbot.utilities.TwitchText;

import com.bot.springboottwitchbot.SpringBootTwitchBotApplication;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@code !reboot} (moderators only) — hot-reloads the Spring context via
 * {@link SpringBootTwitchBotApplication#restart()}. Migrated verbatim from the identical
 * {@code rebootBotContext} methods in both handlers.
 */
@Component
public class RebootCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(RebootCommand.class);

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        // strip the invisible Unicode tag char some Twitch clients append, then match the prefix
        String message = event.getMessage().toLowerCase().replace(TwitchText.INVISIBLE_TAG, "");
        return ctx.isModerator(event.getUser().getName()) && message.startsWith("!reboot");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        log.info("rebooting...");
        ctx.send("@" + event.getUser().getName() + " rebooting...");
        SpringBootTwitchBotApplication.restart();
    }
}
