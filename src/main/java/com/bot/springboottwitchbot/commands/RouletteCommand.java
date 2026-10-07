package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.timers.GlobalRouletteTimer;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Russian-roulette mini-game, triggered by any message containing "monkas". The first such message opens
 * a lobby; the next ones join it; once 6 players have gathered they "shoot" and one (random) is timed out.
 * If the lobby doesn't fill before the accept window, it's cancelled. Migrated from
 * {@code russianRouletteCommand} in both handlers. The lobby is per-channel
 * ({@link ChannelContext#getRoulettePlayers()}); the accept/cooldown timers stay JVM-wide in
 * {@link GlobalRouletteTimer}.
 * <p>Drift preserved via {@link ChannelContext#isSandbox()}: main skips the bot's own messages and
 * de-duplicates joiners, times out the loser for 180s, and uses a 60s accept window + 600s cooldown;
 * test does none of the de-dup/skip (so dupes and the bot's own trigger message can join), times out
 * for 10s, and uses a 20s accept window + 40s cooldown (plus a lowercase "сходка ... не состоялась").
 */
@Component
public class RouletteCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(RouletteCommand.class);

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (!ctx.isSandbox() && event.getUser().getName().equalsIgnoreCase(ctx.getBotAccountName())) {
            return false;
        }
        return event.getMessage().toLowerCase().contains("monkas");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String newMessage = event.getMessage().toLowerCase();

        if (newMessage.contains("monkas") && ctx.getRoulettePlayers() == null && GlobalRouletteTimer.rouletteCooldownTimer == null) {
            ctx.send("@" + event.getUser().getName() +
                    " инициировал сходку клуба любителей пострелять monkaSHAKE они почему-то пишут monkaS в чат");
            ctx.setRoulettePlayers(new ArrayList<>(Collections.singleton(event.getUser().getName())));

            String notHappenedMessage = ctx.isSandbox()
                    ? "сходка клуба любителей пострелять не состоялась FeelsBadMan"
                    : "Сходка клуба любителей пострелять не состоялась FeelsBadMan";
            long cooldownDelay = (ctx.isSandbox() ? 40 : 600) * 1000L;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    ctx.send(notHappenedMessage);
                    GlobalRouletteTimer.rouletteTimerToAccept = null;
                    ctx.setRoulettePlayers(null);
                    GlobalRouletteTimer.rouletteCooldownTimer = new Timer("rouletteCoolDownTimer");
                    GlobalRouletteTimer.rouletteCooldownTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GlobalRouletteTimer.rouletteCooldownTimer = null;
                        }
                    }, cooldownDelay); // delay of roulette cooldown
                    GlobalRouletteTimer.setRouletteCoolDownTimerLeft(cooldownDelay / 1000);
                }
            };
            if (GlobalRouletteTimer.rouletteTimerToAccept == null) {
                GlobalRouletteTimer.rouletteTimerToAccept = new Timer("rouletteTimerToAccept");
                long delay = (ctx.isSandbox() ? 20 : 60) * 1000L; // delay for "shodka ne sostoyalas"
                GlobalRouletteTimer.rouletteTimerToAccept.schedule(timerTask, delay);
            }

            log.debug("roulette players: {}", ctx.getRoulettePlayers());
        } else if (newMessage.contains("monkas") && ctx.getRoulettePlayers() != null && GlobalRouletteTimer.rouletteCooldownTimer == null) {
            List<String> players = ctx.getRoulettePlayers();
            if (players.size() < 6) {
                // main de-duplicates joiners; the test channel intentionally allows repeats (check was commented out there).
                if (ctx.isSandbox() || !players.contains(event.getUser().getName())) {
                    players.add(event.getUser().getName());
                    log.debug("roulette players: {}", players);
                }
            }
            if (players.size() == 6) {
                GlobalRouletteTimer.rouletteTimerToAccept.cancel();
                GlobalRouletteTimer.rouletteTimerToAccept = null;

                StringBuilder allRoulettePlayers = new StringBuilder();
                for (int i = 0; i < players.size(); i++) {
                    if (i == (players.size() - 1)) {
                        allRoulettePlayers.append("и ").append("@").append(players.get(i));
                    } else {
                        allRoulettePlayers.append("@").append(players.get(i)).append(" ");
                    }
                }

                GlobalRouletteTimer.rouletteCooldownTimer = new Timer("rouletteCoolDownTimer");
                long delay = (ctx.isSandbox() ? 40 : 600) * 1000L;
                GlobalRouletteTimer.rouletteCooldownTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        GlobalRouletteTimer.rouletteCooldownTimer = null;
                    }
                }, delay); // delay of roulette cooldown
                GlobalRouletteTimer.setRouletteCoolDownTimerLeft(delay / 1000);

                ctx.send(allRoulettePlayers.toString() +
                        " стреляют себе в лица, на всех один патрон monkaMEGA ...");
                Thread.sleep(3000);

                int dice = (int) (Math.random() * 6);
                int dice2 = (int) (Math.random() * 2);
                String deadRoulettePlayer = players.get(dice);
                int timeoutSeconds = ctx.isSandbox() ? 10 : 180;
                if (dice2 == 0) {
                    ctx.send("@" + deadRoulettePlayer + " happaF");
                    ctx.timeoutByName(deadRoulettePlayer, timeoutSeconds, "shodka");
                } else {
                    ctx.send("@" + deadRoulettePlayer +
                            "'у повезло и он помер не сразу, есть 5 сек...");
                    Thread timeoutIn5sec = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                                ctx.send("@" + deadRoulettePlayer + " happaF");
                                ctx.timeoutByName(deadRoulettePlayer, timeoutSeconds, "shodka");
                            } catch (InterruptedException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    timeoutIn5sec.start();
                }
                ctx.setRoulettePlayers(null);
            }
        }
    }
}
