package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.send.Outcomes;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.send.SendCreatePredictionDTO;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.end_predictions_DTOs.send.SendEndPredictionDTO;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.GetPredictionsDTO;
import com.bot.springboottwitchbot.connections.channels.builder_utils.SecondBuilderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class UtilityCommandsSecondChannel {
    private static final ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    public static ArrayList<com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data> getPredictionsList() {
        HttpHeaders getPredictionsHeaders = new HttpHeaders();
        getPredictionsHeaders.setContentType(MediaType.APPLICATION_JSON);
        getPredictionsHeaders.add("Authorization", "Bearer " + applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelToken());
        getPredictionsHeaders.add("Client-Id", applicationContext.getBean(SecondBuilderUtil.class).getClient_id());
        String url = "https://api.twitch.tv/helix/predictions?broadcaster_id=" + applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelId()
                +"&first=3";
        HttpEntity<Void> httpEntityGetPredictions = new HttpEntity<>(getPredictionsHeaders);
        GetPredictionsDTO getPredictionsDTO = new RestTemplate().exchange(url, HttpMethod.GET, httpEntityGetPredictions, GetPredictionsDTO.class).getBody();
        if (getPredictionsDTO != null && getPredictionsDTO.getData() != null && !getPredictionsDTO.getData().isEmpty()) {
//            System.out.println("getPredictionList: " + getPredictionsDTO.getData());
            return getPredictionsDTO.getData();
        }
        return null;
    }

    public static boolean checkIfStandardPredictionIsActive() {
        if (getPredictionsList()!= null) {
            for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data : getPredictionsList()) {
                //TODO POMENYAT
                if (data.getTitle().toLowerCase().contains("Победа или смерть?".toLowerCase())) {
                    if (data.getStatus().equalsIgnoreCase("active")) {
//                        System.out.println("Prediction status is ACTIVE");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkIfStandardPredictionIsLocked() {
        if (getPredictionsList()!= null) {
            for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data : getPredictionsList()) {
                //TODO POMENYAT
                if (data.getTitle().toLowerCase().contains("Победа или смерть?".toLowerCase())) {
                    if (data.getStatus().equalsIgnoreCase("locked")) {
//                        System.out.println("Prediction status is ACTIVE");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data checkAndReturnDataIfStandardPredictionIsActive() {
        if (getPredictionsList()!= null) {
            for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data : getPredictionsList()) {
                //TODO POMENYAT
                if (data.getTitle().toLowerCase().contains("Победа или смерть?".toLowerCase())) {
                    if (data.getStatus().equalsIgnoreCase("active")) {
//                        System.out.println("Prediction status is ACTIVE");
                        return data;
                    }
                }
            }
        }
        return null;
    }

    public static com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data checkAndReturnDataIfStandardPredictionIsLocked() {
        if (getPredictionsList()!= null) {
            for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data : getPredictionsList()) {
                //TODO POMENYAT
                if (data.getTitle().toLowerCase().contains("Победа или смерть?".toLowerCase())) {
                    if (data.getStatus().equalsIgnoreCase("locked")) {
//                        System.out.println("Prediction status is ACTIVE");
                        return data;
                    }
                }
            }
        }
        return null;
    }
    public static void makeStandardPrediction() throws IOException {
        SendCreatePredictionDTO sendCreatePredictionDTO = new SendCreatePredictionDTO();
        Outcomes outcome1 = new Outcomes();
        Outcomes outcome2 = new Outcomes();
        //TODO POMENYAT
        outcome1.setTitle("Я гордый Беливер (1-4)");
        outcome2.setTitle("Я скользкий Даубтер (5-8)");
        ArrayList<Outcomes> outcomes = new ArrayList<>(Arrays.asList(outcome1, outcome2));
        sendCreatePredictionDTO.setOutcomes(outcomes);
        //TODO POMENYAT
        sendCreatePredictionDTO.setPrediction_window(300);
        sendCreatePredictionDTO.setBroadcaster_id(applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelId());
        //TODO POMENYAT
        sendCreatePredictionDTO.setTitle("Победа или смерть?");
        String url = "https://api.twitch.tv/helix/predictions";

        String sendCreatePredictionStringDTO = new ObjectMapper().writeValueAsString(sendCreatePredictionDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelToken());
        httpHeaders.add("Client-Id", applicationContext.getBean(SecondBuilderUtil.class).getClient_id());

        HttpEntity<String> request = new HttpEntity<>(sendCreatePredictionStringDTO, httpHeaders);
        System.out.println("Prediction started: " + new RestTemplate().postForObject(url, request, String.class));
    }

    public static void winStandardPrediction () throws JsonProcessingException {
        com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data = checkAndReturnDataIfStandardPredictionIsLocked();
        SendEndPredictionDTO sendEndPredictionDTO = new SendEndPredictionDTO();
        sendEndPredictionDTO.setBroadcaster_id(applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelId());
        sendEndPredictionDTO.setId(Objects.requireNonNull(data).getId());
        sendEndPredictionDTO.setStatus("RESOLVED");

        String winningOutcomeId = null;

        for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Outcomes outcome : data.getOutcomes()) {
            //TODO POMENYAT
            if (outcome.getTitle().toLowerCase().contains("Я гордый Беливер (1-4)".toLowerCase())) {
                winningOutcomeId = outcome.getId();
            }
        }

        sendEndPredictionDTO.setWinning_outcome_id(winningOutcomeId);

        String url = "https://api.twitch.tv/helix/predictions";
        String sendEndPredictionStringDTO = new ObjectMapper().writeValueAsString(sendEndPredictionDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelToken());
        httpHeaders.add("Client-Id", applicationContext.getBean(SecondBuilderUtil.class).getClient_id());

        HttpEntity<String> request = new HttpEntity<>(sendEndPredictionStringDTO, httpHeaders);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        System.out.println("Prediction RESOLVED WIN: " + restTemplate.patchForObject(url, request, String.class));
    }


    public static void loseStandardPrediction () throws JsonProcessingException {
        com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data = checkAndReturnDataIfStandardPredictionIsLocked();
        SendEndPredictionDTO sendEndPredictionDTO = new SendEndPredictionDTO();
        sendEndPredictionDTO.setBroadcaster_id(applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelId());
        sendEndPredictionDTO.setId(Objects.requireNonNull(data).getId());
        sendEndPredictionDTO.setStatus("RESOLVED");

        String losingOutcomeId = null;

        for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Outcomes outcome : data.getOutcomes()) {
            //TODO POMENYAT
            if (outcome.getTitle().toLowerCase().contains("Я скользкий Даубтер (5-8)".toLowerCase())) {
                losingOutcomeId = outcome.getId();
            }
        }

        sendEndPredictionDTO.setWinning_outcome_id(losingOutcomeId);

        String url = "https://api.twitch.tv/helix/predictions";
        String sendEndPredictionStringDTO = new ObjectMapper().writeValueAsString(sendEndPredictionDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelToken());
        httpHeaders.add("Client-Id", applicationContext.getBean(SecondBuilderUtil.class).getClient_id());

        HttpEntity<String> request = new HttpEntity<>(sendEndPredictionStringDTO, httpHeaders);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        System.out.println("Prediction RESOLVED LOSE: " + restTemplate.patchForObject(url, request, String.class));
    }
}
