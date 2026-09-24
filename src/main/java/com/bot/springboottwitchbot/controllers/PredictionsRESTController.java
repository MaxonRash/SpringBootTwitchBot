package com.bot.springboottwitchbot.controllers;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@RestController
public class PredictionsRESTController {
    private static final Logger log = LoggerFactory.getLogger(PredictionsRESTController.class);

    @Qualifier("webApplicationContext")
    @Autowired
    ApplicationContext applicationContext;

    private static String lastExecutedPredictionStartTime = null;

    @GetMapping("/rest_predictions/resetLastPredictionTime")
    public String resetLastPredictionTime() {
        PredictionsRESTController.lastExecutedPredictionStartTime = null;
        log.info("Last prediction time has been reset");
        return "Last prediction time has been reset";
    }

    @GetMapping("/rest_predictions/showLastPredictionTime")
    public String showLastPredictionTime() {
        log.info("Last prediction time is: {}", lastExecutedPredictionStartTime);
        return "Last prediction time is: " + lastExecutedPredictionStartTime;
    }

    @GetMapping("/rest_predictions")
    public String predictionsGetPage(@RequestParam(required = false) String makeNewPrediction,
                                     @RequestParam(required = false) String winPrediction,
                                     @RequestParam(required = false) String losePrediction,
                                     @RequestParam() String time) throws IOException, InterruptedException {
        if (makeNewPrediction == null && winPrediction == null && losePrediction == null) {
            log.warn("No one of the required parameters");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No req param", new Exception("No req param"));
        }
        if (makeNewPrediction != null) {
            if (lastExecutedPredictionStartTime != null) {
                if (time.equals(lastExecutedPredictionStartTime)) {
                    log.warn("This prediction was already executed earlier");
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This prediction was already executed earlier",
                            new Exception("Same prediction"));
                }
            }
            try {
                UtilityCommandsMainChannel.makeStandardPrediction();
            } catch (HttpClientErrorException.BadRequest e) {
                log.warn("Prediction is already started. Time: {} Waiting fow outcome now.", time);
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                .getMainChannelName(), "@" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelName()
                                + " Кто-то (модеры MODS ) уже запустил ставку ResidentSleeper Ок, если она для игры, "
                        + "начавшейся в " + time + " Ждём исхода...");
                lastExecutedPredictionStartTime = time;
                return "Prediction event is already existing, it's ok if it is for the game started at: " + time + " Waiting for outcome now";
            }
            Thread.sleep(1000);
            lastExecutedPredictionStartTime = time;
            log.info("New Prediction Made, Game Started at: {}", time);
            ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                    .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                            .getMainChannelName(), "@" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelName()
                            + " Начата ставка для игры, начавшейся в " + time);
            return "New Prediction Made, Game Started at: " + time;
        }
        else if (winPrediction != null) {
            if (time.equals(lastExecutedPredictionStartTime)) {
                try {
                    UtilityCommandsMainChannel.winStandardPrediction();
                } catch (NullPointerException e) {
                    lastExecutedPredictionStartTime = null;
                    log.info("No opened predictions, someone closed it earlier. Time was: {}", time);
                    ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                            .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                    .getMainChannelName(), "@" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelName()
                                    + " Нет открытых ставок. Кто-то (модеры MODS ) уже закрыл ставку для игры, "
                                    + "начавшейся в " + time + " Ждём начала новой игры...");
                    return "No opened predictions, someone closed it earlier. Time was: " + time;
                }
                Thread.sleep(1000);
                lastExecutedPredictionStartTime = null;
                log.info("Outcome for prediction for game started at: {} is set to WON", time);
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                .getMainChannelName(),"@" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelName()
                                + " Исход для игры, начавшейся в " + time + " - WIN (1-4)");
                return "Outcome for prediction for game started at: " + time + " is set to WON";
            }
        }
        else {
            if (time.equals(lastExecutedPredictionStartTime)) {
                try {
                    UtilityCommandsMainChannel.loseStandardPrediction();
                } catch (NullPointerException e) {
                    lastExecutedPredictionStartTime = null;
                    log.info("No opened predictions, someone closed it earlier. Time was: {}", time);
                    ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                            .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                    .getMainChannelName(), "@" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelName()
                                    + " Нет открытых ставок. Кто-то (модеры MODS ) уже закрыл ставку для игры, "
                                    + "начавшейся в " + time + " Ждём начала новой игры...");
                    return "No opened predictions, someone closed it earlier. Time was: " + time;
                }
                Thread.sleep(1000);
                lastExecutedPredictionStartTime = null;
                log.info("Outcome for prediction for game started at: {} is set to LOST", time);
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                .getMainChannelName(), "@" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelName()
                                + " Исход для игры, начавшейся в " + time + " - LOST (5-8)");
                return "Outcome for prediction for game started at: " + time + " is set to LOST";
            }
        }
        log.warn("Nothing was done, but ok...");
        return "Nothing was done, but ok...";
    }

}
