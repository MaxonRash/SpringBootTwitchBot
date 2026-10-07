package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * {@code !emotemodetest} (owner only) — briefly flips the main channel into emote-only mode for 7s to
 * verify the toggle. Migrated from {@code emoteModeMessage}. Main-only: gated on {@code !isSandbox()}
 * because it drives {@link UtilityCommandsMainChannel} regardless of which channel it's typed in, and the
 * original only existed on the main handler. The 7s sleep runs inline, as in the original.
 */
@Component
public class EmoteModeTestCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return !ctx.isSandbox()
                && ctx.isOwner(event.getUser().getName())
                && event.getMessage().toLowerCase().contains("!emotemodetest");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        UtilityCommandsMainChannel.emoteOnlyMode(true);
        Thread.sleep(7000);
        UtilityCommandsMainChannel.emoteOnlyMode(false);
    }
}
