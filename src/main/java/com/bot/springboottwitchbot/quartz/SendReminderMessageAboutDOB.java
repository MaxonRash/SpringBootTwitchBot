package com.bot.springboottwitchbot.quartz;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SendReminderMessageAboutDOB implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat()
                .sendMessage("happasc2", "Добавляй свой др командой !др день/месяц/год или день/месяц , если фолловер >6 месяцев! "
                + "Поздравления в 14:30");
    }
}
