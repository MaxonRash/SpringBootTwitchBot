package com.bot.springboottwitchbot.controllers;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@RestController
public class PredictionsRESTController {

    private static String lastExecutedPredictionStartTime = null;

    @GetMapping("/rest_predictions/resetLastPredictionTime")
    public String resetLastPredictionTime() {
        lastExecutedPredictionStartTime = null;
        System.out.println("Last prediction time has been reset");
        return "Last prediction time has been reset";
    }

    @GetMapping("/rest_predictions/showLastPredictionTime")
    public String showLastPredictionTime() {
        System.out.println("Last prediction time is: " + lastExecutedPredictionStartTime);
        return "Last prediction time is: " + lastExecutedPredictionStartTime;
    }

    @GetMapping("/rest_predictions")
    public String predictionsGetPage(@RequestParam(required = false) String makeNewPrediction,
                                     @RequestParam(required = false) String winPrediction,
                                     @RequestParam(required = false) String losePrediction,
                                     @RequestParam() String time) throws IOException, InterruptedException {
        if (makeNewPrediction == null && winPrediction == null && losePrediction == null) {
            System.err.println("No one of the required parameters");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No req param", new Exception("No req param"));
        }
        if (makeNewPrediction != null) {
            if (lastExecutedPredictionStartTime != null) {
                if (time.equals(lastExecutedPredictionStartTime)) {
                    System.err.println("This prediction was already executed earlier");
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This prediction was already executed earlier",
                            new Exception("Same prediction"));
                }
            }
            try {
                UtilityCommandsMainChannel.makeStandardPrediction();
            } catch (HttpClientErrorException.BadRequest e) {
                System.err.println("Prediction is already started. Time: " + time + " Waiting fow outcome now.");
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                .getMainChannelName(), "@happasc2 Кто-то (модеры MODS ) уже запустил ставку ResidentSleeper Ок, если она для игры, "
                        + "начавшейся в " + time + " Ждём исхода...");
                lastExecutedPredictionStartTime = time;
                return "Prediction event is already existing, it's ok if it is for the game started at: " + time + " Waiting for outcome now";
            }
            Thread.sleep(1000);
            lastExecutedPredictionStartTime = time;
            System.out.println("New Prediction Made, Game Started at: " + time);
            ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                    .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                            .getMainChannelName(), "@happasc2 Начата ставка для игры, начавшейся в " + time);
            return "New Prediction Made, Game Started at: " + time;
        }
        else if (winPrediction != null) {
            if (time.equals(lastExecutedPredictionStartTime)) {
                try {
                    UtilityCommandsMainChannel.winStandardPrediction();
                } catch (NullPointerException e) {
                    lastExecutedPredictionStartTime = null;
                    System.out.println("No opened predictions, someone closed it earlier. Time was: " + time);
                    ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                            .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                    .getMainChannelName(), "@happasc2 Нет открытых ставок. Кто-то (модеры MODS ) уже закрыл ставку для игры, "
                                    + "начавшейся в " + time + " Ждём начала новой игры...");
                    return "No opened predictions, someone closed it earlier. Time was: " + time;
                }
                Thread.sleep(1000);
                lastExecutedPredictionStartTime = null;
                System.out.println("Outcome for prediction for game started at: " + time + " is set to WON");
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                .getMainChannelName(), "@happasc2 Исход для игры, начавшейся в " + time + " - WIN (1-4)");
                return "Outcome for prediction for game started at: " + time + " is set to WON";
            }
        }
        else {
            if (time.equals(lastExecutedPredictionStartTime)) {
                try {
                    UtilityCommandsMainChannel.loseStandardPrediction();
                } catch (NullPointerException e) {
                    lastExecutedPredictionStartTime = null;
                    System.out.println("No opened predictions, someone closed it earlier. Time was: " + time);
                    ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                            .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                    .getMainChannelName(), "@happasc2 Нет открытых ставок. Кто-то (модеры MODS ) уже закрыл ставку для игры, "
                                    + "начавшейся в " + time + " Ждём начала новой игры...");
                    return "No opened predictions, someone closed it earlier. Time was: " + time;
                }
                Thread.sleep(1000);
                lastExecutedPredictionStartTime = null;
                System.out.println("Outcome for prediction for game started at: " + time + " is set to LOST");
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot()
                        .getChat().sendMessage(ApplicationContextProvider.getApplicationContext().getBean(MainBuilderUtil.class)
                                .getMainChannelName(), "@happasc2 Исход для игры, начавшейся в " + time + " - LOST (4-8)");
                return "Outcome for prediction for game started at: " + time + " is set to LOST";
            }
        }
        System.err.println("Nothing was done, but ok...");
        return "Nothing was done, but ok...";
    }

}
