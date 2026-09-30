package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * {@code !mods} — posts the channel's current Twitch moderator list.
 * <p>
 * Migrated from {@code getModeratorsTest} (test) and {@code getModeratorsHappa} (main), which had
 * drifted: it was open to everyone on the test channel but owner-only on main. That per-channel
 * behavior is preserved via {@link ChannelContext#isSandbox()} — the single command class now
 * covers both channels instead of two copies.
 */
@Component
public class ModsCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return event.getMessage().contains("!mods");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        if (ctx.isSandbox() || ctx.isOwner(event.getUser().getName())) {
            ctx.send(ctx.getModerators().toString());
        }
    }
}
