package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Owner-only, main-only debug probe {@code !rep1ly}: sends "ku" from the MAIN account (not the bot
 * account) to verify that path. Migrated from {@code replyTest}; gated on {@code !isSandbox()} since it
 * only existed on the main handler and sends via {@link MainBuilderUtil} rather than {@link ChannelContext#send}.
 */
@Component
public class ReplyTestCommand implements ChatCommand {

    private final MainBuilderUtil mainBuilderUtil;

    @Autowired
    public ReplyTestCommand(MainBuilderUtil mainBuilderUtil) {
        this.mainBuilderUtil = mainBuilderUtil;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return !ctx.isSandbox()
                && ctx.isOwner(event.getUser().getName())
                && event.getMessage().toLowerCase().contains("!rep1ly");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        mainBuilderUtil.sendMessage(event.getChannel().getName(), "ku");
    }
}
