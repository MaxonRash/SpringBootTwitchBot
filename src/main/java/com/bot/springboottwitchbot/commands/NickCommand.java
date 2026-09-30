package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * {@code !ник} / {@code !nick} — rates the caller's nick (d10); on 1 and 10 it times them out.
 * Migrated from spamMessagesCommand. Timeout duration differs per channel (test 10s, main 60s),
 * preserved via {@link ChannelContext#isSandbox()}; everything else was identical.
 */
@Component
public class NickCommand extends CooldownCommand {

    @Override
    protected boolean triggers(String lowerMessage) {
        return lowerMessage.startsWith("!ник") || lowerMessage.startsWith("!nick");
    }

    @Override
    protected void run(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String firstNick = event.getUser().getName();
        ctx.send("Оцениваю " + "@" + firstNick + " ...");
        Thread.sleep(3000);

        int dice = (int) (Math.random() * 10) + 1;
        int timeoutSeconds = ctx.isSandbox() ? 10 : 60;
        if (dice == 1) {
            ctx.send("@" + firstNick + " твой ник калич 4Head 1/10, зобаню даж");
            ctx.timeoutByName(firstNick, timeoutSeconds, "nick");
        } else if (dice == 2) {
            ctx.send("@" + firstNick + " ник ниачом 2/10, даж без смайла");
        } else if (dice == 3) {
            ctx.send("@" + firstNick + " ну такой себе ник 3/10 DansGame");
        } else if (dice == 4) {
            ctx.send("@" + firstNick + " скучный ник 4/10 ResidentSleeper");
        } else if (dice == 5) {
            ctx.send("@" + firstNick + " твердая питёрка 5/10  billyWink");
        } else if (dice == 6) {
            ctx.send("@" + firstNick + " нормальный такой ник SeemsGood 6/10");
        } else if (dice == 7) {
            ctx.send("@" + firstNick + " ну, конечно, не Александр, но 7/10 , not bad ChadYes");
        } else if (dice == 8) {
            ctx.send("@" + firstNick + " ну, конечно, не Максон, но 8/10 PogChamp");
        } else if (dice == 9) {
            ctx.send("@" + firstNick + " хрена себе никчанский happaWut 9/10");
        } else if (dice == 10) {
            ctx.send("@" + firstNick + " твой ник просто прекрасен cageGASM держи happa100 и бан, чтоб другим не обидно было happaShutup");
            ctx.timeoutByName(firstNick, timeoutSeconds, "nick");
        }
    }
}
