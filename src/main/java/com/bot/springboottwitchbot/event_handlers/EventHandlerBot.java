package com.bot.springboottwitchbot.event_handlers;


import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.DTOs.utilities_for_DTOs.GetUserDTOToUserConverter;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.services.UsersService;
import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.bot.springboottwitchbot.timers.GlobalDuelTimer;
import com.bot.springboottwitchbot.timers.GlobalKillTimer;
import com.bot.springboottwitchbot.timers.GlobalRouletteTimer;
import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.bot.springboottwitchbot.utilities.UtilityCommandsTestChannel;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class EventHandlerBot {
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    @Autowired
    UsersService usersService;
    private String firstDuelName = null;
    private String secondDuelName = null;

    @EventSubscriber
    public void printChannelMessage(ChannelMessageEvent event) {
        System.out.println("[" + event.getChannel().getName() + "]" + "{MyEventHandlerBot}" + "["+event.getPermissions().toString()+"] " + event.getUser().getName() + ": " + event.getMessage());
    }

    @EventSubscriber
    public void duelCommand(ChannelMessageEvent event) throws IOException {
        String newMessage = event.getMessage().toLowerCase();

        String[] array = newMessage.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove("\udb40\udc00");

        if (newMessage.toLowerCase().startsWith("!duel") && (arrayList.size() > 1)) {

            if (/*newMessage.toLowerCase().startsWith("!duel") && */(GlobalDuelTimer.duelTimerToAccept != null) && (GlobalDuelTimer.duelCooldownTimer == null)) {
//            System.out.println("this works");
                String eventChannel = event.getChannel().getName();

                String secondDuelName = arrayList.get(1).toLowerCase();
                if (secondDuelName.startsWith("@")) {
                    secondDuelName = secondDuelName.substring(1);
                }
                if (secondDuelName.equals(this.firstDuelName) && (event.getUser().getName().equals(this.secondDuelName))) {
                    GlobalDuelTimer.duelTimerToAccept.cancel();
                    GlobalDuelTimer.duelTimerToAccept = null;
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(eventChannel,
                            "@" + this.firstDuelName + " и " + "@" + this.secondDuelName + " подходят друг к другу...");
                    GlobalDuelTimer.duelCooldownTimer = new Timer("duelCoolDownTimer");
                    //TODO change delay to 60
                    long delay = 30 * 1000L;
                    GlobalDuelTimer.duelCooldownTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GlobalDuelTimer.duelCooldownTimer = null;
                        }
                    }, 30 * 1000L); // delay of duel cooldown
                    GlobalDuelTimer.setDuelCoolDownTimerLeft(delay / 1000);
                    if (UtilityCommandsTestChannel.getModeratorsList().contains(this.secondDuelName)) {
                        applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstDuelName + " он же модир FailFish");
                        UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName("happasc2"), 10, "no");
                    }
                    //TODO duel logic
                }

                this.firstDuelName = null;
                this.secondDuelName = null;
            }

            //snachala rabotaet 2 else, perviy uje na otvet

            else if (/*newMessage.toLowerCase().startsWith("!duel") && */(GlobalDuelTimer.duelTimerToAccept == null) && (GlobalDuelTimer.duelCooldownTimer == null)) {
//            String[] array = newMessage.split(" ");
                String secondDuelName = arrayList.get(1).toLowerCase();
                if (secondDuelName.startsWith("@")) {
                    secondDuelName = secondDuelName.substring(1);
                }
                String eventChannel = event.getChannel().getName();
                String firstDuelName = event.getUser().getName();
                if (firstDuelName.equals(secondDuelName)) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(eventChannel,"@" + firstDuelName + " выстрелил себе в лицо FailFish");
                    GlobalDuelTimer.duelCooldownTimer = new Timer("duelCoolDownTimer");
                    //TODO change delay to 600
                    long delay = 30 * 1000L;
                    GlobalDuelTimer.duelCooldownTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GlobalDuelTimer.duelCooldownTimer = null;
                        }
                    }, delay); // delay of duel cooldown
                    GlobalDuelTimer.setDuelCoolDownTimerLeft(delay / 1000);
                }
                else {
                    this.firstDuelName = firstDuelName.toLowerCase();
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(eventChannel, "@" + firstDuelName + " вызывает " + "@" + secondDuelName + " на дуэль!");
                    System.out.println(arrayList);
                    String finalSecondDuelName = secondDuelName;
                    this.secondDuelName = finalSecondDuelName;
//            System.out.println("second name = " + this.secondDuelName + "// first name = " + this.firstDuelName);
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(eventChannel, "@" + finalSecondDuelName + " намочил штанишки 4Head");
                            GlobalDuelTimer.duelTimerToAccept = null;
                        }
                    };
                    if (GlobalDuelTimer.duelTimerToAccept == null) {
                        GlobalDuelTimer.duelTimerToAccept = new Timer("duelTimer");
                        //TODO change delay to 60
                        long delay = 5 * 1000L; // delay for "namochil shtanishki"
                        GlobalDuelTimer.duelTimerToAccept.schedule(timerTask, delay);
                    }
                }
            }
            else if (Global10secCDTimer.getGlobal10secTimer() == null) {
                if (GlobalDuelTimer.duelCoolDownTimerLeft != 0) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName()
                            + " вызвать кого-либо на дуэль можно через " + GlobalDuelTimer.duelCoolDownTimerLeft + " секунд");
                    Global10secCDTimer.setGlobal10secTimer();
                }
                else {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName()
                            + " можно вызвать кого-то на дуэль Kappa");
                    Global10secCDTimer.setGlobal10secTimer();
                }
            }
        }
        else if (newMessage.toLowerCase().startsWith("!duel") && (arrayList.size() == 1) && (Global10secCDTimer.getGlobal10secTimer() == null)){
            if (GlobalDuelTimer.duelCoolDownTimerLeft != 0) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName()
                        + " вызвать кого-либо на дуэль можно через " + GlobalDuelTimer.duelCoolDownTimerLeft + " секунд");
                Global10secCDTimer.setGlobal10secTimer();
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName()
                        + " можно вызвать кого-то на дуэль Kappa");
                Global10secCDTimer.setGlobal10secTimer();
            }
        }
    }

    @EventSubscriber
    public void killCommand(ChannelMessageEvent event) throws InterruptedException, IOException {
        String newMessage = event.getMessage().toLowerCase();

        String[] array = newMessage.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove("\udb40\udc00");

        if (newMessage.toLowerCase().startsWith("!kill") && (arrayList.size() > 1) && (GlobalKillTimer.killCooldownTimer == null)) {
            String firstNick = event.getUser().getName().toLowerCase();
            String secondNick = arrayList.get(1).toLowerCase();
            if (secondNick.startsWith("@")) {
                secondNick = secondNick.substring(1);
            }

            String commandPermissionString = event.getPermissions().toString();
            commandPermissionString = commandPermissionString.substring(1);
            commandPermissionString = commandPermissionString.substring(0, commandPermissionString.lastIndexOf("]"));
            ArrayList<String> commandPermissionList = new ArrayList<>(Arrays.asList(commandPermissionString.split(",")));
            ArrayList<String> requiredPermissionList = new ArrayList<>(Arrays.asList("PARTNER, SUBSCRIBER, FOUNDER, SUBGIFTER, VIP, MODERATOR, BROADCASTER".split(",")));
            commandPermissionList.retainAll(requiredPermissionList);

            if (!commandPermissionList.isEmpty()) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " подходит к @"
                        + secondNick + " ...");
                Thread.sleep(3000);

                //TODO change test timeouts to real ones

                int dice = (int) (Math.random() * 17) + 1;
                if (dice == 1) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " проходит мимо " + "@" + secondNick + " peepoLeaveFinger");
                }
                else if (dice == 2) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " отстреливает " + "@" + secondNick + " лицо WutFace");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 3) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " зачем-то обнимает " + "@" + secondNick + " ヽ༼ຈل͜ຈ༽ﾉ");
                }
                else if (dice == 4) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " джошукеном ─=≡Σ happaDjosh ))");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 5) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " хадукеном つಠ益ಠ༽つ ─=≡ΣO))");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 6) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " со снайперки ▄︻̿┻̿═━一");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 7) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " с автомата <,︻╦╤─ ҉ — —");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 8) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " заколол " + "@" + secondNick + " трезубцами Ψ༼ຈل͜ຈ༽Ψ");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 9) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " с помощью магии ( ͡ ͠° ͟ʖ ͡° )つ──☆*:・ﾟ");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 10) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " катаной ▬▬ι═══════ﺤ");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 11) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " с локтя ༼ᕗ•̀_•́༽ᕗ");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 12) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " убивает " + "@" + secondNick + " силой русского репа ヾ(⌐■_■)ノ♪");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 13) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " кидает в " + "@" + secondNick + " стол (ノಠ益ಠ)ノ彡┻━┻");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(secondNick), 10, "no");
                }
                else if (dice == 14 || dice == 15 || dice == 16 || dice == 17) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " зачем-то обнимает " + "@" + secondNick + " ヽ༼ຈل͜ຈ༽ﾉ");
                }

                GlobalKillTimer.killCooldownTimer = new Timer("killCoolDownTimer");
                long delay = 30 * 1000L;
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
        }
        else if (
                ((newMessage.toLowerCase().startsWith("!kill")) && (arrayList.size() == 1) && (Global10secCDTimer.getGlobal10secTimer() == null))
                        || ((newMessage.toLowerCase().startsWith("!kill")) && (arrayList.size() > 1) && (GlobalKillTimer.killCooldownTimer != null) && (Global10secCDTimer.getGlobal10secTimer() == null))
        ) {
            if (GlobalKillTimer.killCoolDownTimerLeft != 0) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName()
                        + " Подойти к кому-либо можно будет через " + GlobalKillTimer.killCoolDownTimerLeft + " секунд OpieOP");
                Global10secCDTimer.setGlobal10secTimer();
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName()
                        + " !kill готов happaDjosh =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿");
                Global10secCDTimer.setGlobal10secTimer();
            }
        }
    }

    @EventSubscriber
    public void resetKillCommand(ChannelMessageEvent event) {
        String newMessage = event.getMessage().toLowerCase();

        String[] array = newMessage.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove("\udb40\udc00");

        if (newMessage.toLowerCase().startsWith("!reset") && (arrayList.size() > 1) && (arrayList.get(1).equals("kill"))) {
            String commandPermissionString = event.getPermissions().toString();
            commandPermissionString = commandPermissionString.substring(1);
            commandPermissionString = commandPermissionString.substring(0, commandPermissionString.lastIndexOf("]"));
            ArrayList<String> commandPermissionList = new ArrayList<>(Arrays.asList(commandPermissionString.split(",")));
            ArrayList<String> requiredPermissionList = new ArrayList<>(Arrays.asList("VIP, MODERATOR, BROADCASTER".split(",")));
            commandPermissionList.retainAll(requiredPermissionList);

            if (!commandPermissionList.isEmpty()) {
                GlobalKillTimer.resetKill();
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "!kill готов happaDjosh =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿");
            }
            else if (Global10secCDTimer.getGlobal10secTimer() == null) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                        " Должна быть одна из этих ролей: " + requiredPermissionList);
            }
        }
    }

    private ArrayList<String> russianRoulettePlayers = null;
    @EventSubscriber
    public void russianRouletteCommand(ChannelMessageEvent event) throws InterruptedException, IOException {
        String newMessage = event.getMessage().toLowerCase();
        if (newMessage.contains("monkas") && russianRoulettePlayers == null && GlobalRouletteTimer.rouletteCooldownTimer == null) {
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                    " инициировал сходку клуба любителей пострелять monkaSHAKE они почему-то пишут monkaS в чат");
            russianRoulettePlayers = new ArrayList<>(Collections.singleton(event.getUser().getName()));

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "сходка клуба любителей пострелять не состоялась FeelsBadMan");
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

            System.out.println(russianRoulettePlayers);
        }
        else if (newMessage.contains("monkas") && russianRoulettePlayers != null && GlobalRouletteTimer.rouletteCooldownTimer == null) {
            if (russianRoulettePlayers.size() < 6) {
                //TODO uncomment after tests
//                if (!russianRoulettePlayers.contains(event.getUser().getName())) {
                russianRoulettePlayers.add(event.getUser().getName());
                System.out.println(russianRoulettePlayers);
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

                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), allRoulettePlayers.toString() +
                        " стреляют себе в лица, на всех один патрон monkaMEGA ...");
                Thread.sleep(3000);

                int dice = (int) (Math.random() * 6);
                int dice2 = (int) (Math.random()* 2);
                String deadRoulettePlayer = russianRoulettePlayers.get(dice);
                if (dice2 == 0) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + deadRoulettePlayer + " happaF");
                    UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(deadRoulettePlayer), 10, "shodka");
                }
                else {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + deadRoulettePlayer +
                            "'у повезло и он помер не сразу, есть 5 сек...");
                    Thread timeoutIn5sec = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + deadRoulettePlayer + " happaF");
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

    @EventSubscriber
    public void russianRouletteTimeLeft(ChannelMessageEvent event) {
        String newMessage = event.getMessage().toLowerCase();
        if (newMessage.contains("!сходка") && GlobalRouletteTimer.rouletteCooldownTimer == null && Global10secCDTimer.getGlobal10secTimer() == null) {
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "ну довай PepegaAim");
            Global10secCDTimer.setGlobal10secTimer();
        }
        else if (newMessage.contains("!сходка") && GlobalRouletteTimer.rouletteCooldownTimer != null && Global10secCDTimer.getGlobal10secTimer() == null) {
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                    " начать сходку можно через " + GlobalRouletteTimer.rouletteCoolDownTimerLeft + " happaUHW");
            Global10secCDTimer.setGlobal10secTimer();
        }
    }

    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    // Spam commands
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________
    //_______________________________________________________________________________________________________________________________

    //TODO change timeouts from test to real ones
    @EventSubscriber
    public void spamMessagesCommand(ChannelMessageEvent event) throws IOException, InterruptedException {
        String newMessage = event.getMessage().toLowerCase();

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!ник") ||
                newMessage.toLowerCase().startsWith("!nick"))) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "Оцениваю " + "@" + firstNick + " ...");
            Thread.sleep(3000);

            int dice = (int) (Math.random() * 10) + 1;
            if (dice == 1) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " твой ник калич 4Head 1/10, зобаню даж");
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "nick");
            }
            else if (dice == 2) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ник ниачом 2/10, даж без смайла");
            }
            else if (dice == 3) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ну такой себе ник 3/10 DansGame");
            }
            else if (dice == 4) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " скучный ник 4/10 ResidentSleeper");
            }
            else if (dice == 5) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " твердая питёрка 5/10  billyWink");
            }
            else if (dice == 6) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " нормальный такой ник SeemsGood 6/10");
            }
            else if (dice == 7) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ну, конечно, не Александр, но 7/10 , not bad ChadYes");
            }
            else if (dice == 8) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ну, конечно, не Максон, но 8/10 PogChamp");
            }
            else if (dice == 9) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " хрена себе никчанский happaWut 9/10");
            }
            else if (dice == 10) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " твой ник просто прекрасен cageGASM держи happa100 и бан, чтоб другим не обидно было happaShutup");
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "nick");
            }
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!слот") ||
                newMessage.toLowerCase().startsWith("!slot"))) {
            String firstNick = event.getUser().getName();

            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("mericCat", "happaPepe", "happaWut", "happaPride", "happa100", "PepeLaugh", "EZ"));
            int dice1 = (int) (Math.random() * 7);
            int dice2 = (int) (Math.random() * 7);
            int dice3 = (int) (Math.random() * 7);

            String firstSlot = arrayList.get(dice1);
            String secondSlot = arrayList.get(dice2);
            String thirdSlot = arrayList.get(dice3);

            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), firstSlot + " " + secondSlot + " " + thirdSlot);

            if (firstSlot.equals("mericCat") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " Вы выиграли 1$ haHAA");
            }
            if (firstSlot.equals("happaPepe") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " Вы выиграли 100$");
            }
            if (firstSlot.equals("happaWut") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " Вы выиграли 1000$!");
            }
            if (firstSlot.equals("happaPride") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " Вы выиграли 69$  gachiBASS");
            }
            if (firstSlot.equals("happa100") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " Вы выиграли 100 рублей!!! Два трека (условно)!");
            }
            if (firstSlot.equals("PepeLaugh") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " Вы выиграли 100000$!!!");
            }
            if (firstSlot.equals("EZ") && firstSlot.equals(secondSlot) && firstSlot.equals(thirdSlot)) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick + " Вы выиграли 1000000$ и разорили казино!!!");
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 300, "Casino");
            }

//            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!drops") ||
                newMessage.toLowerCase().startsWith("!дропс"))) {
            String firstNick = event.getUser().getName();

            int dice = (int) (Math.random() * 6) + 1;
            if (dice == 1) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ОГО! Ты получаешь целое... НИ ХУ ХРЫ!");
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "drops");
            }
            else if (dice == 2) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ОГО! Да тебе выпало целое НИ ФИ ГА! жоска!");
            }
            else if (dice == 3) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ОГО! У тебя теперь есть НИ ЧЕР ТА! круто!");
            }
            else if (dice == 4) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ОГО! Да это же ШИШ С МАСЛОМ! забирай! и таймач прихвати!");
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "drops");
            }
            else if (dice == 5) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ОГО! Да это же ГОЛЯК! грац!");
            }
            else if (dice == 6) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " ОГО! Тут всего-то чуть меньше, чем НИ ЧТО! Вау!");
            }
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!в очередь"))) {
            String firstNick = event.getUser().getName();

            int dice = (int) (Math.random() * 1000) + 1;
            if (dice == 1) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " Ваше место в очереди... Вы следующий! PogChamp скринь! SHTO");
            } else if (dice == 1000) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " Ваше место в очереди... Вы последний! АХАХА maaaaan");
            } else {
                if (dice < 100) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " Ваше место в очереди... " + dice + ", осталось немного peepoComfy");
                }
                else if (dice > 900) {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " Ваше место в очереди... " + dice + ", это вооон за тем челом PepePoint");
                }
                else {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                            + " Ваше место в очереди... " + dice + " Tssk");
                }
            }
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!мут мне на"))) {
            String firstNick = event.getUser().getName();

            String[] array = newMessage.split(" ");
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
            arrayList.remove("\udb40\udc00");

            if (UtilityCommandsTestChannel.getModeratorsList().contains(firstNick)){
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " А жареных гвоздей не хочешь? PETTHEMODS");
            }
            else {
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), Integer.parseInt(arrayList.get(3)), "muteAsked");
            }
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().contains("лфзза"))) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                    + " Kappa");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().contains("4руфв"))) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                    + " 4Head");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!паук"))) {
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "ВИКТОР - /\\/\\╭( ͡° ͡° ͜ʖ ͡° ͡°)╮/\\╱\\");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().contains("амиго"))) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                    + " Вот не надо на Амиго гнать, вполне обычный браузер. Репутацию сломал потому что вместе с вирусами ставился. С офф сайта он нормальный");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!джошукен"))) {
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "༼ つ happaDans ༽つ ─=≡Σ happaDjosh ))");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!хадукен"))) {
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "༼ つಠ益ಠ༽つ ─=≡ΣO))");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!пасты"))) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                    + " Только не балуйся Kapp https://docs.google.com/document/d/1S8tudkuBmTQjoLZcIcTPDFJ4AZqnpi7pof3FlhevktI/edit?usp=sharing");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if ((Global10secCDTimer.getGlobal10secTimer() == null) && (newMessage.toLowerCase().startsWith("!судоку"))) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                    + " https://sudoku.com/ 4Head");
            Global10secCDTimer.setGlobal10secTimer();
        }

        if (newMessage.toLowerCase().startsWith("!с локтя") && (event.getUser().getName().equalsIgnoreCase("maximuz666"))) {
            String[] array = newMessage.split(" ");
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
            arrayList.remove("\udb40\udc00");

            UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(arrayList.get(2)), 30, "sLoktya");
        }
    }

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
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + event.getUser().getName() +
                        " Стыдно, товарищ!");
            }
            else if (!commandPermissionList2.isEmpty()) {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " бан 10 мин за гуся и прочую ересь. Одумайся, уважаемый на канале чел!");
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "kaban_i_gus");
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
                        + " бан на 11 дней за гуся и прочую ересь.");
                //TODO change to 999999
                UtilityCommandsTestChannel.timeoutUserTest(UtilityCommandsGlobal.getUserIdByName(firstNick), 10, "kaban_i_gus");
            }
        }
        if (newMessage.contains("Ỏ")) {
            String firstNick = event.getUser().getName();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), "@" + firstNick
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
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage("maximuz666", "new Bot: That was a bad word");
        }
    }

    @EventSubscriber
    public void getModeratorsTest(ChannelMessageEvent event) {
        String message = event.getMessage();
        if (message.contains("!mods")) {
            ArrayList<String> moderatorsList = UtilityCommandsTestChannel.getModeratorsList();
            String fullModsList = moderatorsList.toString();
            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), fullModsList);
        }
    }
    @EventSubscriber
    public void getUserIdTest(ChannelMessageEvent event) {
        String message = event.getMessage();
        if (message.contains("!test")) {
            try {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot()
                                .getChat().sendMessage("maximuz666", "new bot: " + UtilityCommandsGlobal.getUserIdByName("maximuz666"));
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
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage("maximuz666", date.toString());
            }
            else {
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage("maximuz666", "TI NE FOLLOWER");
            }
        }
    }

    @EventSubscriber
    public void UserDOBTest(ChannelMessageEvent event) throws IOException, ParseException, InterruptedException {
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("!др")) {
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(message.split(" ")));
            arrayList.remove("\udb40\udc00");
            if (arrayList.size() > 1) {
                String DOB = arrayList.get(1) + "/2000";
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date DOBDate = sdf.parse(DOB);
                User user = GetUserDTOToUserConverter.ConvertUserFromDTO(UtilityCommandsGlobal
                        .getUserDTOByName(event.getUser().getName()));
                user.setDateOfBirth(DOBDate);

                usersService.save(user);
            }
            else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
                User user = usersService.findOne(event.getUser().getName());
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage( event.getChannel().getName(), simpleDateFormat.format(user.getDateOfBirth()));
            }
        }
    }

    @EventSubscriber
    public void timeoutHappaTest(ChannelMessageEvent event) {
        String message = event.getMessage();
        try {
            if (message.contains("!emotetest")) {
                UtilityCommandsTestChannel.emoteOnlyMode(true);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
//
//    @EventSubscriber
//    public void duelCooldownTest(ChannelMessageEvent event) {
//        String message = event.getMessage();
//        if (message.contains("!cooldownduel")) {
//            applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(event.getChannel().getName(), String.valueOf(GlobalDuelTimer.duelCoolDownTimerLeft));
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
@EventSubscriber
public void timeouteForMat(ChannelMessageEvent event) {
    String newMessage = event.getMessage();
//        String newMessage = message.replaceAll("\\s", "").toLowerCase();
    newMessage = newMessage.replaceAll("(.)\\1+", "$1");
//        twitchClient.getChat().sendMessage("maximuz666", "request sent: " + newMessage);
    try {
//            if (StringUtils.containsIgnoreCase(message, "бля")) {
        if (
                ( (newMessage.toLowerCase().contains("мудила")) || (newMessage.toLowerCase().contains("мудак")) || (newMessage.toLowerCase().contains("мудень")) || (newMessage.toLowerCase().contains("мудозвон")) ||
                        (newMessage.toLowerCase().contains("huecruch")) || (newMessage.toLowerCase().contains("дебил")) )
        ) {}
        else if (
                (Pattern.compile("[.]*ахую[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    /*Pattern.compile("^хуй[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*хуй[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("^х[ую[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*хую[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*похуй[.])*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*нахуй[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*хуя[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*хуи[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||

                    Pattern.compile("[.]*хуев[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*хуё[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*охуе[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||

                    Pattern.compile("[.]*пизд[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("^бля[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*бля[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*бляд[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*блеат[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("[.]*блеят[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                    Pattern.compile("\\s+бля", Pattern.CASE_INSENSITIVE).matcher(newMessage).find()*/

                        Pattern.compile("[.]*ахуе[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                        Pattern.compile("[.]*прихуе[.]*".toLowerCase()).matcher(newMessage.toLowerCase()).find() ||
                        Pattern.compile("(?iu)\\b(([уyu]|[нзnz3][аa]|(хитро|не)?[вvwb][зz3]?[ыьъi]|[сsc][ьъ']|(и|[рpr][аa4])[зсzs]ъ?|([оo0][тбtb6]|[пp][оo0][дd9])[ьъ']?|(.\\B)+?[оаеиeo])?-?([еёe][бb6](?!о[рй])|и[пб][ае][тц]).*?|([нn][иеаaie]|([дпdp]|[вv][еe3][рpr][тt])[оo0]|[рpr][аa][зсzc3]|[з3z]?[аa]|с(ме)?|[оo0]([тt]|дно)?|апч)?-?[хxh][уuy]([яйиеёюuie]|ли(?!ган)).*?|([вvw][зы3z]|(три|два|четыре)жды|(н|[сc][уuy][кk])[аa])?-?[бb6][лl]([яy](?!(х|ш[кн]|мб)[ауеыио]).*?|[еэe][дтdt][ь']?)|([рp][аa][сзc3z]|[знzn][аa]|[соsc]|[вv][ыi]?|[пp]([еe][рpr][еe]|[рrp][оиioеe]|[оo0][дd])|и[зс]ъ?|[аоao][тt])?[пpn][иеёieu][зz3][дd9].*?|([зz3][аa])?[пp][иеieu][дd][аоеaoe]?[рrp](ну.*?|[оаoa][мm]|([аa][сcs])?([иiu]([лl][иiu])?[нщктлtlsn]ь?)?|([оo](ч[еиei])?|[аa][сcs])?[кk]([оo]й)?|[юu][гg])[ауеыauyei]?|[мm][аa][нnh][дd]([ауеыayueiи]([лl]([иi][сзc3щ])?[ауеыauyei])?|[оo][йi]|[аоao][вvwb][оo](ш|sh)[ь']?([e]?[кk][ауеayue])?|юк(ов|[ауи])?)|[мm][уuy][дd6]([яyаиоaiuo0].*?|[еe]?[нhn]([ьюия'uiya]|ей))|мля([тд]ь)?|лять|([нз]а|по)х|м[ао]л[ао]фь([яию]|[её]й))\\b").matcher(newMessage.toLowerCase()).find()
                )) {
//            if (message.contains("!timeout")) {
            String id = event.getUser().getId();

            String eventChannel = event.getChannel().getName();

            if (event.getChannel().getName().equalsIgnoreCase("happasc2")) {

                UtilityCommandsMainChannel.timeoutUser(id, 600, "bad word bot");
//            twitchClient.getChat().sendMessage("maximuz666",  "@" + event.getUser().getName() + " Мат в чате запрещён!");
                applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(eventChannel, "@" + event.getUser().getName() + " Мат в чате запрещён!");
            } else if (event.getChannel().getName().equalsIgnoreCase("maximuz666")) {
                try {
                    applicationContext.getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().sendMessage(eventChannel, "@" + event.getUser().getName() + " Мат в чате запрещён!");
                    UtilityCommandsTestChannel.timeoutUserTest(id, 5, "bad word bot");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
}


}
