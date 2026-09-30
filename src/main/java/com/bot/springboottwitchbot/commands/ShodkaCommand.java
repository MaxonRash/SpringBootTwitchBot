package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.bot.springboottwitchbot.timers.GlobalRouletteTimer;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.stereotype.Component;

/**
 * {@code !сходка} — reports whether a russian-roulette round can be started now, or how long until it can.
 * Read-only over {@link GlobalRouletteTimer} (the roulette game itself stays in the legacy handler for now).
 * Migrated from the {@code russianRouletteTimeLeft} method (identical on both channels).
 */
@Component
public class ShodkaCommand implements ChatCommand {

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return event.getMessage().toLowerCase().contains("!сходка")
                && Global10secCDTimer.getGlobal10secTimer() == null;
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        if (GlobalRouletteTimer.rouletteCooldownTimer == null) {
            ctx.send("ну довай PepegaAim");
        } else {
            ctx.send("@" + event.getUser().getName()
                    + " начать сходку можно через " + GlobalRouletteTimer.rouletteCoolDownTimerLeft + " happaUHW");
        }
        Global10secCDTimer.setGlobal10secTimer();
    }
}
