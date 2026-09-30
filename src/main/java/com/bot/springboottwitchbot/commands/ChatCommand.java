package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

/**
 * A single chat command or always-on behavior for a channel.
 * <p>
 * Implementations are Spring {@code @Component}s. The per-channel {@link ChatEventDispatcher}
 * runs {@link #execute} for every command whose {@link #matches} returns true for a given
 * message — so passive behaviors (message logging, swear filters, tsya-nag) and prefix commands
 * (e.g. {@code !mods}) coexist, exactly as the many {@code @EventSubscriber} methods did before.
 * <p>
 * The {@link ChannelContext} tells the command which channel it is acting on (name/id, permission
 * checks, how to send a reply), so one command class works for both the test and main channels
 * instead of being copy-pasted between {@code EventHandlerBot} and {@code EventHandlerMain}.
 */
public interface ChatCommand {

    boolean matches(ChannelMessageEvent event, ChannelContext ctx);

    void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception;
}
