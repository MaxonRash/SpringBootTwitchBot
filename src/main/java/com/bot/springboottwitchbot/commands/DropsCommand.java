package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * {@code !drops} / {@code !дропс} — rolls a d6 of flavor "prizes", some of which time the roller out.
 * <p>
 * Does NOT extend {@link CooldownCommand}: it is cooldown-gated but the two channels drifted, so the
 * behavior is preserved per-channel via {@link ChannelContext#isSandbox()}:
 * <ul>
 *   <li>Test (sandbox): dice 1 and 4 both time out 10s; does NOT set the cooldown afterwards.</li>
 *   <li>Main: dice 1 does not time out, dice 4 times out 30s; sets the 10s cooldown afterwards.</li>
 * </ul>
 */
@Component
public class DropsCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        String message = event.getMessage().toLowerCase();
        return Global10secCDTimer.getGlobal10secTimer() == null
                && (message.startsWith("!drops") || message.startsWith("!дропс"));
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String firstNick = event.getUser().getName();
        int dice = (int) (Math.random() * 6) + 1;
        if (dice == 1) {
            ctx.send("@" + firstNick + " ОГО! Ты получаешь целое... НИ ХУ ХРЫ!");
            if (ctx.isSandbox()) { // test channel timed out on dice 1; main did not
                ctx.timeoutByName(firstNick, 10, "drops");
            }
        } else if (dice == 2) {
            ctx.send("@" + firstNick + " ОГО! Да тебе выпало целое НИ ФИ ГА! жоска!");
        } else if (dice == 3) {
            ctx.send("@" + firstNick + " ОГО! У тебя теперь есть НИ ЧЕР ТА! круто!");
        } else if (dice == 4) {
            ctx.send("@" + firstNick + " ОГО! Да это же ШИШ С МАСЛОМ! забирай! и таймач прихвати!");
            ctx.timeoutByName(firstNick, ctx.isSandbox() ? 10 : 30, "drops");
        } else if (dice == 5) {
            ctx.send("@" + firstNick + " ОГО! Да это же ГОЛЯК! грац!");
        } else if (dice == 6) {
            ctx.send("@" + firstNick + " ОГО! Тут всего-то чуть меньше, чем НИ ЧТО! Вау!");
        }
        if (!ctx.isSandbox()) { // main set the cooldown after drops; test did not
            Global10secCDTimer.setGlobal10secTimer();
        }
    }
}
