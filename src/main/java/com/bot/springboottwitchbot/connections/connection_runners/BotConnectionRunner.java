package com.bot.springboottwitchbot.connections.connection_runners;

import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BotConnectionRunner {
    private final ChannelConnection channelConnection;
    @Autowired
    public BotConnectionRunner(@Qualifier("testChannel") ChannelConnection channelConnection) {
        this.channelConnection = channelConnection;
    }

    public ChannelConnection getChannelConnection() {
        return channelConnection;
    }
}
