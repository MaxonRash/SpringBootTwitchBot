package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.commands.ChatCommand;
import com.bot.springboottwitchbot.commands.ChatEventDispatcher;
import com.bot.springboottwitchbot.commands.MainChannelContext;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerMain;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainChannel implements ChannelConnection {
    private final MainBuilderUtil mainBuilderUtil;
    private final EventHandlerMain eventHandlerMain;
    private final List<ChatCommand> chatCommands;
    private final MainChannelContext mainChannelContext;

    @Autowired
    public MainChannel(MainBuilderUtil mainBuilderUtil, EventHandlerMain eventHandlerMain,
                       List<ChatCommand> chatCommands, MainChannelContext mainChannelContext) {
        this.mainBuilderUtil = mainBuilderUtil;
        this.eventHandlerMain = eventHandlerMain;
        this.chatCommands = chatCommands;
        this.mainChannelContext = mainChannelContext;
    }

    @Override
    public void run() {
        mainBuilderUtil.getTwitchClientMain().getChat().joinChannel(mainBuilderUtil.getMainChannelName());
        EventManager eventManagerMain = mainBuilderUtil.getTwitchClientMain().getEventManager();
        // Phase 5 migration: the new dispatcher runs alongside the legacy handler. Commands moved into
        // ChatCommand beans are removed from EventHandlerMain so they don't fire twice.
        eventManagerMain.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerMain);
        eventManagerMain.getEventHandler(SimpleEventHandler.class)
                .registerListener(new ChatEventDispatcher(chatCommands, mainChannelContext));
        mainBuilderUtil.getTwitchClientMain().getPubSub()
                .listenForSubscriptionEvents(mainBuilderUtil.getCredentialMain(), mainBuilderUtil.getMainChannelId());
        mainBuilderUtil.getTwitchClientMain().getPubSub()
                .listenForChannelPointsRedemptionEvents(mainBuilderUtil.getCredentialMain(), mainBuilderUtil.getMainChannelId());
    }
}
