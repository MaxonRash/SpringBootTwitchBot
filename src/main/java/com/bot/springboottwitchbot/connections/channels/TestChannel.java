package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerBot;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestChannel implements ChannelConnection {
    private final BotBuilderUtil botBuilderUtil;
    private final EventHandlerBot eventHandlerBot;

    @Autowired
    public TestChannel(BotBuilderUtil botBuilderUtil, EventHandlerBot eventHandlerBot) {
        this.botBuilderUtil = botBuilderUtil;
        this.eventHandlerBot = eventHandlerBot;
    }

    @Override
    public void run() {
        botBuilderUtil.getTwitchClientBot().getChat().joinChannel(botBuilderUtil.getTestChannelName());
        // After second channel credentials, also join the second channel here (inject SecondBuilderUtil).
        EventManager eventManagerBot = botBuilderUtil.getTwitchClientBot().getEventManager();
        eventManagerBot.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerBot);
    }
}
