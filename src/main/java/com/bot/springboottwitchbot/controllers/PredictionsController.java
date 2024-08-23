package com.bot.springboottwitchbot.controllers;

import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.text.ParseException;

@Controller
public class PredictionsController {
    @GetMapping("/predictions")
    public String predictionsPage(Model model) {
        boolean active = UtilityCommandsMainChannel.checkIfStandardPredictionIsActive();
        boolean locked = UtilityCommandsMainChannel.checkIfStandardPredictionIsLocked();
        model.addAttribute("standardPredictionIsActive", active);
        model.addAttribute("standardPredictionIsLocked", locked);
        return "predictions";
    }

    @GetMapping("/predictions/make_standard_prediction")
    public String makeStandardPrediction() throws IOException, InterruptedException {
        UtilityCommandsMainChannel.makeStandardPrediction();
        Thread.sleep(1500);
        return "redirect:/predictions";
    }

    @GetMapping("/predictions/cancel_standard_prediction")
    public String cancelStandardPrediction() throws IOException, InterruptedException {
        UtilityCommandsMainChannel.cancelStandardPrediction();
        Thread.sleep(1500);
        return "redirect:/predictions";
    }

    @GetMapping("/predictions/win_standard_prediction")
    public String winStandardPrediction() throws IOException, InterruptedException {
        UtilityCommandsMainChannel.winStandardPrediction();
        Thread.sleep(1500);
        return "redirect:/predictions";
    }

    @GetMapping("/predictions/lose_standard_prediction")
    public String loseStandardPrediction() throws IOException, InterruptedException {
        UtilityCommandsMainChannel.loseStandardPrediction();
        Thread.sleep(1500);
        return "redirect:/predictions";
    }

}
