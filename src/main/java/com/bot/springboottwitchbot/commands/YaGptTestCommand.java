package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.yandexgpt.YandexGPT;
import com.bot.springboottwitchbot.utilities.TwitchText;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Owner-only debug probe {@code !yagpt_test}: pings YandexGPT's bad-word check with a sample string to
 * confirm the backend is reachable. Migrated from {@code testGettingAIMToken}. Unlike the other test
 * probes this keeps its owner gate (it existed that way on the test handler); strips the invisible tag
 * char before matching.
 */
@Component
public class YaGptTestCommand implements ChatCommand {

    private final YandexGPT yandexGPT;

    @Autowired
    public YaGptTestCommand(YandexGPT yandexGPT) {
        this.yandexGPT = yandexGPT;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase().replace(TwitchText.INVISIBLE_TAG, "");
        return ctx.isOwner(event.getUser().getName()) && newMessage.startsWith("!yagpt_test");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        ctx.send("@" + event.getUser().getName() + " testing...");
        yandexGPT.isTextContainingBadWord("пиздец");
    }
}
