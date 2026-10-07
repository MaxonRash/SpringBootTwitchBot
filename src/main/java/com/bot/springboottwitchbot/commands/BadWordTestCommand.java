package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * Test-only sandbox probe: a message containing "badword"/"фыва" times out a fixed test account for 10s
 * and acks in chat. Migrated from {@code badWordMessage} (only existed on the test handler); gated on
 * {@code isSandbox()} so it never runs on main. Keeps the hard-coded test user id, as in the original.
 */
@Component
public class BadWordTestCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (!ctx.isSandbox()) {
            return false;
        }
        String message = event.getMessage();
        return message.contains("badword") || message.contains("фыва");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        UtilityCommandsTestChannel.timeoutUserTest("72903124", 10, "test");
        ctx.send("new Bot: That was a bad word");
    }
}
