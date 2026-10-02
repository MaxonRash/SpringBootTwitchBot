package com.bot.springboottwitchbot.commands;
import com.bot.springboottwitchbot.utilities.TwitchText;

import com.bot.springboottwitchbot.dto.support.UsersResponseToUserConverter;
import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.services.UsersService;
import com.bot.springboottwitchbot.timers.Global10secCDTimer;
import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * {@code !др ДД/ММ[/ГГГГ]} registers the caller's birthday (requires being a follower &gt; 6 months;
 * can't be changed once set), and {@code !др} shows it with a countdown. Migrated from
 * {@code UserDOBTest} (test) / {@code UserDOB} (main), which were identical except the test copy sent
 * register-path errors to the literal "maximuz666" — that is the test channel name, so {@link ChannelContext#send}
 * is equivalent on both channels. The follower check uses the main channel's endpoint on both (as before).
 */
@Component
public class BirthdayCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(BirthdayCommand.class);

    private final UsersService usersService;

    @Autowired
    public BirthdayCommand(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        String message = event.getMessage().toLowerCase().replace(TwitchText.INVISIBLE_TAG, "");
        return message.startsWith("!др ") || message.equals("!др");
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) throws Exception {
        String message = event.getMessage().toLowerCase().replace(TwitchText.INVISIBLE_TAG, "");
        String user = event.getUser().getName();

        if (message.startsWith("!др ")) {
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(message.split(" ")));
            arrayList.remove(TwitchText.INVISIBLE_TAG);
            if (arrayList.size() > 1) {
                User checkUser = usersService.findOne(user);
                if (checkUser == null) {
                    checkUser = UsersResponseToUserConverter.ConvertUserFromDTO(UtilityCommandsGlobal.getUserDTOByName(user));
                    checkUser.setFollowingSince(UtilityCommandsMainChannel.getFollowingSinceDate(
                            Integer.parseInt(Objects.requireNonNull(UtilityCommandsGlobal.getUserIdByName(user)))));
                    usersService.save(checkUser);
                }
                if (checkUser.getFollowingSince() == null) {
                    checkUser.setFollowingSince(UtilityCommandsMainChannel.getFollowingSinceDate(
                            Integer.parseInt(Objects.requireNonNull(UtilityCommandsGlobal.getUserIdByName(user)))));
                    usersService.save(checkUser);
                    if (checkUser.getFollowingSince() == null) {
                        if (Global10secCDTimer.getGlobal10secTimer() == null) {
                            ctx.send("@" + user + " нужно быть фолловером больше, чем 6 месяцев.");
                            Global10secCDTimer.setGlobal10secTimer();
                        }
                    }
                }
                if (checkUser.getFollowingSince() != null) {
                    if (UtilityDOB.CheckIfFollowIsMoreThan6Months(new Date(), checkUser.getFollowingSince())) {
                        if (checkUser.getDateOfBirth() == null) {
                            String originalDOB = arrayList.get(1);
                            String DOB;
                            Date DOBDate;
                            if (originalDOB.matches("(0[1-9]|[1-2]\\d|3[01])[/.-](1[0-2]|0[1-9])[/.-](19[6-9]\\d|20[0-1]\\d)")) {
                                originalDOB = originalDOB.replaceAll("\\.", "/");
                                originalDOB = originalDOB.replaceAll("-", "/");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                DOB = originalDOB;
                                DOBDate = sdf.parse(DOB);
                            } else if (originalDOB.matches("(0[1-9]|[1-2]\\d|3[01])[/.-](1[0-2]|0[1-9])")) {
                                originalDOB = originalDOB.replaceAll("\\.", "/");
                                originalDOB = originalDOB.replaceAll("-", "/");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                DOB = originalDOB + "/1900";
                                DOBDate = sdf.parse(DOB);
                            } else {
                                DOBDate = null;
                                if (Global10secCDTimer.getGlobal10secTimer() == null) {
                                    ctx.send("@" + user + " Формат даты должен быть таким: "
                                            + "день/месяц/год или день/месяц . С лидирующими нолями в дне и месяце");
                                    Global10secCDTimer.setGlobal10secTimer();
                                }
                            }
                            if (DOBDate != null) {
                                checkUser.setDateOfBirth(DOBDate);
                                usersService.save(checkUser);
                                int dice = (int) (Math.random() * 2) + 1;
                                if (dice == 1) {
                                    ctx.send("@" + user + " pepeNoted");
                                } else if (dice == 2) {
                                    ctx.send("@" + user + " HmmNotes");
                                }
                            }
                        } else {
                            if (Global10secCDTimer.getGlobal10secTimer() == null) {
                                ctx.send("@" + user + " нельзя менять ДР DansGame");
                                Global10secCDTimer.setGlobal10secTimer();
                            }
                        }
                    } else {
                        if (Global10secCDTimer.getGlobal10secTimer() == null) {
                            ctx.send("@" + user + " нужно быть фолловером больше, чем 6 месяцев.");
                            Global10secCDTimer.setGlobal10secTimer();
                        }
                    }
                }
            }
        } else if (message.equals("!др")) {
            if (Global10secCDTimer.getGlobal10secTimer() == null) {
                Locale ruLocale = new Locale("ru", "RU");
                SimpleDateFormat simpleDateFormatWithoutYear = new SimpleDateFormat("dd MMMM", ruLocale);
                SimpleDateFormat simpleDateFormatWithYear = new SimpleDateFormat("dd MMMM yyyy", ruLocale);
                User foundUser = usersService.findOne(user);
                if (foundUser != null) {
                    String messageToDOB = "";
                    long daysBetween = 0;
                    if (foundUser.getDateOfBirth() != null) {
                        Date todayDate = new Date();
                        LocalDate currentDate = LocalDate.ofInstant(todayDate.toInstant(), ZoneId.systemDefault()).withYear(1900);
                        Date userDateFromDB = foundUser.getDateOfBirth();
                        LocalDate userDate = Instant.ofEpochMilli(userDateFromDB.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate userDateUpdated;
                        if (userDate.getMonth().getValue() < currentDate.getMonth().getValue()) {
                            userDateUpdated = userDate.withYear(1901);
                        } else {
                            userDateUpdated = userDate.withYear(1900);
                        }
                        daysBetween = DAYS.between(currentDate, userDateUpdated);
                        messageToDOB = ", ещё " + daysBetween + " дней!";
                        if (String.valueOf(daysBetween).startsWith("-")) {
                            messageToDOB = ", был всего " + String.valueOf(daysBetween).substring(1) + " дней назад Kappa";
                        }
                        if (daysBetween == 0) {
                            messageToDOB = ", это же сегодня! Pog";
                        }
                    }
                    if (foundUser.getDateOfBirth() != null && foundUser.getDateOfBirth().getYear() == 0) {
                        ctx.send("@" + user + " " + simpleDateFormatWithoutYear.format(foundUser.getDateOfBirth()) + messageToDOB);
                    } else if (foundUser.getDateOfBirth() != null && foundUser.getDateOfBirth().getYear() != 0) {
                        ctx.send("@" + user + " " + simpleDateFormatWithYear.format(foundUser.getDateOfBirth()) + messageToDOB);
                    } else {
                        ctx.send("@" + user + " День рождения не установлен");
                    }
                } else {
                    if (Global10secCDTimer.getGlobal10secTimer() == null) {
                        ctx.send("@" + user + " сначала добавь командой !др день/месяц/год или день/месяц . С лидирующими нолями в дне и месяце");
                        log.debug("такого юзера нет: {}", user);
                        Global10secCDTimer.setGlobal10secTimer();
                    }
                }
                Global10secCDTimer.setGlobal10secTimer();
            }
        }
    }
}
