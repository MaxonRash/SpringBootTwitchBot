package com.bot.springboottwitchbot.event_handlers;
import com.bot.springboottwitchbot.utilities.TwitchText;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.config.BotProperties;
import com.bot.springboottwitchbot.dto.support.UsersResponseToUserConverter;
import com.bot.springboottwitchbot.SpringBootTwitchBotApplication;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.gpt.FilterMode;
import com.bot.springboottwitchbot.gpt.openai.GPT4o;
import com.bot.springboottwitchbot.gpt.GptBotMode;
import com.bot.springboottwitchbot.gpt.TsyaMode;
import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.services.UsersService;
import com.bot.springboottwitchbot.timers.*;
import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.domain.SubscriptionData;
import com.github.twitch4j.pubsub.events.ChannelSubscribeEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
// The applicationContext field below is initialized from ApplicationContextProvider at construction time,
// so the provider bean must be created first (see ApplicationContextProvider). Removed in Phase 5.
@DependsOn({"applicationContextProvider"})
public class EventHandlerMain {
    private static final Logger log = LoggerFactory.getLogger(EventHandlerMain.class);
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    @Autowired
    UsersService usersService;
    @Autowired
    GPT4o gpt4o;
    @Autowired
    BotProperties botProperties;

    // printChannelMessage migrated to commands/MessageLoggingCommand (Phase 5).

    // duelCommand (!duel) migrated to commands/DuelCommand (Phase 5).

    // killCommand (!kill) migrated to commands/KillCommand (Phase 5).

    // resetKillCommand (!reset kill) migrated to commands/ResetKillCommand (Phase 5).

    // russianRouletteCommand (monkaS roulette) migrated to commands/RouletteCommand (Phase 5).

    // russianRouletteTimeLeft (!сходка) migrated to commands/ShodkaCommand (Phase 5).
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    // Other events Handlers (like subs and triggers)
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________

    // getSubNotification (ChannelSubscribeEvent) migrated to commands/PubSubEventDispatcher (Phase 5).

    // emoteOnlyForPoints reward migrated to commands/EmoteOnlyRewardCommand (Phase 5).

    // vipUserForPoints reward migrated to commands/VipRewardCommand (Phase 5).

    // timeoutUserForPoints reward migrated to commands/TimeoutRewardCommand (Phase 5).

    // checkTodayDOBs (!чек др) migrated to commands/CheckBirthdaysCommand (Phase 5).

    // UserDOB (!др) migrated to commands/BirthdayCommand (Phase 5).


    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    // Spam commands
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________

    //TODO change timeouts from test to real ones
    // spamMessagesCommand fully migrated to commands/ (Phase 5): nick, slot, drops, queue, mute, trigger words, pastas, sudoku, !с локтя.

    // badMessagesBanCommand (ASCII-art ban) migrated to commands/BadMessageBanCommand (Phase 5).


    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    // Test commands
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________

    // TestForPoints reward migrated to commands/TestRewardCommand (Phase 5).

    // emoteModeMessage (!emotemodetest) migrated to commands/EmoteModeTestCommand (Phase 5).

    // getModeratorsHappa (!mods) migrated to commands/ModsCommand (Phase 5).

    // replyTest (!rep1ly) migrated to commands/ReplyTestCommand (Phase 5).
    // vipAndUnVipTest (!viptest) migrated to commands/VipUnvipTestCommand (Phase 5).
//
//    @EventSubscriber
//    public void getSubNotificationTest(ChannelMessageEvent event) {
//        if (botProperties.isOwner(event.getUser().getName())) {
//            String message = event.getMessage();
//            if (message.contains("!testSub")) {
//                System.out.println(event.getEventId());
//            }
//
//        }
//    }
//
    // timeoutMainTest (!time1outnewtest) migrated to commands/TimeoutMainTestCommand (Phase 5).
//
//    @EventSubscriber
//    public void duelCooldownTest(ChannelMessageEvent event) {
//        if (botProperties.isOwner(event.getUser().getName())) {
//            String message = event.getMessage();
//            if (message.contains("!cooldownduel")) {
//                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), String.valueOf(GlobalDuelTimer.duelCoolDownTimerLeft));
//            }
//        }
//    }
//

//
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    // Mat Filter
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//

    // changeFilterMode (!filter) migrated to commands/FilterToggleCommand (Phase 5).

    // timeoutForMat (swear filter) migrated to commands/FilterCommand (Phase 5).

    // tsyaMode state moved to ChannelContext; !tsya + nag migrated to commands/ (Phase 5).

    // changeTsyaMode (!tsya) migrated to commands/TsyaToggleCommand (Phase 5).

    // tellAboutTsyaMistake migrated to commands/TsyaMistakeCommand (Phase 5).

    // gptBotMode state moved to ChannelContext; !botreply + reply migrated to commands/ (Phase 5).

    // changeBotMode (!botreply) migrated to commands/BotReplyToggleCommand (Phase 5).

    // replyToMessage migrated to commands/BotReplyCommand (Phase 5).

    // rebootBotContext (!reboot) migrated to commands/RebootCommand (Phase 5).


}
