package com.bot.springboottwitchbot.commands;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * One instance per channel, registered as a single Twitch4J listener (replacing the ~25
 * {@code @EventSubscriber} methods on the god-class handlers). For each chat message it runs
 * <em>every</em> command whose {@link ChatCommand#matches} returns true — not "first match wins" —
 * because several behaviors legitimately fire on the same message (e.g. the always-on message log
 * and a prefix command). A failure in one command is logged and does not stop the others.
 * <p>
 * Not a Spring {@code @Component}: it is constructed per channel with that channel's
 * {@link ChannelContext} (see {@code TestChannel}/{@code MainChannel}) and the shared, Spring-collected
 * list of all {@link ChatCommand} beans.
 */
public class ChatEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(ChatEventDispatcher.class);

    private final List<ChatCommand> commands;
    private final ChannelContext context;

    public ChatEventDispatcher(List<ChatCommand> commands, ChannelContext context) {
        this.commands = commands;
        this.context = context;
    }

    @EventSubscriber
    public void onChannelMessage(ChannelMessageEvent event) {
        for (ChatCommand command : commands) {
            try {
                if (command.matches(event, context)) {
                    command.execute(event, context);
                }
            } catch (Exception e) {
                log.error("Command {} failed on message [{}]", command.getClass().getSimpleName(), event.getMessage(), e);
            }
        }
    }
}
