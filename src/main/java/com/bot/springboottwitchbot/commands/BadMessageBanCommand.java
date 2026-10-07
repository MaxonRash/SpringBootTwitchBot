package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Auto-moderates two kinds of spam art. ASCII/box-drawing walls (⣿⣿⣿ / ░░░ / ███): VIP+/mods/broadcaster
 * get a scolding only; subs/partners/etc. get a 10-min ban; everyone else a long ban. A second trigger,
 * the "Ỏ" half-chat garbage char, times the user out for a day. Migrated from {@code badMessagesBanCommand}
 * (present in both handlers). Trigger chars are stored as {@code \\uXXXX} escapes so they match exactly.
 * <p>Drift preserved via {@link ChannelContext#isSandbox()}: only the timeout durations differ — the test
 * channel uses 10s for every ban, main uses the real 600s / 999999s / 86400s.
 */
@Component
public class BadMessageBanCommand implements ChatCommand {

    private static final String ART_BRAILLE = "⣿⣿⣿"; // ⣿⣿⣿
    private static final String ART_LIGHT = "░░░";   // ░░░
    private static final String ART_FULL = "███";    // ███
    private static final String HALF_CHAT_CHAR = "Ỏ";         // Ỏ

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        String message = event.getMessage();
        return message.contains(ART_BRAILLE) || message.contains(ART_LIGHT)
                || message.contains(ART_FULL) || message.contains(HALF_CHAT_CHAR);
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String newMessage = event.getMessage();

        if (newMessage.contains(ART_BRAILLE) || newMessage.contains(ART_LIGHT) || newMessage.contains(ART_FULL)) {
            String firstNick = event.getUser().getName();

            String commandPermissionString = event.getPermissions().toString();
            commandPermissionString = commandPermissionString.substring(1);
            commandPermissionString = commandPermissionString.substring(0, commandPermissionString.lastIndexOf("]"));
            ArrayList<String> commandPermissionList = new ArrayList<>(Arrays.asList(commandPermissionString.split(",")));

            ArrayList<String> commandPermissionList1 = new ArrayList<>(commandPermissionList);
            ArrayList<String> requiredPermissionList1 = new ArrayList<>(Arrays.asList("VIP, MODERATOR, BROADCASTER".split(",")));

            ArrayList<String> commandPermissionList2 = new ArrayList<>(commandPermissionList);
            ArrayList<String> requiredPermissionList2 = new ArrayList<>(Arrays.asList("PARTNER, SUBSCRIBER, FOUNDER, SUBGIFTER".split(",")));

            commandPermissionList1.retainAll(requiredPermissionList1);
            commandPermissionList2.retainAll(requiredPermissionList2);

            if (!commandPermissionList1.isEmpty()) {
                ctx.send("@" + event.getUser().getName() + " Стыдно, товарищ!");
            } else if (!commandPermissionList2.isEmpty()) {
                ctx.send("@" + firstNick + " бан 10 мин за гуся и прочую ересь. Одумайся, уважаемый на канале чел!");
                ctx.timeoutByName(firstNick, ctx.isSandbox() ? 10 : 600, "kaban_i_gus");
            } else {
                ctx.send("@" + firstNick + " бан на 11 дней за гуся и прочую ересь.");
                ctx.timeoutByName(firstNick, ctx.isSandbox() ? 10 : 999999, "kaban_i_gus");
            }
        }
        if (newMessage.contains(HALF_CHAT_CHAR)) {
            String firstNick = event.getUser().getName();
            ctx.send("@" + firstNick + " таймач сутки за хрень на пол чата");
            ctx.timeoutByName(firstNick, ctx.isSandbox() ? 10 : 86400, "polChataHren");
        }
    }
}
