package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@code !чек др} — announces whose birthday is today (from {@link UtilityDOB#listOfUsersWithDOB}).
 * Migrated from {@code checkTodayDOBs} (identical on both channels). Permission intentionally checks the
 * MAIN broadcaster + MAIN moderator list even on the test channel (preserving the original behavior);
 * the Helix moderator lookup now runs only when the command is actually used (it ran on every message before).
 */
@Component
public class CheckBirthdaysCommand implements ChatCommand {

    private final MainBuilderUtil mainBuilderUtil;

    @Autowired
    public CheckBirthdaysCommand(MainBuilderUtil mainBuilderUtil) {
        this.mainBuilderUtil = mainBuilderUtil;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        return event.getMessage().toLowerCase().contains("!чек др");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String user = event.getUser().getName();
        boolean permitted = user.equalsIgnoreCase(mainBuilderUtil.getMainChannelName())
                || ctx.isOwner(user)
                || ctx.isModerator(user)
                || UtilityCommandsMainChannel.getModeratorsList().contains(user);
        if (!permitted) {
            return;
        }

        List<String> dobs = UtilityDOB.listOfUsersWithDOB;
        if (dobs.isEmpty()) {
            ctx.send("@" + user + " Сегодня ни у кого нет ДР FeelsBadMan");
        } else if (dobs.size() == 1) {
            ctx.send("@" + user + " Сегодня у " + dobs.get(0) + " день рождения! " + "@" + dobs.get(0)
                    + " PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan "
                    + "PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String login : dobs) {
                sb.append("@").append(login).append(" ");
            }
            String allUsersWithDOB = sb.toString().trim();
            ctx.send("@" + user + " Сегодня у этих прекрасных людей дни рождения! "
                    + allUsersWithDOB + " PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan");
        }
    }
}
