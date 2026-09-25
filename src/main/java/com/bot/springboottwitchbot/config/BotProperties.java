package com.bot.springboottwitchbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Non-secret bot identity/config, bound from the {@code bot.*} keys in application.properties.
 * Centralizes values previously hardcoded across the event handlers (moderator logins,
 * the bot account name, and which channel to start).
 */
@Component
@ConfigurationProperties(prefix = "bot")
public class BotProperties {
    private String owner;
    private List<String> moderators = new ArrayList<>();
    private String botAccountName;
    /** Which channels to connect on startup, e.g. {@code test,main,second}. */
    private List<String> channels = new ArrayList<>();

    /** True only for the broadcaster/owner account (owner-only commands). */
    public boolean isOwner(String username) {
        return username != null && owner != null && owner.equalsIgnoreCase(username);
    }

    /** Case-insensitive membership check, matching the handlers' existing equalsIgnoreCase style. */
    public boolean isModerator(String username) {
        if (username == null) {
            return false;
        }
        return moderators.stream().anyMatch(m -> m.equalsIgnoreCase(username));
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getModerators() {
        return moderators;
    }

    public void setModerators(List<String> moderators) {
        this.moderators = moderators;
    }

    public String getBotAccountName() {
        return botAccountName;
    }

    public void setBotAccountName(String botAccountName) {
        this.botAccountName = botAccountName;
    }

    /** True if the given channel key (test/main/second) is enabled for startup. */
    public boolean isChannelEnabled(String channel) {
        return channels.stream().anyMatch(c -> c.equalsIgnoreCase(channel));
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
