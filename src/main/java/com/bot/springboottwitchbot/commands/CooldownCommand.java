package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

/**
 * Base for public viewer commands gated by the shared ~10s global cooldown
 * ({@link Global10secCDTimer}, a single JVM-wide timer shared across all channels — preserved as-is).
 * <p>
 * Subclasses declare their trigger and action; this base handles the cooldown gate ({@link #matches})
 * and starts the cooldown after a successful run ({@link #execute}), matching the original
 * {@code spamMessagesCommand} blocks that did {@code getGlobal10secTimer() == null} then
 * {@code setGlobal10secTimer()}.
 */
public abstract class CooldownCommand implements ChatCommand {

    /** True if this command should fire for the given already-lowercased message. */
    protected abstract boolean triggers(String lowerMessage);

    /** The command's action (send a message, etc.). */
    protected abstract void run(ChannelMessageEvent event, ChannelContext ctx) throws Exception;

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return Global10secCDTimer.getGlobal10secTimer() == null
                && triggers(event.getMessage().toLowerCase());
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        run(event, ctx);
        Global10secCDTimer.setGlobal10secTimer();
    }
}
