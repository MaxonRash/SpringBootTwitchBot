package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.connections.channels.builder_utils.SecondBuilderUtil;
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
        // After second channel credentials
//        applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().joinChannel(
//                applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelName());
        EventManager eventManagerBot = applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getEventManager();
//        EventHandlerBot eventHandlerBot = new EventHandlerBot();
        eventManagerBot.getEventHandler(SimpleEventHandler.class).registerListener(applicationContext.getBean(EventHandlerBot.class) /*eventHandlerBot*/);
    }
}
