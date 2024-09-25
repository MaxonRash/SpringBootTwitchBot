package com.bot.springboottwitchbot.connections.connection_runners;

import com.bot.springboottwitchbot.connections.channel_connections.ChannelConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SecondConnectionRunner {
    private final ChannelConnection channelConnection;
    @Autowired
    public SecondConnectionRunner(@Qualifier("secondChannel") ChannelConnection channelConnection) {
        this.channelConnection = channelConnection;
    }

    public ChannelConnection getChannelConnection() {
        return channelConnection;
    }
}
