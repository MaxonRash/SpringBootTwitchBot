package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.config.BotProperties;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.gpt.GptBotMode;
import com.bot.springboottwitchbot.gpt.TsyaMode;
import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * {@link ChannelContext} for the main channel. Channel identity comes from {@link MainBuilderUtil},
 * but messages are still sent via the bot account ({@link BotBuilderUtil}), matching the existing
 * behavior where the bot chats in the main channel.
 */
@Component
public class MainChannelContext implements ChannelContext {

    private final BotBuilderUtil botBuilderUtil;
    private final MainBuilderUtil mainBuilderUtil;
    private final BotProperties botProperties;
    private TsyaMode tsyaMode = TsyaMode.OFF;
    private GptBotMode gptBotMode = GptBotMode.ON;
    private final DuelState duelState = new DuelState();
    private List<String> roulettePlayers = null;

    @Autowired
    public MainChannelContext(BotBuilderUtil botBuilderUtil, MainBuilderUtil mainBuilderUtil, BotProperties botProperties) {
        this.botBuilderUtil = botBuilderUtil;
        this.mainBuilderUtil = mainBuilderUtil;
        this.botProperties = botProperties;
    }

    @Override
    public String getChannelName() {
        return mainBuilderUtil.getMainChannelName();
    }

    @Override
    public String getChannelId() {
        return mainBuilderUtil.getMainChannelId();
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
    public boolean isSandbox() {
        return false;
    }

    @Override
    public List<String> getModerators() {
        return UtilityCommandsMainChannel.getModeratorsList();
    }

    @Override
    public void timeoutByName(String login, int seconds, String reason) throws IOException {
        UtilityCommandsMainChannel.timeoutUser(UtilityCommandsGlobal.getUserIdByName(login), seconds, reason);
    }

    @Override
    public void timeoutById(String userId, int seconds, String reason) throws IOException {
        UtilityCommandsMainChannel.timeoutUser(userId, seconds, reason);
    }

    @Override
    public TsyaMode getTsyaMode() {
        return tsyaMode;
    }

    @Override
    public void setTsyaMode(TsyaMode mode) {
        this.tsyaMode = mode;
    }

    @Override
    public GptBotMode getGptBotMode() {
        return gptBotMode;
    }

    @Override
    public void setGptBotMode(GptBotMode mode) {
        this.gptBotMode = mode;
    }

    @Override
    public DuelState getDuelState() {
        return duelState;
    }

    @Override
    public List<String> getRoulettePlayers() {
        return roulettePlayers;
    }

    @Override
    public void setRoulettePlayers(List<String> players) {
        this.roulettePlayers = players;
    }

    @Override
    public void send(String message) {
        botBuilderUtil.sendMessage(getChannelName(), message);
    }
}
