package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.GptBotMode;
import com.bot.springboottwitchbot.gpt.openai.GPT4o;
import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.bot.springboottwitchbot.timers.GlobalReplyTimer;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Answers messages addressed to the bot ({@code @<bot> ...}) via GPT, when {@code !botreply} is ON.
 * Migrated from {@code replyToMessage}. Moderators are answered immediately; other privileged users
 * (sub/VIP/etc.) are answered but rate-limited by {@link GlobalReplyTimer}; everyone else gets a
 * "stop DDoSing me" message (itself throttled by {@link Global10secCDTimer}). The redundant per-channel
 * send branches in the original collapse to {@link ChannelContext#send}.
 */
@Component
public class BotReplyCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(BotReplyCommand.class);

    private static final List<String> REQUIRED_PERMISSIONS =
            Arrays.asList("PARTNER, SUBSCRIBER, FOUNDER, SUBGIFTER, VIP, MODERATOR, BROADCASTER".split(", "));

    private final GPT4o gpt4o;

    @Autowired
    public BotReplyCommand(GPT4o gpt4o) {
        this.gpt4o = gpt4o;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (event.getUser().getName().equals(ctx.getBotAccountName())) {
            return false;
        }
        if (ctx.getGptBotMode() != GptBotMode.ON) {
            return false;
        }
        String newMessage = event.getMessage().toLowerCase().replace("󠀀", "");
        return newMessage.startsWith("@" + ctx.getBotAccountName() + " ");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase().replace("󠀀", "");
        String addressed = newMessage.substring(("@" + ctx.getBotAccountName() + " ").length());
        String user = event.getUser().getName();

        if (ctx.isModerator(user)) {
            String reply = gpt4o.ResponseForTextAddressingToBot(addressed);
            ctx.send("@" + user + " " + reply);
        } else if (GlobalReplyTimer.getTimerLeft() == 0) {
            String permissions = event.getPermissions().toString();
            permissions = permissions.substring(1);
            permissions = permissions.substring(0, permissions.lastIndexOf("]"));
            List<String> userPermissions = new ArrayList<>(Arrays.asList(permissions.split(", ")));
            log.debug("command permission list: {}", userPermissions);
            log.debug("required permission list: {}", REQUIRED_PERMISSIONS);
            userPermissions.retainAll(REQUIRED_PERMISSIONS);
            log.debug("updated command permission: {}", userPermissions);

            if (!userPermissions.isEmpty()) {
                String reply = gpt4o.ResponseForTextAddressingToBot(addressed);
                ctx.send("@" + user + " " + reply);
                GlobalReplyTimer.setTimer();
            }
        } else if (GlobalReplyTimer.getTimerLeft() != 0) {
            if (Global10secCDTimer.getGlobal10secTimer() == null) {
                ctx.send("@" + user + " не дудось меня AAAA (еще " + GlobalReplyTimer.getTimerLeft() + " сек)");
                Global10secCDTimer.setGlobal10secTimer();
            }
        }
    }
}
