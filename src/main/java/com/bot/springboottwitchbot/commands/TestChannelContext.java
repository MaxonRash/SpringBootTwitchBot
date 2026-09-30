package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.config.BotProperties;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** {@link ChannelContext} for the test channel (sends via the bot account to the test channel). */
@Component
public class TestChannelContext implements ChannelContext {

    private final BotBuilderUtil botBuilderUtil;
    private final BotProperties botProperties;

    @Autowired
    public TestChannelContext(BotBuilderUtil botBuilderUtil, BotProperties botProperties) {
        this.botBuilderUtil = botBuilderUtil;
        this.botProperties = botProperties;
    }

    @Override
    public String getChannelName() {
        return botBuilderUtil.getTestChannelName();
    }

    @Override
    public String getChannelId() {
        return botBuilderUtil.getTestChannelId();
    }

    @Override
    public boolean isOwner(String username) {
        return botProperties.isOwner(username);
    }

    @Override
    public boolean isModerator(String username) {
        return botProperties.isModerator(username);
    }

    @Override
    public String getBotAccountName() {
        return botProperties.getBotAccountName();
    }

    @Override
    public void send(String message) {
        botBuilderUtil.sendMessage(getChannelName(), message);
    }
}
