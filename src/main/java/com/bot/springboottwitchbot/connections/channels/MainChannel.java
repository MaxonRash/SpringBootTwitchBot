package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerMain;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainChannel implements ChannelConnection {
    private final MainBuilderUtil mainBuilderUtil;
    private final EventHandlerMain eventHandlerMain;

    @Autowired
    public MainChannel(MainBuilderUtil mainBuilderUtil, EventHandlerMain eventHandlerMain) {
        this.mainBuilderUtil = mainBuilderUtil;
        this.eventHandlerMain = eventHandlerMain;
    }

    @Override
    public void run() {
        mainBuilderUtil.getTwitchClientMain().getChat().joinChannel(mainBuilderUtil.getMainChannelName());
        EventManager eventManagerMain = mainBuilderUtil.getTwitchClientMain().getEventManager();
        eventManagerMain.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerMain);
        mainBuilderUtil.getTwitchClientMain().getPubSub()
                .listenForSubscriptionEvents(mainBuilderUtil.getCredentialMain(), mainBuilderUtil.getMainChannelId());
        mainBuilderUtil.getTwitchClientMain().getPubSub()
                .listenForChannelPointsRedemptionEvents(mainBuilderUtil.getCredentialMain(), mainBuilderUtil.getMainChannelId());
    }
}
