package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.GptBotMode;
import com.bot.springboottwitchbot.gpt.TsyaMode;

import java.io.IOException;
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

    /** Times out a user (by login) for {@code seconds} with a reason, via this channel's moderation endpoint. */
    void timeoutByName(String login, int seconds, String reason) throws IOException;

    /** Per-channel тся/ться nag toggle state (independent between test and main). */
    TsyaMode getTsyaMode();

    void setTsyaMode(TsyaMode mode);

    /** Per-channel "bot replies to @&lt;bot&gt;" toggle state (independent between test and main). */
    GptBotMode getGptBotMode();

    void setGptBotMode(GptBotMode mode);

    /** Per-channel pending-duel state (independent between test and main). */
    DuelState getDuelState();

    /** Sends a chat message to this channel via the bot account (logged by the builder util). */
    void send(String message);
}
