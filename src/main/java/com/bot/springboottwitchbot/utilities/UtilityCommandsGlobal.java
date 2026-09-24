package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.DTOs.get_user_DTOs.Data;
import com.bot.springboottwitchbot.DTOs.get_user_DTOs.GetUserDTO;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UtilityCommandsGlobal {
    private static final Logger log = LoggerFactory.getLogger(UtilityCommandsGlobal.class);
    public static String getUserIdByName(String login) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        String url = "https://api.twitch.tv/helix/users?login=" + login;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + applicationContext.getBean(BotBuilderUtil.class).getBotToken());
        headers.add("Client-Id", applicationContext.getBean(BotBuilderUtil.class).getClient_id());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<GetUserDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, GetUserDTO.class);
        try {
            return Objects.requireNonNull(response.getBody()).getData().get(0).getId();
        } catch (NullPointerException e) {
            log.warn("getUserIdByName: user not found for login {}", login, e);
        }

        // without mapping to DTO :
        /* JsonNode object = new ObjectMapper().readTree(response.getBody());
        try {
            return object.findValue("id").asText();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } */

        // with Gson and apache
        /* HttpGet getUser = new HttpGet("https://api.twitch.tv/helix/users?login=" + login);
        Header header1 = new BasicHeader("Authorization", "Bearer TOKEN");
        Header header2 = new BasicHeader("Client-Id", "TOKEN");
        Header header3 = new BasicHeader("Content-Type", "application/json");
        Header[] headers = new Header[]{header1, header2, header3};

        String userDetails;

        getUser.setHeaders(headers);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(getUser)) {
            userDetails = EntityUtils.toString(response.getEntity());
        }

        GetUserDTO getUserDTO = null;
        if (userDetails != null) {
            Gson gson = new Gson();
            getUserDTO = gson.fromJson(userDetails, GetUserDTO.class);
        }
        if (getUserDTO != null) {
            return getUserDTO.getData().get(0).getId();
        } */

        return null;
    }

    public static List<Data> getUserDTODataByName(String login) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        String url = "https://api.twitch.tv/helix/users?login=" + login;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + applicationContext.getBean(BotBuilderUtil.class).getBotToken());
        headers.add("Client-Id", applicationContext.getBean(BotBuilderUtil.class).getClient_id());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<GetUserDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, GetUserDTO.class);
        try {
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (NullPointerException e) {
            log.warn("getUserDTODataByName: user not found for login {}", login, e);
            throw new IOException("User not found");
        }
//        throw new IOException("User not found");
    }

    public static GetUserDTO getUserDTOByName(String login) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        String url = "https://api.twitch.tv/helix/users?login=" + login;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + applicationContext.getBean(BotBuilderUtil.class).getBotToken());
        headers.add("Client-Id", applicationContext.getBean(BotBuilderUtil.class).getClient_id());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<GetUserDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, GetUserDTO.class);
        try {
            return Objects.requireNonNull(response.getBody());
        } catch (NullPointerException e) {
            log.warn("getUserDTOByName: user not found for login {}", login, e);
            throw new IOException("User not found");
        }
//        throw new IOException("User not found");
    }
}
