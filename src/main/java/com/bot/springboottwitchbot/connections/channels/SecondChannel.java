package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.connections.channels.builder_utils.SecondBuilderUtil;
import com.bot.springboottwitchbot.event_handlers.EventHandlerSecond;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecondChannel implements ChannelConnection {
    private final SecondBuilderUtil secondBuilderUtil;
    private final EventHandlerSecond eventHandlerSecond;

    @Autowired
    public SecondChannel(SecondBuilderUtil secondBuilderUtil, EventHandlerSecond eventHandlerSecond) {
        this.secondBuilderUtil = secondBuilderUtil;
        this.eventHandlerSecond = eventHandlerSecond;
    }

    @Override
    public void run() {
        secondBuilderUtil.getTwitchClientSecond().getChat().joinChannel(secondBuilderUtil.getSecondChannelName());
        EventManager eventManagerSecond = secondBuilderUtil.getTwitchClientSecond().getEventManager();
        eventManagerSecond.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerSecond);
    }
}
