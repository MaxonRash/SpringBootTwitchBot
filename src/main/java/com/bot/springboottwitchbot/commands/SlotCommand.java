package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * {@code !слот} / {@code !slot} — a 3-emote slot machine; the "EZ" jackpot times the winner out 300s.
 * <p>
 * Does NOT extend {@link CooldownCommand} because the two channels drifted on the cooldown: test had
 * its {@code setGlobal10secTimer()} commented out (no cooldown), main sets it. Preserved via
 * {@link ChannelContext#isSandbox()}. The 300s jackpot timeout is the same on both channels.
 */
@Component
public class SlotCommand implements ChatCommand {

    private static final List<String> EMOTES =
            Arrays.asList("mericCat", "happaPepe", "happaWut", "happaPride", "happa100", "PepeLaugh", "EZ");

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        String message = event.getMessage().toLowerCase();
        return Global10secCDTimer.getGlobal10secTimer() == null
                && (message.startsWith("!слот") || message.startsWith("!slot"));
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String firstNick = event.getUser().getName();

        String firstSlot = EMOTES.get((int) (Math.random() * 7));
        String secondSlot = EMOTES.get((int) (Math.random() * 7));
        String thirdSlot = EMOTES.get((int) (Math.random() * 7));

        ctx.send(firstSlot + " " + secondSlot + " " + thirdSlot);

        if (firstSlot.equals("mericCat") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
            ctx.send("@" + firstNick + " Вы выиграли 1$ haHAA");
        }
        if (firstSlot.equals("happaPepe") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
            ctx.send("@" + firstNick + " Вы выиграли 100$");
        }
        if (firstSlot.equals("happaWut") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
            ctx.send("@" + firstNick + " Вы выиграли 1000$!");
        }
        if (firstSlot.equals("happaPride") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
            ctx.send("@" + firstNick + " Вы выиграли 69$  gachiBASS");
        }
        if (firstSlot.equals("happa100") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
            ctx.send("@" + firstNick + " Вы выиграли 100 рублей!!! Два трека (условно)!");
        }
        if (firstSlot.equals("PepeLaugh") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
            ctx.send("@" + firstNick + " Вы выиграли 100000$!!!");
        }
        if (firstSlot.equals("EZ") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
            ctx.send("@" + firstNick + " Вы выиграли 1000000$ и разорили казино!!!");
            ctx.timeoutByName(firstNick, 300, "Casino");
        }

        if (!ctx.isSandbox()) { // test channel had the cooldown commented out; main sets it
            Global10secCDTimer.setGlobal10secTimer();
        }
    }
}
