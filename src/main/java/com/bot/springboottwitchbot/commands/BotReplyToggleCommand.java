package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.GptBotMode;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * {@code !botreply on|off} (moderators only) — toggles whether the bot answers messages addressed to it
 * ({@link BotReplyCommand}). Migrated from {@code changeBotMode}; the on/off state now lives per-channel
 * in {@link ChannelContext}. Both channels stripped the invisible tag char, so there is no drift here.
 */
@Component
public class BotReplyToggleCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (event.getUser().getName().equals(ctx.getBotAccountName())) {
            return false;
        }
        return ctx.isModerator(event.getUser().getName())
                && event.getMessage().toLowerCase().startsWith("!botreply");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase().replace("󠀀", "");
        String user = event.getUser().getName();
        String[] splitMessage = newMessage.split(" ");
        if (splitMessage.length > 1) {
            String mode = splitMessage[1];
            if (mode.toUpperCase().equals(GptBotMode.ON.getName())) {
                ctx.setGptBotMode(GptBotMode.ON);
                ctx.send("@" + user + " теперь  бот будет отвечать");
            } else if (mode.toUpperCase().equals(GptBotMode.OFF.getName())) {
                ctx.setGptBotMode(GptBotMode.OFF);
                ctx.send("@" + user + " теперь бот не будет отвечать");
            } else {
                ctx.send("@" + user + " нужно указать ON или OFF, сейчас - " + ctx.getGptBotMode().getName());
            }
        } else {
            ctx.send("@" + user + " нужно указать ON или OFF, сейчас - " + ctx.getGptBotMode().getName());
        }
    }
}
