package com.bot.springboottwitchbot.connections.channels;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerBot;
import com.bot.springboottwitchbot.event_handlers.EventHandlerMain;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainChannel implements ChannelConnection {
    @Override
    public void Run() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        applicationContext.getBean(MainBuilderUtil.class).getTwitchClientMain().getChat().joinChannel(
                applicationContext.getBean(MainBuilderUtil.class).getMainChannelName());
        EventManager eventManagerMain = applicationContext.getBean(MainBuilderUtil.class).getTwitchClientMain().getEventManager();
//        EventHandlerMain eventHandlerMain = new EventHandlerMain();
        eventManagerMain.getEventHandler(SimpleEventHandler.class).registerListener(applicationContext.getBean(EventHandlerMain.class));
        applicationContext.getBean(MainBuilderUtil.class).getTwitchClientMain().getPubSub()
                .listenForSubscriptionEvents(applicationContext.getBean(MainBuilderUtil.class).getCredentialMain(), applicationContext.getBean(MainBuilderUtil.class).getMainChannelId());
        applicationContext.getBean(MainBuilderUtil.class).getTwitchClientMain().getPubSub()
                .listenForChannelPointsRedemptionEvents(applicationContext.getBean(MainBuilderUtil.class).getCredentialMain(), applicationContext.getBean(MainBuilderUtil.class).getMainChannelId());
    }
}
