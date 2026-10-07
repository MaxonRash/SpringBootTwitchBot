package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.ChatLog;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Always-on: logs every incoming chat message at DEBUG. Migrated from the {@code printChannelMessage}
 * {@code @EventSubscriber} methods that were duplicated in {@code EventHandlerBot}/{@code EventHandlerMain}.
 * First command migrated to the {@link ChatEventDispatcher} — proves the passive ("matches always") path.
 */
@Component
public class MessageLoggingCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(MessageLoggingCommand.class);

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return true;
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        log.debug("[{}]{{dispatcher}}[{}] {}: {}", event.getChannel().getName(), event.getPermissions(),
                event.getUser().getName(), event.getMessage());
        ChatLog.in(event.getChannel().getName(), event.getUser().getName(), event.getMessage());
    }
}
