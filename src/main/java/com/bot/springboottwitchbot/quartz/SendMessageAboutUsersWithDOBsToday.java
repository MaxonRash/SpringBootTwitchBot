package com.bot.springboottwitchbot.quartz;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class SendMessageAboutUsersWithDOBsToday implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (UtilityDOB.listOfUsersWithDOB.isEmpty()) {
            ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat()
                    .sendMessage("happasc2", "@" + "Happasc2" + " Сегодня ни у кого нет ДР FeelsBadMan");
        }
        else if (UtilityDOB.listOfUsersWithDOB.size() == 1) {
            ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat()
                    .sendMessage("happasc2", "@" + "Happasc2" + " Сегодня у " + UtilityDOB.listOfUsersWithDOB.get(0)
                    + " день рождения! " + "@" + UtilityDOB.listOfUsersWithDOB.get(0) + " PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan "
                    + "PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan");
            ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat()
                    .sendMessage("happasc2", "@" + "Happasc2" + " Сегодня у " + UtilityDOB.listOfUsersWithDOB.get(0)
                            + " день рождения! " + "@" + UtilityDOB.listOfUsersWithDOB.get(0) + " PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan "
                            + "PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan");
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (String login : UtilityDOB.listOfUsersWithDOB) {
                sb.append("@").append(login).append(" ");
            }
            String allUsersWithDOB = sb.toString().trim();
            ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat()
                    .sendMessage("happasc2", "@" + "Happasc2" + " Сегодня у этих прекрасных людей дни рождения! "
                            + allUsersWithDOB + " PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan");
            ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat()
                    .sendMessage("happasc2", "@" + "Happasc2" + " Сегодня у этих прекрасных людей дни рождения! "
                            + allUsersWithDOB + " PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan PJSalt FeelsBirthdayMan");
        }
    }
}
