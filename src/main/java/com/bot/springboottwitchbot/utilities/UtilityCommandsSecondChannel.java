package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.dto.prediction.create.request.CreatePredictionRequestOutcome;
import com.bot.springboottwitchbot.dto.prediction.create.request.CreatePredictionRequest;
import com.bot.springboottwitchbot.dto.prediction.end.request.EndPredictionRequest;
import com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsResponse;
import com.bot.springboottwitchbot.connections.channels.builder_utils.SecondBuilderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UtilityCommandsSecondChannel.class);
    private static final ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    public static ArrayList<com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData> getPredictionsList() {
        HttpHeaders getPredictionsHeaders = new HttpHeaders();
        getPredictionsHeaders.setContentType(MediaType.APPLICATION_JSON);
        getPredictionsHeaders.add("Authorization", "Bearer " + applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelToken());
        getPredictionsHeaders.add("Client-Id", applicationContext.getBean(SecondBuilderUtil.class).getClient_id());
        String url = "https://api.twitch.tv/helix/predictions?broadcaster_id=" + applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelId()
                +"&first=3";
        HttpEntity<Void> httpEntityGetPredictions = new HttpEntity<>(getPredictionsHeaders);
        GetPredictionsResponse getPredictionsDTO = new RestTemplate().exchange(url, HttpMethod.GET, httpEntityGetPredictions, GetPredictionsResponse.class).getBody();
        if (getPredictionsDTO != null && getPredictionsDTO.getData() != null && !getPredictionsDTO.getData().isEmpty()) {
//            System.out.println("getPredictionList: " + getPredictionsDTO.getData());
            return getPredictionsDTO.getData();
        }
        return null;
    }

    public static boolean checkIfStandardPredictionIsActive() {
        if (getPredictionsList()!= null) {
            for (com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData data : getPredictionsList()) {
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
            for (com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData data : getPredictionsList()) {
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

    public static com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData checkAndReturnDataIfStandardPredictionIsActive() {
        if (getPredictionsList()!= null) {
            for (com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData data : getPredictionsList()) {
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

    public static com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData checkAndReturnDataIfStandardPredictionIsLocked() {
        if (getPredictionsList()!= null) {
            for (com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData data : getPredictionsList()) {
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
        CreatePredictionRequest sendCreatePredictionDTO = new CreatePredictionRequest();
        CreatePredictionRequestOutcome outcome1 = new CreatePredictionRequestOutcome();
        CreatePredictionRequestOutcome outcome2 = new CreatePredictionRequestOutcome();
        //TODO POMENYAT
        outcome1.setTitle("Я гордый Беливер (1-4)");
        outcome2.setTitle("Я скользкий Даубтер (5-8)");
        ArrayList<CreatePredictionRequestOutcome> outcomes = new ArrayList<>(Arrays.asList(outcome1, outcome2));
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
        log.info("Prediction started: {}", new RestTemplate().postForObject(url, request, String.class));
    }

    public static void winStandardPrediction () throws JsonProcessingException {
        com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData data = checkAndReturnDataIfStandardPredictionIsLocked();
        EndPredictionRequest sendEndPredictionDTO = new EndPredictionRequest();
        sendEndPredictionDTO.setBroadcaster_id(applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelId());
        sendEndPredictionDTO.setId(Objects.requireNonNull(data).getId());
        sendEndPredictionDTO.setStatus("RESOLVED");

        String winningOutcomeId = null;

        for (com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsOutcome outcome : data.getOutcomes()) {
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

        log.info("Prediction RESOLVED WIN: {}", restTemplate.patchForObject(url, request, String.class));
    }


    public static void loseStandardPrediction () throws JsonProcessingException {
        com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsData data = checkAndReturnDataIfStandardPredictionIsLocked();
        EndPredictionRequest sendEndPredictionDTO = new EndPredictionRequest();
        sendEndPredictionDTO.setBroadcaster_id(applicationContext.getBean(SecondBuilderUtil.class).getSecondChannelId());
        sendEndPredictionDTO.setId(Objects.requireNonNull(data).getId());
        sendEndPredictionDTO.setStatus("RESOLVED");

        String losingOutcomeId = null;

        for (com.bot.springboottwitchbot.dto.prediction.get.GetPredictionsOutcome outcome : data.getOutcomes()) {
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

        log.info("Prediction RESOLVED LOSE: {}", restTemplate.patchForObject(url, request, String.class));
    }
}
