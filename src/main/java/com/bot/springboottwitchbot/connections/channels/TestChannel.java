package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.commands.ChatCommand;
import com.bot.springboottwitchbot.commands.ChatEventDispatcher;
import com.bot.springboottwitchbot.commands.TestChannelContext;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerBot;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestChannel implements ChannelConnection {
    private final BotBuilderUtil botBuilderUtil;
    private final EventHandlerBot eventHandlerBot;
    private final List<ChatCommand> chatCommands;
    private final TestChannelContext testChannelContext;

    @Autowired
    public TestChannel(BotBuilderUtil botBuilderUtil, EventHandlerBot eventHandlerBot,
                       List<ChatCommand> chatCommands, TestChannelContext testChannelContext) {
        this.botBuilderUtil = botBuilderUtil;
        this.eventHandlerBot = eventHandlerBot;
        this.chatCommands = chatCommands;
        this.testChannelContext = testChannelContext;
    }

    @Override
    public void run() {
        botBuilderUtil.getTwitchClientBot().getChat().joinChannel(botBuilderUtil.getTestChannelName());
        // After second channel credentials, also join the second channel here (inject SecondBuilderUtil).
        EventManager eventManagerBot = botBuilderUtil.getTwitchClientBot().getEventManager();
        // Phase 5 migration: the new dispatcher runs alongside the legacy handler. Commands moved into
        // ChatCommand beans are removed from EventHandlerBot so they don't fire twice.
        eventManagerBot.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerBot);
        eventManagerBot.getEventHandler(SimpleEventHandler.class)
                .registerListener(new ChatEventDispatcher(chatCommands, testChannelContext));
    }
}
