package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.bot.springboottwitchbot.timers.GlobalKillTimer;
import com.bot.springboottwitchbot.utilities.TwitchText;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@code !kill <user>} — the "approach and attack" mini-game: on a privileged user, rolls a 1-in-17
 * outcome, most of which time the target out, then arms {@link GlobalKillTimer}. A bare {@code !kill}
 * (or {@code !kill <user>} during cooldown) reports how long until it's ready. Migrated from
 * {@code killCommand} in both handlers.
 * <p>Drift preserved via {@link ChannelContext#isSandbox()}: main skips the bot's own messages and
 * refuses already-banned targets, times out for 60s/"kill" with a 600s cooldown; test does neither
 * check and times out for 10s/"no" with a 30s cooldown.
 */
@Component
public class KillCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(KillCommand.class);

    private static final List<String> REQUIRED_PERMISSIONS =
            Arrays.asList("PARTNER, SUBSCRIBER, FOUNDER, SUBGIFTER, VIP, MODERATOR, BROADCASTER".split(", "));

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (!ctx.isSandbox() && event.getUser().getName().equalsIgnoreCase(ctx.getBotAccountName())) {
            return false;
        }
        return event.getMessage().toLowerCase().startsWith("!kill");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String newMessage = event.getMessage().toLowerCase();

        String[] array = newMessage.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove(TwitchText.INVISIBLE_TAG);

        if (newMessage.startsWith("!kill") && (arrayList.size() > 1) && (GlobalKillTimer.killCooldownTimer == null)) {
            String firstNick = event.getUser().getName().toLowerCase();
            String secondNick = arrayList.get(1).toLowerCase();
            if (secondNick.startsWith("@")) {
                secondNick = secondNick.substring(1);
            }

            String commandPermissionString = event.getPermissions().toString();
            commandPermissionString = commandPermissionString.substring(1);
            commandPermissionString = commandPermissionString.substring(0, commandPermissionString.lastIndexOf("]"));
            ArrayList<String> commandPermissionList = new ArrayList<>(Arrays.asList(commandPermissionString.split(", ")));
            log.debug("command permission list: {}", commandPermissionList);
            log.debug("required permission list: {}", REQUIRED_PERMISSIONS);
            commandPermissionList.retainAll(REQUIRED_PERMISSIONS);
            log.debug("updated command permission: {}", commandPermissionList);

            if (!commandPermissionList.isEmpty()) {
                if (!ctx.isSandbox() && UtilityCommandsMainChannel.isBannedUser(secondNick)) {
                    ctx.send("@" + firstNick + " оно уже мертво WutFace");
                    return;
                }
                ctx.send("@" + firstNick + " подходит к @" + secondNick + " ...");
                Thread.sleep(3000);

                int killTimeout = ctx.isSandbox() ? 10 : 60;
                String killReason = ctx.isSandbox() ? "no" : "kill";

                int dice = (int) (Math.random() * 17) + 1;
                if (dice == 1) {
                    ctx.send("@" + firstNick + " проходит мимо " + "@" + secondNick + " peepoLeaveFinger");
                } else if (dice == 2) {
                    ctx.send("@" + firstNick + " отстреливает " + "@" + secondNick + " лицо WutFace");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 3) {
                    ctx.send("@" + firstNick + " зачем-то обнимает " + "@" + secondNick + " ヽ༼ຈل͜ຈ༽ﾉ");
                } else if (dice == 4) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " джошукеном ─=≡Σ happaDjosh ))");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 5) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " хадукеном つಠ益ಠ༽つ ─=≡ΣO))");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 6) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " со снайперки ▄︻̿┻̿═━一");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 7) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " с автомата <,︻╦╤─ ҉ — —");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 8) {
                    ctx.send("@" + firstNick + " заколол " + "@" + secondNick + " трезубцами Ψ༼ຈل͜ຈ༽Ψ");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 9) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " с помощью магии ( ͡ ͠° ͟ʖ ͡° )つ──☆*:・ﾟ");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 10) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " катаной ▬▬ι═══════ﺤ");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 11) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " с локтя ༼ᕗ•̀_•́༽ᕗ");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 12) {
                    ctx.send("@" + firstNick + " убивает " + "@" + secondNick + " силой русского репа ヾ(⌐■_■)ノ♪");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 13) {
                    ctx.send("@" + firstNick + " кидает в " + "@" + secondNick + " стол (ノಠ益ಠ)ノ彡┻━┻");
                    ctx.timeoutByName(secondNick, killTimeout, killReason);
                } else if (dice == 14 || dice == 15 || dice == 16 || dice == 17) {
                    ctx.send("@" + firstNick + " зачем-то обнимает " + "@" + secondNick + " ヽ༼ຈل͜ຈ༽ﾉ");
                }

                GlobalKillTimer.killCooldownTimer = new Timer("killCoolDownTimer");
                long delay = (ctx.isSandbox() ? 30 : 600) * 1000L;
                GlobalKillTimer.killCooldownTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (GlobalKillTimer.killCooldownTimer != null) {
                            GlobalKillTimer.killCooldownTimer = null;
                        }
                    }
                }, delay); // delay of kill cooldown
                GlobalKillTimer.setKillCoolDownTimerLeft(delay / 1000);
            }
        } else if (
                ((newMessage.startsWith("!kill")) && (arrayList.size() == 1) && (Global10secCDTimer.getGlobal10secTimer() == null))
                        || ((newMessage.startsWith("!kill")) && (arrayList.size() > 1) && (GlobalKillTimer.killCooldownTimer != null) && (Global10secCDTimer.getGlobal10secTimer() == null))
        ) {
            if (GlobalKillTimer.killCoolDownTimerLeft != 0) {
                ctx.send("@" + event.getUser().getName()
                        + " Подойти к кому-либо можно будет через " + GlobalKillTimer.killCoolDownTimerLeft + " секунд OpieOP");
                Global10secCDTimer.setGlobal10secTimer();
            } else {
                ctx.send("@" + event.getUser().getName()
                        + " !kill готов happaDjosh =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿");
                Global10secCDTimer.setGlobal10secTimer();
            }
        }
    }
}
