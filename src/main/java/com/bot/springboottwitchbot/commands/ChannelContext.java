package com.bot.springboottwitchbot.commands;

import java.util.List;

/**
 * Per-channel view handed to every {@link ChatCommand}: the channel's identity, permission checks,
 * and how to reply. One implementation per channel ({@code TestChannelContext}, {@code MainChannelContext})
 * is what lets a single command class serve both channels instead of being duplicated per handler.
 */
public interface ChannelContext {

    /** The Twitch channel (login) this context sends messages to. */
    String getChannelName();

    /** The Twitch broadcaster id for this channel. */
    String getChannelId();

    boolean isOwner(String username);

    boolean isModerator(String username);

    String getBotAccountName();

    /**
     * True on sandbox channels (test), where utility/admin commands like {@code !mods} are open to
     * everyone; false on production channels (main), where they require owner/mod. Captures the
     * pre-refactor per-channel behavior so one command class can preserve both.
     */
    boolean isSandbox();

    /** Current Twitch moderator logins for this channel (Helix call). */
    List<String> getModerators();

    /** Sends a chat message to this channel via the bot account (logged by the builder util). */
    void send(String message);
}
