package com.bot.springboottwitchbot.commands;

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

    /** Sends a chat message to this channel via the bot account (logged by the builder util). */
    void send(String message);
}
