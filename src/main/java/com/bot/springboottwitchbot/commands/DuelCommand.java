package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.bot.springboottwitchbot.timers.GlobalDuelTimer;
import com.bot.springboottwitchbot.utilities.TwitchText;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@code !duel <user>} — challenge another viewer to a duel; the target accepts by typing
 * {@code !duel @<challenger>} before the invite expires. Migrated from {@code duelCommand} in both
 * handlers. Pending-challenge state is per-channel ({@link ChannelContext#getDuelState()}); the
 * accept/cooldown timers stay JVM-wide in {@link GlobalDuelTimer}.
 * <p>Drift preserved via {@link ChannelContext#isSandbox()}: main actually times out the loser
 * (60s/"duel") and rolls a 2-sided dice on accept, uses a 600s cooldown + 30s accept window, and adds
 * " happaPled"/" OpieOP" to its messages; test skips the timeouts/dice (only the moderator check fires,
 * which times out the main-channel name 10s/"no" — a pre-refactor test artifact, kept as-is), uses a
 * 30s cooldown + 5s accept window, and plainer messages.
 * <p>One intentional behavior change from the original: the pending duel is cleared only on a genuine
 * accept, so a third party typing an unrelated {@code !duel ...} during an open invite can no longer
 * cancel it (the handlers cleared the names unconditionally in the accept branch).
 */
@Component
public class DuelCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(DuelCommand.class);

    private final MainBuilderUtil mainBuilderUtil;

    @Autowired
    public DuelCommand(MainBuilderUtil mainBuilderUtil) {
        this.mainBuilderUtil = mainBuilderUtil;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return event.getMessage().toLowerCase().startsWith("!duel");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String newMessage = event.getMessage().toLowerCase();

        String[] array = newMessage.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove(TwitchText.INVISIBLE_TAG);

        DuelState duel = ctx.getDuelState();

        if (newMessage.startsWith("!duel") && (arrayList.size() > 1)) {

            if ((GlobalDuelTimer.duelTimerToAccept != null) && (GlobalDuelTimer.duelCooldownTimer == null)) {
                String target = arrayList.get(1).toLowerCase();
                if (target.startsWith("@")) {
                    target = target.substring(1);
                }
                if (target.equals(duel.getChallenger()) && (event.getUser().getName().equals(duel.getTarget()))) {
                    GlobalDuelTimer.duelTimerToAccept.cancel();
                    GlobalDuelTimer.duelTimerToAccept = null;
                    ctx.send("@" + duel.getChallenger() + " и " + "@" + duel.getTarget() + " подходят друг к другу...");
                    GlobalDuelTimer.duelCooldownTimer = new Timer("duelCoolDownTimer");
                    long delay = (ctx.isSandbox() ? 30 : 600) * 1000L;
                    GlobalDuelTimer.duelCooldownTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GlobalDuelTimer.duelCooldownTimer = null;
                        }
                    }, delay); // delay of duel cooldown
                    GlobalDuelTimer.setDuelCoolDownTimerLeft(delay / 1000);

                    if (ctx.getModerators().contains(duel.getTarget())) {
                        ctx.send("@" + duel.getChallenger() + " он же модир FailFish");
                        if (ctx.isSandbox()) {
                            ctx.timeoutByName(mainBuilderUtil.getMainChannelName(), 10, "no");
                        } else {
                            ctx.timeoutByName(duel.getChallenger(), 60, "duel");
                        }
                    } else if (!ctx.isSandbox()) {
                        int dice = (int) (Math.random() * 2) + 1;
                        if (dice == 1) {
                            ctx.send("@" + duel.getChallenger() + " отстреливает " + "@" + duel.getTarget() + " лицо happaDans");
                            ctx.timeoutByName(duel.getTarget(), 60, "duel");
                        }
                        if (dice == 2) {
                            ctx.send("@" + duel.getTarget() + " отстреливает " + "@" + duel.getChallenger() + " лицо happaDans");
                            ctx.timeoutByName(duel.getChallenger(), 60, "duel");
                        }
                    }

                    // Clear the pending duel only on a real accept, so an unrelated "!duel ..." typed by
                    // a third party while an invite is open can no longer void it (deliberate fix: the
                    // original cleared the names here unconditionally).
                    duel.clear();
                }
            }

            // snachala rabotaet 2 else, perviy uje na otvet

            else if ((GlobalDuelTimer.duelTimerToAccept == null) && (GlobalDuelTimer.duelCooldownTimer == null)) {
                String target = arrayList.get(1).toLowerCase();
                if (target.startsWith("@")) {
                    target = target.substring(1);
                }
                String challenger = event.getUser().getName();
                if (challenger.equals(target)) {
                    ctx.send("@" + challenger + " выстрелил себе в лицо FailFish");
                    if (!ctx.isSandbox()) {
                        ctx.timeoutByName(challenger, 60, "duel");
                    }
                    GlobalDuelTimer.duelCooldownTimer = new Timer("duelCoolDownTimer");
                    long delay = (ctx.isSandbox() ? 30 : 600) * 1000L;
                    GlobalDuelTimer.duelCooldownTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GlobalDuelTimer.duelCooldownTimer = null;
                        }
                    }, delay); // delay of duel cooldown
                    GlobalDuelTimer.setDuelCoolDownTimerLeft(delay / 1000);
                } else {
                    duel.setChallenger(challenger.toLowerCase());
                    ctx.send("@" + challenger + " вызывает " + "@" + target + " на дуэль!" + (ctx.isSandbox() ? "" : " happaPled"));
                    log.debug("duel command args: {}", arrayList);
                    String finalTarget = target;
                    duel.setTarget(finalTarget);
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            ctx.send("@" + finalTarget + " намочил штанишки 4Head");
                            GlobalDuelTimer.duelTimerToAccept = null;
                        }
                    };
                    if (GlobalDuelTimer.duelTimerToAccept == null) {
                        GlobalDuelTimer.duelTimerToAccept = new Timer("duelTimer");
                        long delay = (ctx.isSandbox() ? 5 : 30) * 1000L; // delay for "namochil shtanishki"
                        GlobalDuelTimer.duelTimerToAccept.schedule(timerTask, delay);
                    }
                }
            } else if (Global10secCDTimer.getGlobal10secTimer() == null) {
                replyDuelCooldown(event, ctx);
            }
        } else if (newMessage.startsWith("!duel") && (arrayList.size() == 1) && (Global10secCDTimer.getGlobal10secTimer() == null)) {
            replyDuelCooldown(event, ctx);
        }
    }

    private void replyDuelCooldown(ChannelMessageEvent event, ChannelContext ctx) {
        if (GlobalDuelTimer.duelCoolDownTimerLeft != 0) {
            ctx.send("@" + event.getUser().getName()
                    + " вызвать кого-либо на дуэль можно через " + GlobalDuelTimer.duelCoolDownTimerLeft + " секунд"
                    + (ctx.isSandbox() ? "" : " OpieOP"));
            Global10secCDTimer.setGlobal10secTimer();
        } else {
            ctx.send("@" + event.getUser().getName() + " можно вызвать кого-то на дуэль Kappa");
            Global10secCDTimer.setGlobal10secTimer();
        }
    }
}
