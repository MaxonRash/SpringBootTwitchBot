package com.bot.springboottwitchbot.channels;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerBot;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TestChannel implements ChannelConnection {

    @Override
    public void Run() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().joinChannel(
                applicationContext.getBean(BotBuilderUtil.class).getTestChannelName());
        EventManager eventManagerBot = applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getEventManager();
        EventHandlerBot eventHandlerBot = new EventHandlerBot();
        eventManagerBot.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerBot);
    }
}
