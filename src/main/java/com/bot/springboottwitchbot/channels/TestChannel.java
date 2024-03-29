package com.bot.springboottwitchbot.channels;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerBot;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.stereotype.Component;

@Component
public class TestChannel implements ChannelConnection {

    @Override
    public void Run() {
//        twitchClientBot.getChat().joinChannel(getTestChannelName());
        ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().joinChannel(
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTestChannelName());
        EventManager eventManagerBot = ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getEventManager();
        EventHandlerBot eventHandlerBot = new EventHandlerBot();
        eventManagerBot.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerBot);
    }
}
