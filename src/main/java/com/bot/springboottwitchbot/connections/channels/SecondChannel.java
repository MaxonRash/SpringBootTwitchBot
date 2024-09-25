package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.connections.channels.builder_utils.SecondBuilderUtil;
import com.bot.springboottwitchbot.event_handlers.EventHandlerSecond;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SecondChannel implements ChannelConnection {
    public void Run() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        applicationContext.getBean(SecondBuilderUtil.class).getTwitchClientSecond().getChat().joinChannel(
                applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelName());
        EventManager eventManagerSecond = applicationContext.getBean(SecondBuilderUtil.class).getTwitchClientSecond().getEventManager();
        eventManagerSecond.getEventHandler(SimpleEventHandler.class).registerListener(applicationContext.getBean(EventHandlerSecond.class));
    }
}
