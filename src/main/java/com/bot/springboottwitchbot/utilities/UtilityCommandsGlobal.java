package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.DTOs.get_user_DTOs.Data;
import com.bot.springboottwitchbot.DTOs.get_user_DTOs.GetUserDTO;
import com.bot.springboottwitchbot.connections.channels.builder_utils.BotBuilderUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UtilityCommandsGlobal {
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
            System.out.println(e.getMessage());
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
        Header header1 = new BasicHeader("Authorization", "Bearer 4zu2o9yscreqifsdllckdsjivjv3um");
        Header header2 = new BasicHeader("Client-Id", "gp762nuuoqcoxypju8c569th9wz7q5");
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
            throw new IOException("User not found");
        }
//        throw new IOException("User not found");
    }
}
