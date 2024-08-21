package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.DTOs.emote_only_DTOs.EmoteOnlyDTO;
import com.bot.springboottwitchbot.DTOs.get_followers_DTOs.GetFollowersDTO;
import com.bot.springboottwitchbot.DTOs.moderator_DTOs.ModeratorDTO;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.send.Outcomes;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.create_predictions_DTOs.send.SendCreatePredictionDTO;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.end_predictions_DTOs.send.SendEndPredictionDTO;
import com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.GetPredictionsDTO;
import com.bot.springboottwitchbot.DTOs.timeout_DTOs.Data;
import com.bot.springboottwitchbot.DTOs.timeout_DTOs.TimeoutUserDTO;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.channels.builder_utils.MainBuilderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class UtilityCommandsMainChannel {
    private static final HttpEntity<Void> httpGetEntity;
    private static final ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    private static final HttpHeaders headersBotToken;

    static {
        headersBotToken = new HttpHeaders();
        headersBotToken.setContentType(MediaType.APPLICATION_JSON);
        headersBotToken.add("Authorization", "Bearer " + applicationContext.getBean(BotBuilderUtil.class).getBotToken());
        headersBotToken.add("Client-Id", applicationContext.getBean(BotBuilderUtil.class).getClient_id());
        httpGetEntity = new HttpEntity<>(headersBotToken);
    }

    private UtilityCommandsMainChannel() {
    }

    public static void vipUser(String userLogin) throws IOException {
        String id = UtilityCommandsGlobal.getUserIdByName(userLogin);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.twitch.tv/helix/channels/vips?broadcaster_id=" +
                applicationContext.getBean(MainBuilderUtil.class).getMainChannelId() + "&user_id=" + id;

        HttpHeaders headersVIP = new HttpHeaders();
        headersVIP.setContentType(MediaType.APPLICATION_JSON);
        headersVIP.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        headersVIP.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());

        HttpEntity<Void> request = new HttpEntity<>(headersVIP);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println("Making VIP user " + userLogin + " " + response);
    }

    public static void unVipUser(String userLogin) throws IOException {
        String id = UtilityCommandsGlobal.getUserIdByName(userLogin);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.twitch.tv/helix/channels/vips?broadcaster_id=" +
                applicationContext.getBean(MainBuilderUtil.class).getMainChannelId() + "&user_id=" + id;

        HttpHeaders headersVIP = new HttpHeaders();
        headersVIP.setContentType(MediaType.APPLICATION_JSON);
        headersVIP.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        headersVIP.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());

        HttpEntity<Void> request = new HttpEntity<>(headersVIP);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        System.out.println("Unmaking VIP user " + userLogin + " " + response);
    }

    public static void timeoutUser(String userId, int duration, String reason) throws IOException {
        Data data = new Data(userId, duration, reason);
        TimeoutUserDTO timeoutUserDTO = new TimeoutUserDTO(data);
        String url = "https://api.twitch.tv/helix/moderation/bans?broadcaster_id=" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelId() +
                "&moderator_id=" + applicationContext.getBean(BotBuilderUtil.class).getBotChannelId();

        String timeoutUserStringDTO = new ObjectMapper().writeValueAsString(timeoutUserDTO);

        HttpEntity<String> request = new HttpEntity<>(timeoutUserStringDTO, headersBotToken);
        System.out.println("Timeout:" + new RestTemplate().postForObject(url, request, String.class));
    }

    public static void emoteOnlyMode(Boolean state) throws JsonProcessingException {
        String url = "https://api.twitch.tv/helix/chat/settings?broadcaster_id=" +
                applicationContext.getBean(MainBuilderUtil.class).getMainChannelId() + "&moderator_id=" + applicationContext.getBean(BotBuilderUtil.class).getBotChannelId();

        HttpHeaders headersEmotemode = new HttpHeaders();
        headersEmotemode.setContentType(MediaType.APPLICATION_JSON);
        headersEmotemode.add("Authorization", "Bearer " + applicationContext.getBean(BotBuilderUtil.class).getBotToken());
        headersEmotemode.add("Client-Id", applicationContext.getBean(BotBuilderUtil.class).getClient_id());

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        EmoteOnlyDTO emoteOnlyDTO = new EmoteOnlyDTO(state);
        String emoteOnlyStringDTO = new ObjectMapper().writeValueAsString(emoteOnlyDTO);
        HttpEntity<String> request = new HttpEntity<>(emoteOnlyStringDTO, headersEmotemode);

        System.out.println("Emote mode: " + state + " " + restTemplate.patchForObject(url, request, String.class));

    }


    public static ArrayList<String> getModeratorsList() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        headers.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());
        HttpEntity<Void> httpGetEntityMods = new HttpEntity<>(headers);

        String url = "https://api.twitch.tv/helix/moderation/moderators?broadcaster_id=" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelId()
                +"&first=50";
        ResponseEntity<ModeratorDTO> response = restTemplate.exchange(url, HttpMethod.GET, httpGetEntityMods, ModeratorDTO.class);
        ModeratorDTO moderatorDTO = null;
        if (response.hasBody()) {
            moderatorDTO = response.getBody();
        }

        ArrayList<String> moderatorsList = new ArrayList<>();
        if (moderatorDTO != null && !moderatorDTO.getData().isEmpty()) {
            for (com.bot.springboottwitchbot.DTOs.moderator_DTOs.Data data : moderatorDTO.getData()) {
//                System.out.println(data.getUser_login()); // - debug
                moderatorsList.add(data.getUser_login());
            }
        }

        return moderatorsList;
    }

    public static Date getFollowingSinceDate(int userId) throws ParseException {
        HttpHeaders followersHeaders = new HttpHeaders();
        followersHeaders.setContentType(MediaType.APPLICATION_JSON);
        followersHeaders.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        followersHeaders.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());
        String url = "https://api.twitch.tv/helix/channels/followers?broadcaster_id=" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelId()
                + "&user_id=" + userId;
        HttpEntity<Void> httpEntityGetFollowers = new HttpEntity<>(followersHeaders);
        GetFollowersDTO getFollowersDTO = new RestTemplate().exchange(url, HttpMethod.GET, httpEntityGetFollowers, GetFollowersDTO.class).getBody();
        if (!Objects.requireNonNull(getFollowersDTO).getData().isEmpty()) {
            String followedAt = Objects.requireNonNull(getFollowersDTO).getData().get(0).getFollowed_at();
//        System.out.println("Following since: " + followedAt);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return simpleDateFormat.parse(followedAt);
        }
        else {
            return null;
        }
    }

    public static ArrayList<com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data> getPredictionsList() {
        HttpHeaders getPredictionsHeaders = new HttpHeaders();
        getPredictionsHeaders.setContentType(MediaType.APPLICATION_JSON);
        getPredictionsHeaders.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        getPredictionsHeaders.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());
        String url = "https://api.twitch.tv/helix/predictions?broadcaster_id=" + applicationContext.getBean(MainBuilderUtil.class).getMainChannelId()
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
        outcome1.setTitle("Я гордый Беливер (1-4)");
        outcome2.setTitle("Я скользкий Даубтер (5-8)");
        ArrayList<Outcomes> outcomes = new ArrayList<>(Arrays.asList(outcome1, outcome2));
        sendCreatePredictionDTO.setOutcomes(outcomes);
        sendCreatePredictionDTO.setPrediction_window(300);
        sendCreatePredictionDTO.setBroadcaster_id(applicationContext.getBean(MainBuilderUtil.class).getMainChannelId());
        sendCreatePredictionDTO.setTitle("Победа или смерть?");
        String url = "https://api.twitch.tv/helix/predictions";

        String sendCreatePredictionStringDTO = new ObjectMapper().writeValueAsString(sendCreatePredictionDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        httpHeaders.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());

        HttpEntity<String> request = new HttpEntity<>(sendCreatePredictionStringDTO, httpHeaders);
        System.out.println("Prediction started: " + new RestTemplate().postForObject(url, request, String.class));
    }

    public static void cancelStandardPrediction () throws JsonProcessingException {
        com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data = checkAndReturnDataIfStandardPredictionIsActive();
        if (data == null) {
            data = checkAndReturnDataIfStandardPredictionIsLocked();
        }
        SendEndPredictionDTO sendEndPredictionDTO = new SendEndPredictionDTO();
        sendEndPredictionDTO.setBroadcaster_id(applicationContext.getBean(MainBuilderUtil.class).getMainChannelId());
        sendEndPredictionDTO.setId(Objects.requireNonNull(data).getId());
        sendEndPredictionDTO.setStatus("CANCELED");

        String url = "https://api.twitch.tv/helix/predictions";
        String sendEndPredictionStringDTO = new ObjectMapper().writeValueAsString(sendEndPredictionDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        httpHeaders.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());

        HttpEntity<String> request = new HttpEntity<>(sendEndPredictionStringDTO, httpHeaders);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        System.out.println("Prediction CANCELED: " + restTemplate.patchForObject(url, request, String.class));
    }

    public static void winStandardPrediction () throws JsonProcessingException {
        com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data = checkAndReturnDataIfStandardPredictionIsLocked();
        SendEndPredictionDTO sendEndPredictionDTO = new SendEndPredictionDTO();
        sendEndPredictionDTO.setBroadcaster_id(applicationContext.getBean(MainBuilderUtil.class).getMainChannelId());
        sendEndPredictionDTO.setId(Objects.requireNonNull(data).getId());
        sendEndPredictionDTO.setStatus("RESOLVED");

        String winningOutcomeId = null;

        for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Outcomes outcome : data.getOutcomes()) {
            if (outcome.getTitle().toLowerCase().contains("Я гордый Беливер (1-4)".toLowerCase())) {
                winningOutcomeId = outcome.getId();
            }
        }

        sendEndPredictionDTO.setWinning_outcome_id(winningOutcomeId);

        String url = "https://api.twitch.tv/helix/predictions";
        String sendEndPredictionStringDTO = new ObjectMapper().writeValueAsString(sendEndPredictionDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        httpHeaders.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());

        HttpEntity<String> request = new HttpEntity<>(sendEndPredictionStringDTO, httpHeaders);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        System.out.println("Prediction RESOLVED WIN: " + restTemplate.patchForObject(url, request, String.class));
    }


    public static void loseStandardPrediction () throws JsonProcessingException {
        com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Data data = checkAndReturnDataIfStandardPredictionIsLocked();
        SendEndPredictionDTO sendEndPredictionDTO = new SendEndPredictionDTO();
        sendEndPredictionDTO.setBroadcaster_id(applicationContext.getBean(MainBuilderUtil.class).getMainChannelId());
        sendEndPredictionDTO.setId(Objects.requireNonNull(data).getId());
        sendEndPredictionDTO.setStatus("RESOLVED");

        String winningOutcomeId = null;

        for (com.bot.springboottwitchbot.DTOs.predictions_DTOs.get_predictions_DTOs.Outcomes outcome : data.getOutcomes()) {
            if (outcome.getTitle().toLowerCase().contains("Я скользкий Даубтер (5-8)".toLowerCase())) {
                winningOutcomeId = outcome.getId();
            }
        }

        sendEndPredictionDTO.setWinning_outcome_id(winningOutcomeId);

        String url = "https://api.twitch.tv/helix/predictions";
        String sendEndPredictionStringDTO = new ObjectMapper().writeValueAsString(sendEndPredictionDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + applicationContext.getBean(MainBuilderUtil.class).getMainToken());
        httpHeaders.add("Client-Id", applicationContext.getBean(MainBuilderUtil.class).getClient_id());

        HttpEntity<String> request = new HttpEntity<>(sendEndPredictionStringDTO, httpHeaders);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        System.out.println("Prediction RESOLVED LOSE: " + restTemplate.patchForObject(url, request, String.class));
    }
}
