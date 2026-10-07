package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.config.BotProperties;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.gpt.GptBotMode;
import com.bot.springboottwitchbot.gpt.TsyaMode;
import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/** {@link ChannelContext} for the test channel (sends via the bot account to the test channel). */
@Component
public class TestChannelContext implements ChannelContext {

    private final BotBuilderUtil botBuilderUtil;
    private final BotProperties botProperties;
    private TsyaMode tsyaMode = TsyaMode.OFF;
    private GptBotMode gptBotMode = GptBotMode.ON;
    private final DuelState duelState = new DuelState();

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
    public boolean isSandbox() {
        return true;
    }

    @Override
    public List<String> getModerators() {
        return UtilityCommandsTestChannel.getModeratorsList();
    }

    @Override
    public void timeoutByName(String login, int seconds, String reason) throws IOException {
        UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(login), seconds, reason);
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
    public void send(String message) {
        botBuilderUtil.sendMessage(getChannelName(), message);
    }
}
