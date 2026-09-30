package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code !с локтя <user>} (owner only) — times the named user out for 30s. Migrated from the last block
 * of spamMessagesCommand; identical on both channels except the underlying util (now {@link ChannelContext}).
 * No global cooldown (matches the original).
 */
@Component
public class SLoktyaCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return event.getMessage().toLowerCase().startsWith("!с локтя")
                && ctx.isOwner(event.getUser().getName());
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String[] array = event.getMessage().toLowerCase().split(" ");
        List<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove("󠀀");
        ctx.timeoutByName(arrayList.get(2), 30, "sLoktya");
    }
}
