package com.bot.springboottwitchbot.commands;
import com.bot.springboottwitchbot.utilities.TwitchText;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code !мут мне на <секунды>} — self-timeout for the requested number of seconds; moderators are
 * refused (they can't mute themselves this way). Migrated from spamMessagesCommand; the only
 * per-channel difference was the underlying util, now handled by {@link ChannelContext}.
 */
@Component
public class MuteMeCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.startsWith("!мут мне на");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String firstNick = event.getUser().getName();

        String[] array = event.getMessage().toLowerCase().split(" ");
        List<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove(TwitchText.INVISIBLE_TAG);

        if (ctx.getModerators().contains(firstNick)) {
            ctx.send("@" + firstNick + " А жареных гвоздей не хочешь? PETTHEMODS");
        } else {
            ctx.timeoutByName(firstNick, Integer.parseInt(arrayList.get(3)), "muteAsked");
        }
    }
}
