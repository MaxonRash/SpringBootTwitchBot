package com.bot.springboottwitchbot.gpt.yandexgpt;

import com.bot.springboottwitchbot.gpt.yandexgpt.yandexgptDTO.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GetIAMTokenFromOAuth {
    private static final Logger log = LoggerFactory.getLogger(GetIAMTokenFromOAuth.class);

    @Value("${oauth_token}")
    String oAuthToken;

    public String getIAMToken() {
        String url = "https://iam.api.cloud.yandex.net/iam/v1/tokens";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(oAuthToken);
        ResponseEntity<String> response = null;
        try {
            response = new RestTemplate()
                    .postForEntity(url, new HttpEntity<>(new OAuthToken(oAuthToken), headers),
                            String.class);
        } catch (Exception e) {
            log.error("error while requesting IAMToken from OAuthToken", e);
        }
        String textFromResponse = "";
        if (response.getBody() != null) {
            log.debug("ответ при запросе IAMToken: {}", response.getBody());
            try {
                JsonNode node = new ObjectMapper().readTree(response.getBody());
                textFromResponse = node.get("iamToken").asText();
                log.debug("text extracted from response, iamToken: {}", textFromResponse);
            } catch (JsonProcessingException e) {
                log.error("unable to deserialize json", e);
            }
        }

        return textFromResponse;
    }
}
