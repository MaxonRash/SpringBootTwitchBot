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

    @EventSubscriber
    public void badMessagesBanCommand(ChannelMessageEvent event) throws IOException {
        String newMessage = event.getMessage();

        if (newMessage.contains("⣿⣿⣿") || newMessage.contains("░░░") || newMessage.contains("███")) {
            String firstNick = event.getUser().getName();

            String commandPermissionString = event.getPermissions().toString();
            commandPermissionString = commandPermissionString.substring(1);
            commandPermissionString = commandPermissionString.substring(0, commandPermissionString.lastIndexOf("]"));
            ArrayList<String> commandPermissionList = new ArrayList<>(Arrays.asList(commandPermissionString.split(",")));

            ArrayList<String> commandPermissionList1 = new ArrayList<>(commandPermissionList);
            ArrayList<String> requiredPermissionList1 = new ArrayList<>(Arrays.asList("VIP, MODERATOR, BROADCASTER".split(",")));

            ArrayList<String> commandPermissionList2 = new ArrayList<>(commandPermissionList);
            ArrayList<String> requiredPermissionList2 = new ArrayList<>(Arrays.asList("PARTNER, SUBSCRIBER, FOUNDER, SUBGIFTER".split(",")));

            commandPermissionList1.retainAll(requiredPermissionList1);
            commandPermissionList2.retainAll(requiredPermissionList2);

            if (!commandPermissionList1.isEmpty()) {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                        " Стыдно, товарищ!");
            }
            else if (!commandPermissionList2.isEmpty()) {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " бан 10 мин за гуся и прочую ересь. Одумайся, уважаемый на канале чел!");
                UtilityCommandsMainChannel.timeoutUser(UtilityCommandsGlobal.getUserIdByName(firstNick), 600, "kaban_i_gus");
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " бан на 11 дней за гуся и прочую ересь.");
                //TODO change to 999999
                UtilityCommandsMainChannel.timeoutUser(UtilityCommandsGlobal.getUserIdByName(firstNick), 999999, "kaban_i_gus");
            }
        }
        if (newMessage.contains("Ỏ")) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + firstNick
                    + " таймач сутки за хрень на пол чата");
            //TODO change to 86400 - sutki
            UtilityCommandsMainChannel.timeoutUser(UtilityCommandsGlobal.getUserIdByName(firstNick), 86400, "polChataHren");
        }


    }


    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    // Test commands
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________

    // TestForPoints reward migrated to commands/TestRewardCommand (Phase 5).

    @EventSubscriber
    public void emoteModeMessage(ChannelMessageEvent event) throws InterruptedException, JsonProcessingException {
        if (botProperties.isOwner(event.getUser().getName())) {
            if (event.getMessage().toLowerCase().contains("!emotemodetest")) {
                UtilityCommandsMainChannel.emoteOnlyMode(true);
                Thread.sleep(7000);
                UtilityCommandsMainChannel.emoteOnlyMode(false);
            }
        }
    }

    // getModeratorsHappa (!mods) migrated to commands/ModsCommand (Phase 5).

    @EventSubscriber
    public void replyTest(ChannelMessageEvent event) {
        if (botProperties.isOwner(event.getUser().getName())) {
            if (event.getMessage().toLowerCase().contains("!rep1ly")) {
//                twitchClientHappa.getChat().sendMessage(event.getChannel().getName(), "ku");
                applicationContext.getBean(MainBuilderUtil.class).sendMessage(event.getChannel().getName(), "ku");
            }
        }
    }
    @EventSubscriber
    public void vipAndUnVipTest(ChannelMessageEvent event) {
        if (botProperties.isOwner(event.getUser().getName()) && event.getMessage().contains("!viptest")) {
            try {
                UtilityCommandsMainChannel.vipUser("steyro");
                Thread.sleep(5000);
                UtilityCommandsMainChannel.unVipUser("steyro");
            } catch (Exception e) {
                log.error("vipAndUnVipTest failed", e);
            }
        }
    }
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
    @EventSubscriber
    public void timeoutMainTest(ChannelMessageEvent event) {
        if (botProperties.isOwner(event.getUser().getName())) {
            String message = event.getMessage();
            try {
                if (message.contains("!time1outnewtest")) {
                    UtilityCommandsMainChannel.timeoutUser(applicationContext.getBean(BotBuilderUtil.class).getTestChannelId(), 10, "no reason");
                    applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "new request sent");
                }
            } catch (IOException e) {
                log.error("timeoutMainTest failed", e);
            }
        }
    }
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
    private FilterMode filterMode = FilterMode.OLD;
    List<String> badWordsList = List.of("хуа", "хуе", "хуё", "хуи", "хуй", "хул", "хуу", "хуэ", "хую", "хуя",
        "еба", "ебб,", "ебе", "ебё", "еби", "ебк", "ебл", "ебн", "ебо", "ебс", "ебу", "ебц", "ебч", "ебщ", "ебъ", "ебы", "ебь", "ебэ", "ебю", "ебя",
        "аеб", "иеб", "йеб", "оеб", "уеб", "ъеб", "ыеб", "ьеб",
        "ёб",
        "эба", "эбб,", "эбе", "эбё", "эби", "эбк", "эбл", "эбн", "эбо", "эбс", "эбу", "эбц", "эбч", "эбщ", "эбъ", "эбы", "эбь", "эбэ", "эбю", "эбя",
        "пизд", "пезд", "пёзд", "пэзд",
        "писд", "песд", "пёсд", "пэсд",
        "педи", "педа", "педо", "педе", "педр",
        "пиди", "пида", "пидо", "пиде", "пидр",
        "бля", "бле", "блэ",
        "blya", "pizd", "hui");
    List<String> notSoBadWordsList = List.of("мудила", "мудак", "мудень", "мудозвон", "huecruch", "дебил", "пидиди", "говн", "насилов", "хрен", "хер");

    @EventSubscriber
    public void changeFilterMode(ChannelMessageEvent event) {
        String newMessage = event.getMessage().toLowerCase();
        newMessage = newMessage.replace(TwitchText.INVISIBLE_TAG, "");

        if ( (botProperties.isModerator(event.getUser().getName())) && (newMessage.toLowerCase().startsWith("!filter")) ) {
            String[] splitMessage  = newMessage.split(" ");
            if (splitMessage.length > 1) {
                String mode = splitMessage[1];
                if (mode.toUpperCase().equals(FilterMode.AI.getName())) {
                    this.filterMode = FilterMode.AI;
                    applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                            " мат фильтр теперь - " + this.filterMode.getName());
                } else if (mode.toUpperCase().equals(FilterMode.OLD.getName())) {
                    this.filterMode = FilterMode.OLD;
                    applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                            " мат фильтр теперь - " + this.filterMode.getName());
                }
                else {
                    applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                            " нужно указать способ фильтрации (ai или old), сейчас - " + this.filterMode.getName());
                }
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                        " нужно указать способ фильтрации (ai или old), сейчас - " + this.filterMode.getName());
            }
        }
    }

    @EventSubscriber
    public void timeoutForMat(ChannelMessageEvent event) {
        if (event.getUser().getName().equals(botProperties.getBotAccountName())) {
            return;
        }
        String newMessage = event.getMessage().toLowerCase();
        newMessage = newMessage.replace(TwitchText.INVISIBLE_TAG, "");
        newMessage = newMessage.replaceAll("(.)\\1+", "$1");
        String trimmedNewMessage = newMessage.replace(" ", "");
        if (this.filterMode == FilterMode.AI) {
            try {
                if (
                        notSoBadWordsList.stream().anyMatch(trimmedNewMessage::contains)
                ) { //return
                } else if (badWordsList.stream().anyMatch(trimmedNewMessage::contains)) {
                    boolean isBadWord = gpt4o.isTextContainingBadWord(newMessage);

                    log.debug("isBadWord: {}", isBadWord);
                    if (isBadWord) {
                        String textAboutBadWord = gpt4o.TextContainingBadWord(newMessage);
                        String id = event.getUser().getId();

                        String eventChannel = event.getChannel().getName();

                        if (event.getChannel().getName().equalsIgnoreCase(applicationContext.getBean(MainBuilderUtil.class).getMainChannelName())) {
                            applicationContext.getBean(BotBuilderUtil.class).sendMessage(eventChannel, "@" + event.getUser().getName() + " " + textAboutBadWord);
                            UtilityCommandsMainChannel.timeoutUser(id, 600, "bad word bot");
                        } else if (event.getChannel().getName().equalsIgnoreCase(applicationContext.getBean(BotBuilderUtil.class).getTestChannelName())) {
                            try {
                                applicationContext.getBean(BotBuilderUtil.class).sendMessage(eventChannel, "@" + event.getUser().getName() + " " + textAboutBadWord);
                                UtilityCommandsTestChannel.timeoutUserTest(id, 5, "bad word bot");

                            } catch (Exception e) {
                                log.error("timeoutForMat: failed to timeout test channel user", e);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                log.error("timeoutForMat failed", e);
            }
        }
        else {
            if ( (newMessage.toLowerCase().contains("мудила")) || (newMessage.toLowerCase().contains("мудак")) || (newMessage.toLowerCase().contains("мудень")) || (newMessage.toLowerCase().contains("мудозвон")) ||
                    (newMessage.toLowerCase().contains("huecruch")) || (newMessage.toLowerCase().contains("дебил")) ) {
                //return
            } else if (Pattern.compile("(?iu)\\b(([уyu]|[нзnz3][аa]|(хитро|не)?[вvwb][зz3]?[ыьъi]|[сsc][ьъ']|(и|[рpr][аa4])[зсzs]ъ?|([оo0][тбtb6]|[пp][оo0][дd9])[ьъ']?|(.\\B)+?[оаеиeo])?-?([еёe][бb6](?!о[рй])|и[пб][ае][тц]).*?|([нn][иеаaie]|([дпdp]|[вv][еe3][рpr][тt])[оo0]|[рpr][аa][зсzc3]|[з3z]?[аa]|с(ме)?|[оo0]([тt]|дно)?|апч)?-?[хxh][уuy]([яйиеёюuie]|ли(?!ган)).*?|([вvw][зы3z]|(три|два|четыре)жды|(н|[сc][уuy][кk])[аa])?-?[бb6][лl]([яy](?!(х|ш[кн]|мб)[ауеыио]).*?|[еэe][дтdt][ь']?)|([рp][аa][сзc3z]|[знzn][аa]|[соsc]|[вv][ыi]?|[пp]([еe][рpr][еe]|[рrp][оиioеe]|[оo0][дd])|и[зс]ъ?|[аоao][тt])?[пpn][иеёieu][зz3][дd9].*?|([зz3][аa])?[пp][иеieu][дd][аоеaoe]?[рrp](ну.*?|[оаoa][мm]|([аa][сcs])?([иiu]([лl][иiu])?[нщктлtlsn]ь?)?|([оo](ч[еиei])?|[аa][сcs])?[кk]([оo]й)?|[юu][гg])[ауеыauyei]?|[мm][аa][нnh][дd]([ауеыayueiи]([лl]([иi][сзc3щ])?[ауеыauyei])?|[оo][йi]|[аоao][вvwb][оo](ш|sh)[ь']?([e]?[кk][ауеayue])?|юк(ов|[ауи])?)|[мm][уuy][дd6]([яyаиоaiuo0].*?|[еe]?[нhn]([ьюия'uiya]|ей))|мля([тд]ь)?|лять|([нз]а|по)х|м[ао]л[ао]фь([яию]|[её]й))\\b").matcher(newMessage.toLowerCase()).find()) {
                String id = event.getUser().getId();

                String eventChannel = event.getChannel().getName();

                if (eventChannel.equalsIgnoreCase(applicationContext.getBean(MainBuilderUtil.class).getMainChannelName())) {
                    try {
                        applicationContext.getBean(BotBuilderUtil.class).sendMessage(eventChannel, "@" + event.getUser().getName() + " Мат в чате запрещён!");
                        UtilityCommandsMainChannel.timeoutUser(id, 600, "bad word bot");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (eventChannel.equalsIgnoreCase(applicationContext.getBean(BotBuilderUtil.class).getTestChannelName())) {
                    try {
                        applicationContext.getBean(BotBuilderUtil.class).sendMessage(eventChannel, "@" + event.getUser().getName() + " Мат в чате запрещён!");
                        UtilityCommandsTestChannel.timeoutUserTest(id, 5, "bad word bot");
                    } catch (Exception e) {
                        log.error("timeoutForMat (old filter): failed to timeout test channel user", e);
                    }
                }
            }
        }
    }

    // tsyaMode state moved to ChannelContext; !tsya + nag migrated to commands/ (Phase 5).

    // changeTsyaMode (!tsya) migrated to commands/TsyaToggleCommand (Phase 5).

    // tellAboutTsyaMistake migrated to commands/TsyaMistakeCommand (Phase 5).

    // gptBotMode state moved to ChannelContext; !botreply + reply migrated to commands/ (Phase 5).

    // changeBotMode (!botreply) migrated to commands/BotReplyToggleCommand (Phase 5).

    // replyToMessage migrated to commands/BotReplyCommand (Phase 5).

    // rebootBotContext (!reboot) migrated to commands/RebootCommand (Phase 5).


}
