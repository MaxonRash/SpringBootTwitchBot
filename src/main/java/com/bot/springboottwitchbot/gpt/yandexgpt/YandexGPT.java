package com.bot.springboottwitchbot.gpt.yandexgpt;

import com.bot.springboottwitchbot.gpt.yandexgpt.yandexgptDTO.CreateModelRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class YandexGPT {
    private static final Logger log = LoggerFactory.getLogger(YandexGPT.class);

    @Autowired
    GetIAMTokenFromOAuth gptToken;

    public boolean isTextContainingBadWord (String text) {
        Map<String, Object> completionOptions = Map.of(
//                "reasoningOptions", Map.of("mode", "ENABLED_HIDDEN"),
                "stream", false,
                "maxTokens", 100,
                "temperature", 0
        );
        Map<String, String> systemText = Map.of(
                "role", "system",
//                "text", "Нужно определить есть ли в сообщении матерное слово. Ответь одним словом да или нет"
                "text", "Нужно определить есть ли в сообщении матерное слово. Ответь да или нет"
//                "text", "Придумай "
        );
        Map<String, String> userText = Map.of(
                "role", "user",
                "text", text
        );
        List<Map<String, String>> messages = List.of(systemText, userText);
        CreateModelRequest modelRequest = new CreateModelRequest(
//                "gpt://b1grqt6ajiq7mrqmm22h/yandexgpt/rc",
//                "gpt://b1grqt6ajiq7mrqmm22h/yandexgpt-lite/rc@tamrfn92ev7cnf4iu0a03",
                "gpt://b1grqt6ajiq7mrqmm22h/yandexgpt-lite/rc@tamr9ra7qgbqrn09uvh62",
                completionOptions,
                messages
                );

        String url = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptToken.getIAMToken());
        ResponseEntity<String> response = null;
        try {
            response = new RestTemplate()
                    .postForEntity(url, new HttpEntity<>(modelRequest, headers),
                            String.class);
        } catch (Exception e) {
            log.error("error while requesting to gpt", e);
        }
        String textFromResponse = "";
        log.debug("gpt response body: {}", response.getBody());
        return true;
    }
}
