package com.bot.springboottwitchbot.controllers;

import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/rest_predictions")
    public String predictionsGetPage(@RequestParam(required = false) String makeNewPrediction,
                                     @RequestParam(required = false) String winPrediction,
                                     @RequestParam(required = false) String losePrediction,
                                     @RequestParam() String time) throws IOException {
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
            UtilityCommandsMainChannel.makeStandardPrediction();
            lastExecutedPredictionStartTime = time;
            System.out.println("New Prediction Made, Game Started at: " + time);
            return "New Prediction Made, Game Started at: " + time;
        }
        else if (winPrediction != null) {
            if (time.equals(lastExecutedPredictionStartTime)) {
                UtilityCommandsMainChannel.winStandardPrediction();
                lastExecutedPredictionStartTime = null;
                System.out.println("Outcome for prediction for game started at: " + time + " is set to WON");
                return "Outcome for prediction for game started at: " + time + " is set to WON";
            }
        }
        else {
            if (time.equals(lastExecutedPredictionStartTime)) {
                UtilityCommandsMainChannel.loseStandardPrediction();
                lastExecutedPredictionStartTime = null;
                System.out.println("Outcome for prediction for game started at: " + time + " is set to LOST");
                return "Outcome for prediction for game started at: " + time + " is set to LOST";
            }
        }
        System.err.println("Nothing was done, but ok...");
        return "Nothing was done, but ok...";
    }

}
