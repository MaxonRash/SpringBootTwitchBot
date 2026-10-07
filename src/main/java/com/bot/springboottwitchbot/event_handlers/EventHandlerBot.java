package com.bot.springboottwitchbot.event_handlers;
import com.bot.springboottwitchbot.utilities.TwitchText;


import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.config.BotProperties;
import com.bot.springboottwitchbot.dto.support.UsersResponseToUserConverter;
import com.bot.springboottwitchbot.SpringBootTwitchBotApplication;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.gpt.openai.GPT4o;
import com.bot.springboottwitchbot.gpt.GptBotMode;
import com.bot.springboottwitchbot.gpt.TsyaMode;
import com.bot.springboottwitchbot.gpt.yandexgpt.GetIAMTokenFromOAuth;
import com.bot.springboottwitchbot.gpt.yandexgpt.YandexGPT;
import com.bot.springboottwitchbot.gpt.yandexgpt.yandexgptDTO.OAuthToken;
import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.services.UsersService;
import com.bot.springboottwitchbot.timers.*;
import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
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

import static java.time.temporal.ChronoUnit.DAYS;

@Component
// The applicationContext field below is initialized from ApplicationContextProvider at construction time,
// so the provider bean must be created first (see ApplicationContextProvider). Removed in Phase 5.
@DependsOn({"applicationContextProvider"})
public class EventHandlerBot {
    private static final Logger log = LoggerFactory.getLogger(EventHandlerBot.class);
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    @Autowired
    UsersService usersService;
    @Autowired
    GPT4o gpt4o;
    @Autowired
    BotProperties botProperties;
    @Autowired
    YandexGPT yandexGPT;

    // printChannelMessage migrated to commands/MessageLoggingCommand (Phase 5).

    // duelCommand (!duel) migrated to commands/DuelCommand (Phase 5).

    // killCommand (!kill) migrated to commands/KillCommand (Phase 5).

    // resetKillCommand (!reset kill) migrated to commands/ResetKillCommand (Phase 5).

    private ArrayList<String> russianRoulettePlayers = null;
    @EventSubscriber
    public void russianRouletteCommand(ChannelMessageEvent event) throws InterruptedException, IOException {
        String newMessage = event.getMessage().toLowerCase();
        if (newMessage.contains("monkas") && russianRoulettePlayers == null && GlobalRouletteTimer.rouletteCooldownTimer == null) {
            applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                    " инициировал сходку клуба любителей пострелять monkaSHAKE они почему-то пишут monkaS в чат");
            russianRoulettePlayers = new ArrayList<>(Collections.singleton(event.getUser().getName()));

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "сходка клуба любителей пострелять не состоялась FeelsBadMan");
                    GlobalRouletteTimer.rouletteTimerToAccept = null;
                    russianRoulettePlayers = null;
                    GlobalRouletteTimer.rouletteCooldownTimer = new Timer("rouletteCoolDownTimer");
                    //TODO change delay to 600
                    long delay = 40 * 1000L;
                    GlobalRouletteTimer.rouletteCooldownTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GlobalRouletteTimer.rouletteCooldownTimer = null;
                        }
                    }, delay); // delay of roulette cooldown
                    GlobalRouletteTimer.setRouletteCoolDownTimerLeft(delay / 1000);
                }
            };
            if (GlobalRouletteTimer.rouletteTimerToAccept == null) {
                GlobalRouletteTimer.rouletteTimerToAccept = new Timer("rouletteTimerToAccept");
                //TODO change delay to 60
                long delay = 20 * 1000L; // delay for "shodka ne sostoyalas"
                GlobalRouletteTimer.rouletteTimerToAccept.schedule(timerTask, delay);
            }

            log.debug("roulette players: {}", russianRoulettePlayers);
        }
        else if (newMessage.contains("monkas") && russianRoulettePlayers != null && GlobalRouletteTimer.rouletteCooldownTimer == null) {
            if (russianRoulettePlayers.size() < 6) {
                //TODO uncomment after tests
//                if (!russianRoulettePlayers.contains(event.getUser().getName())) {
                russianRoulettePlayers.add(event.getUser().getName());
                log.debug("roulette players: {}", russianRoulettePlayers);
//                }
            }
            if (russianRoulettePlayers.size() == 6) {
                GlobalRouletteTimer.rouletteTimerToAccept.cancel();
                GlobalRouletteTimer.rouletteTimerToAccept = null;

                StringBuilder allRoulettePlayers = new StringBuilder();
                for (int i = 0; i < russianRoulettePlayers.size(); i++) {
                    if (i == (russianRoulettePlayers.size() - 1)) {
                        allRoulettePlayers.append("и ").append("@").append(russianRoulettePlayers.get(i));
                    }
                    else {
                        allRoulettePlayers.append("@").append(russianRoulettePlayers.get(i)).append(" ");
                    }
                }

                GlobalRouletteTimer.rouletteCooldownTimer = new Timer("rouletteCoolDownTimer");
                //TODO change delay to 600
                long delay = 40 * 1000L;
                GlobalRouletteTimer.rouletteCooldownTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        GlobalRouletteTimer.rouletteCooldownTimer = null;
                    }
                }, delay); // delay of roulette cooldown
                GlobalRouletteTimer.setRouletteCoolDownTimerLeft(delay / 1000);

                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), allRoulettePlayers.toString() +
                        " стреляют себе в лица, на всех один патрон monkaMEGA ...");
                Thread.sleep(3000);

                int dice = (int) (Math.random() * 6);
                int dice2 = (int) (Math.random()* 2);
                String deadRoulettePlayer = russianRoulettePlayers.get(dice);
                if (dice2 == 0) {
                    applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + deadRoulettePlayer + " happaF");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(deadRoulettePlayer), 10, "shodka");
                }
                else {
                    applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + deadRoulettePlayer +
                            "'у повезло и он помер не сразу, есть 5 сек...");
                    Thread timeoutIn5sec = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + deadRoulettePlayer + " happaF");
                                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(deadRoulettePlayer), 10, "shodka");
                            } catch (InterruptedException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    timeoutIn5sec.start();
                }
                russianRoulettePlayers = null;
            }
        }
    }

    // russianRouletteTimeLeft (!сходка) migrated to commands/ShodkaCommand (Phase 5).

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
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "kaban_i_gus");
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " бан на 11 дней за гуся и прочую ересь.");
                //TODO change to 999999
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "kaban_i_gus");
            }
        }
        if (newMessage.contains("Ỏ")) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + firstNick
                    + " таймач сутки за хрень на пол чата");
            //TODO change to 86400 - sutki
            UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "polChataHren");
        }


    }
//
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    // Test commands
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//    //_______________________________________________________________________________________________________________________________
//
////    @EventSubscriber
////    public void duelCommandAccept(ChannelMessageEvent event) {
////        String newMessage = event.getMessage();
////        if (newMessage.toLowerCase().startsWith("!duel") && (this.duelTimer != null)) {
//////            System.out.println("this works");
////            String eventChannel = event.getChannel().getName();
////
////            String[] array = newMessage.split(" ");
////            String secondDuelName = array[1].toLowerCase();
////            if (secondDuelName.startsWith("@")) {
////                secondDuelName = secondDuelName.substring(1);
////            }
////            if (secondDuelName.equals(this.firstDuelName) && (event.getUser().getName().equals(this.secondDuelName))) {
////                this.duelTimer = null;
////                twitchClient.getChat().sendMessage(eventChannel,
////                        "@" + this.firstDuelName + " и " + "@" + this.secondDuelName + " подходят друг к другу...");
////            }
////            //TODO duel logic
////            this.firstDuelName = null;
////            this.secondDuelName = null;
////        }
////    }
//
    @EventSubscriber
    public void badWordMessage(ChannelMessageEvent event) throws IOException {
        String message = event.getMessage();
        if (message.contains("badword") || message.contains("фыва")) {
            UtilityCommandsTestChannel.timeoutUserTest("72903124", 10, "test");
            applicationContext.getBean(BotBuilderUtil.class).sendMessage("maximuz666", "new Bot: That was a bad word");
        }
    }

    // getModeratorsTest (!mods) migrated to commands/ModsCommand (Phase 5).
    @EventSubscriber
    public void getUserIdTest(ChannelMessageEvent event) {
        String message = event.getMessage();
        if (message.contains("!test")) {
            try {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage("maximuz666", "new bot: " + UtilityCommandsGlobal.getUserIdByName("maximuz666"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @EventSubscriber
    public void vipUser(ChannelMessageEvent event) throws IOException {
        String message = event.getMessage();
        if (message.contains("!vip")) {
            UtilityCommandsTestChannel.vipUser(message.split(" ")[1]);
        }
    }

    @EventSubscriber
    public void followedSinceTest(ChannelMessageEvent event) throws IOException, ParseException {
        String message = event.getMessage();
        if (message.contains("!follow")) {
            User user = usersService.findOne("winretkristin");
            Date followingSince = UtilityCommandsTestChannel.getFollowingSinceDate(137335434);
            user.setFollowingSince(followingSince);
            usersService.save(user);
        }
    }

    @EventSubscriber
    public void BobFollowTest(ChannelMessageEvent event) throws IOException, ParseException {
        String message = event.getMessage();
        if (message.contains("!checkfollow")) {
            Date date = UtilityCommandsTestChannel.getFollowingSinceDate(Integer.parseInt(Objects.requireNonNull(UtilityCommandsGlobal.getUserIdByName(event.getUser().getName()))));
//            User user = usersService.findOne("steyro");
            if (date != null) {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage("maximuz666", date.toString());
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).sendMessage("maximuz666", "TI NE FOLLOWER");
            }
        }
    }

    // checkTodayDOBs (!чек др) migrated to commands/CheckBirthdaysCommand (Phase 5).

    // UserDOBTest (!др) migrated to commands/BirthdayCommand (Phase 5).

    @EventSubscriber
    public void timeoutHappaTest(ChannelMessageEvent event) {
        String message = event.getMessage();
        try {
            if (message.contains("!emotetest")) {
                UtilityCommandsTestChannel.emoteOnlyMode(true);
            }
        } catch (IOException e) {
            log.error("timeoutHappaTest failed", e);
        }
    }

    @EventSubscriber
    public void checkIfUserIsBanned(ChannelMessageEvent event) {
        String message = event.getMessage();
            if (message.contains("!checkbanned")) {
                boolean isBanned = UtilityCommandsTestChannel.isBannedUser("72903124");
                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), "@" + event.getUser().getName()
                                + " " + isBanned);
            }
    }
//
//    @EventSubscriber
//    public void duelCooldownTest(ChannelMessageEvent event) {
//        String message = event.getMessage();
//        if (message.contains("!cooldownduel")) {
//            applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), String.valueOf(GlobalDuelTimer.duelCoolDownTimerLeft));
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
            "blya", "pizd", "hui", "eba", "ebl", "ebu");
    List<String> notSoBadWordsList = List.of("мудила", "мудак", "мудень", "мудозвон", "huecruch", "дебил", "пидиди");

    @EventSubscriber
    public void timeoutForMat(ChannelMessageEvent event) {
//    String newMessage = event.getMessage();
        String newMessage = event.getMessage().toLowerCase();
        newMessage = newMessage.replace(TwitchText.INVISIBLE_TAG, "");
        newMessage = newMessage.replaceAll("(.)\\1+", "$1");
        String trimmedNewMessage = newMessage.replace(" ", "");
//        String newMessage = message.replaceAll("\\s", "").toLowerCase();

//        twitchClient.getChat().sendMessage("maximuz666", "request sent: " + newMessage);
        try {
//            if (StringUtils.containsIgnoreCase(message, "бля")) {
            if (
                    notSoBadWordsList.stream().anyMatch(trimmedNewMessage::contains)
//                ( (newMessage.toLowerCase().contains("мудила")) || (newMessage.toLowerCase().contains("мудак")) || (newMessage.toLowerCase().contains("мудень")) || (newMessage.toLowerCase().contains("мудозвон")) ||
//                        (newMessage.toLowerCase().contains("huecruch")) || (newMessage.toLowerCase().contains("дебил")) )
            ) {}

            else if (badWordsList.stream().anyMatch(trimmedNewMessage::contains)

//                (Pattern.compile("[.]*ахую[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    /*Pattern.compile("^хуй[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*хуй[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("^х[ую[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*хую[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*похуй[.])*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*нахуй[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*хуя[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*хуи[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//
//                    Pattern.compile("[.]*хуев[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*хуё[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*охуе[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//
//                    Pattern.compile("[.]*пизд[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("^бля[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*бля[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*бляд[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*блеат[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("[.]*блеят[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                    Pattern.compile("\\s+бля", Pattern.CASE_INSENSITIVE).matcher(newMessage).find()*/
//
//                        Pattern.compile("[.]*ахуе[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                        Pattern.compile("[.]*прихуе[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
//                        Pattern.compile("(?iu)\\b(([уyu]|[нзnz3][аa]|(хитро|не)?[вvwb][зz3]?[ыьъi]|[сsc][ьъ']|(и|[рpr][аa4])[зсzs]ъ?|([оo0][тбtb6]|[пp][оo0][дd9])[ьъ']?|(.\\B)+?[оаеиeo])?-?([еёe][бb6](?!о[рй])|и[пб][ае][тц]).*?|([нn][иеаaie]|([дпdp]|[вv][еe3][рpr][тt])[оo0]|[рpr][аa][зсzc3]|[з3z]?[аa]|с(ме)?|[оo0]([тt]|дно)?|апч)?-?[хxh][уuy]([яйиеёюuie]|ли(?!ган)).*?|([вvw][зы3z]|(три|два|четыре)жды|(н|[сc][уuy][кk])[аa])?-?[бb6][лl]([яy](?!(х|ш[кн]|мб)[ауеыио]).*?|[еэe][дтdt][ь']?)|([рp][аa][сзc3z]|[знzn][аa]|[соsc]|[вv][ыi]?|[пp]([еe][рpr][еe]|[рrp][оиioеe]|[оo0][дd])|и[зс]ъ?|[аоao][тt])?[пpn][иеёieu][зz3][дd9].*?|([зz3][аa])?[пp][иеieu][дd][аоеaoe]?[рrp](ну.*?|[оаoa][мm]|([аa][сcs])?([иiu]([лl][иiu])?[нщктлtlsn]ь?)?|([оo](ч[еиei])?|[аa][сcs])?[кk]([оo]й)?|[юu][гg])[ауеыauyei]?|[мm][аa][нnh][дd]([ауеыayueiи]([лl]([иi][сзc3щ])?[ауеыauyei])?|[оo][йi]|[аоao][вvwb][оo](ш|sh)[ь']?([e]?[кk][ауеayue])?|юк(ов|[ауи])?)|[мm][уuy][дd6]([яyаиоaiuo0].*?|[еe]?[нhn]([ьюия'uiya]|ей))|мля([тд]ь)?|лять|([нз]а|по)х|м[ао]л[ао]фь([яию]|[её]й))\\b").matcher(newMessage.toLowerCase()).find()
//                )) {
            ) {
                boolean isBadWord = gpt4o.isTextContainingBadWord(newMessage);


//                applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(), textAboutBadWord);

//                                if (gpt4oMini.isTextContainingBadWord(newMessage)) {
                log.debug("isBadWord: {}", isBadWord);
                if (isBadWord) {
                    String textAboutBadWord = gpt4o.TextContainingBadWord(newMessage);
//            if (message.contains("!timeout")) {
                    String id = event.getUser().getId();

                    String eventChannel = event.getChannel().getName();

                    if (event.getChannel().getName().equalsIgnoreCase(applicationContext.getBean(MainBuilderUtil.class).getMainChannelName())) {
                        log.debug("timeoutForMat: main channel branch");

                        UtilityCommandsMainChannel.timeoutUser(id, 600, "bad word bot");
//            twitchClient.getChat().sendMessage("maximuz666",  "@" + event.getUser().getName() + " Мат в чате запрещён!");
                        applicationContext.getBean(BotBuilderUtil.class).sendMessage(eventChannel, "@" + event.getUser().getName() + " " + textAboutBadWord);
                    } else if (event.getChannel().getName().equalsIgnoreCase(applicationContext.getBean(BotBuilderUtil.class).getTestChannelName())) {
                        log.debug("timeoutForMat: test channel branch");
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

    // tsyaMode state moved to ChannelContext; !tsya + nag migrated to commands/ (Phase 5).

    // changeTsyaMode (!tsya) migrated to commands/TsyaToggleCommand (Phase 5).

    // tellAboutTsyaMistake migrated to commands/TsyaMistakeCommand (Phase 5).

    // gptBotMode state moved to ChannelContext; !botreply + reply migrated to commands/ (Phase 5).

    // changeBotMode (!botreply) migrated to commands/BotReplyToggleCommand (Phase 5).

    // replyToMessage migrated to commands/BotReplyCommand (Phase 5).

    // rebootBotContext (!reboot) migrated to commands/RebootCommand (Phase 5).


    @EventSubscriber
    public void testGettingAIMToken(ChannelMessageEvent event) {
        String newMessage = event.getMessage().toLowerCase();
        newMessage = newMessage.replace(TwitchText.INVISIBLE_TAG, "");

        if ( (botProperties.isOwner(event.getUser().getName())) && (newMessage.toLowerCase().startsWith("!yagpt_test")) ) {
            applicationContext.getBean(BotBuilderUtil.class).sendMessage(event.getChannel().getName(),
                    "@" + event.getUser().getName() + " testing...");
//            yandexGPT.isTextContainingBadWord("кароче слушай сюда, мудень, я знаю, что ты говноед, из тебя хреновый писатель, жопный ты человек, да и вообще херовый пиздец блять");
            yandexGPT.isTextContainingBadWord("пиздец");
        }
    }
}
