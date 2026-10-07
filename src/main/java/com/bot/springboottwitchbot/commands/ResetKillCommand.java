package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.bot.springboottwitchbot.timers.GlobalKillTimer;
import com.bot.springboottwitchbot.timers.KillResetTimer;
import com.bot.springboottwitchbot.utilities.TwitchText;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@code !reset kill} (VIP/mod/broadcaster) — clears the {@link GlobalKillTimer} cooldown early.
 * Migrated from {@code resetKillCommand} in both handlers.
 * <p>Drift preserved via {@link ChannelContext#isSandbox()}: main skips the bot's own messages and
 * gates the reset behind its own 120s {@link KillResetTimer} cooldown (replying "не так быстро" while
 * it runs); test resets unconditionally with no secondary cooldown. The two paths are kept separate
 * so each channel keeps its exact replies.
 */
@Component
public class ResetKillCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(ResetKillCommand.class);

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        if (!ctx.isSandbox() && event.getUser().getName().equalsIgnoreCase(ctx.getBotAccountName())) {
            return false;
        }
        return event.getMessage().toLowerCase().startsWith("!reset");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase();

        String[] array = newMessage.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove(TwitchText.INVISIBLE_TAG);

        if (!(newMessage.startsWith("!reset") && (arrayList.size() > 1) && (arrayList.get(1).equals("kill")))) {
            return;
        }

        if (ctx.isSandbox()) {
            ArrayList<String> commandPermissionList = parsePermissions(event, ",");
            ArrayList<String> requiredPermissionList =
                    new ArrayList<>(Arrays.asList("VIP, MODERATOR, BROADCASTER".split(",")));
            commandPermissionList.retainAll(requiredPermissionList);

            if (!commandPermissionList.isEmpty()) {
                GlobalKillTimer.resetKill();
                ctx.send("!kill готов happaDjosh =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿");
            } else if (Global10secCDTimer.getGlobal10secTimer() == null) {
                ctx.send("@" + event.getUser().getName() +
                        " Должна быть одна из этих ролей: " + requiredPermissionList);
            }
            return;
        }

        ArrayList<String> commandPermissionList = parsePermissions(event, ", ");
        log.debug("Permissions All: {}", commandPermissionList);
        ArrayList<String> requiredPermissionList =
                new ArrayList<>(Arrays.asList("VIP, MODERATOR, BROADCASTER".split(", ")));
        log.debug("Permissions Required: {}", requiredPermissionList);
        commandPermissionList.retainAll(requiredPermissionList);
        log.debug("Permissions Left: {}", commandPermissionList);

        if (commandPermissionList.size() != 0 && (KillResetTimer.killResetCooldownTimer == null)) {
            try {
                GlobalKillTimer.resetKill();
            } catch (Exception e) {
                log.error("resetKillCommand failed", e);
            }
            ctx.send("!kill готов happaDjosh =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿");
            KillResetTimer.killResetCooldownTimer = new Timer("killResetCoolDownTimer");
            long delay = 120 * 1000L;
            KillResetTimer.killResetCooldownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    KillResetTimer.killResetCooldownTimer = null;
                }
            }, delay); // delay of kill reset cooldown
            KillResetTimer.setKillResetCoolDownTimerLeft(delay / 1000);
        } else if (commandPermissionList.size() != 0) {
            ctx.send("@" + event.getUser().getName() + " не так быстро Tssk можно через " + KillResetTimer.killResetCoolDownTimerLeft + " сек");
        } else if (Global10secCDTimer.getGlobal10secTimer() == null) {
            ctx.send("@" + event.getUser().getName() +
                    " Должна быть одна из этих ролей: " + requiredPermissionList);
            Global10secCDTimer.setGlobal10secTimer();
        }
    }

    private ArrayList<String> parsePermissions(ChannelMessageEvent event, String delimiter) {
        String commandPermissionString = event.getPermissions().toString();
        commandPermissionString = commandPermissionString.substring(1);
        commandPermissionString = commandPermissionString.substring(0, commandPermissionString.lastIndexOf("]"));
        return new ArrayList<>(Arrays.asList(commandPermissionString.split(delimiter)));
    }
}
