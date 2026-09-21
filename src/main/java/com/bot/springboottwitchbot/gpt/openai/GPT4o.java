package com.bot.springboottwitchbot.gpt.openai;

import com.bot.springboottwitchbot.gpt.openai.openaiDTO.CreateModelResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GPT4o {
    @Value("${gpt_token}")
    String gptToken;
    public boolean isTextContainingBadWord (String text) {
        CreateModelResponse createModelResponse = new CreateModelResponse("gpt-4o",
                "есть ли в этом сообщении мат, да или нет одним словом: \"" + text + "\"", 70, 1.0);
        String url = "https://api.openai.com/v1/responses";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptToken);
        ResponseEntity<String> response = null;
        try {
            response = new RestTemplate()
                    .postForEntity(url, new HttpEntity<>(createModelResponse, headers),
                            String.class);
        } catch (Exception e) {
            System.out.println("error while requesting to gpt: " + e.getMessage());
        }
        String textFromResponse = "";
        System.out.println(response.getBody());
        if (response != null) {
            try {
                JsonNode outputNode = new ObjectMapper().readTree(response.getBody()).path("output");
                JsonNode contentNode = outputNode.get(0).path("content");
                textFromResponse = contentNode.get(0).path("text").asText();
                System.out.println("text from response: " + textFromResponse);
            } catch (JsonProcessingException e) {
                System.out.println("unable to deserialize json");
            }
        }
        return textFromResponse.toLowerCase().contains("да");
    }

    public String TextContainingBadWord (String text) {
        CreateModelResponse createModelResponse = new CreateModelResponse("gpt-4o-mini",
//                "есть ли в этом сообщении матное слово и объясни что оно значит: \"" + text + "\"");
                "объясни почему в этом сообщении есть некорректное слово не повторяя его, умести в 70 токенов: \"" + text + "\"", 70, 1.0);
        String url = "https://api.openai.com/v1/responses";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptToken);
        ResponseEntity<String> response = null;
        try {
            response = new RestTemplate()
                    .postForEntity(url, new HttpEntity<>(createModelResponse, headers),
                            String.class);
        } catch (Exception e) {
            System.out.println("error while requesting to gpt: " + e.getMessage());
        }
        String textFromResponse = "";
        if (response != null) {
            try {
                JsonNode outputNode = new ObjectMapper().readTree(response.getBody()).path("output");
                JsonNode contentNode = outputNode.get(0).path("content");
                textFromResponse = contentNode.get(0).path("text").asText();
            } catch (JsonProcessingException e) {
                System.out.println("unable to deserialize json");
            }
        }
        return textFromResponse;
    }

    public boolean isTextContainingTsyaMistake (String text) {
        CreateModelResponse createModelResponse = new CreateModelResponse("gpt-4o",
                "есть ли в этом сообщении ошибки в написании слов с \"тся\" и \"ться\", ответь да или нет одним словом: \"" + text + "\"", 70, 1.0);
        String url = "https://api.openai.com/v1/responses";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptToken);
        ResponseEntity<String> response = null;
        try {
            response = new RestTemplate()
                    .postForEntity(url, new HttpEntity<>(createModelResponse, headers),
                            String.class);
        } catch (Exception e) {
            System.out.println("error while requesting to gpt: " + e.getMessage());
        }
        String textFromResponse = "";
        System.out.println(response.getBody());
        if (response != null) {
            try {
                JsonNode outputNode = new ObjectMapper().readTree(response.getBody()).path("output");
                JsonNode contentNode = outputNode.get(0).path("content");
                textFromResponse = contentNode.get(0).path("text").asText();
                System.out.println("text from response: " + textFromResponse);
            } catch (JsonProcessingException e) {
                System.out.println("unable to deserialize json");
            }
        }
        return textFromResponse.toLowerCase().contains("да");
    }

    public String ResponseForTextContainingTsyaMistake (String text) {
        CreateModelResponse createModelResponse = new CreateModelResponse("gpt-4o",
                "вежливо объясни в каких словах с \"тся\" и \"ться\" в этом сообщении есть ошибка не учитывая ошибки в других словах, умести в 70 токенов: \"" + text + "\"", 70, 1.0);
        String url = "https://api.openai.com/v1/responses";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptToken);
        ResponseEntity<String> response = null;
        try {
            response = new RestTemplate()
                    .postForEntity(url, new HttpEntity<>(createModelResponse, headers),
                            String.class);
        } catch (Exception e) {
            System.out.println("error while requesting to gpt: " + e.getMessage());
        }
        String textFromResponse = "";
        if (response != null) {
            try {
                JsonNode outputNode = new ObjectMapper().readTree(response.getBody()).path("output");
                JsonNode contentNode = outputNode.get(0).path("content");
                textFromResponse = contentNode.get(0).path("text").asText();
            } catch (JsonProcessingException e) {
                System.out.println("unable to deserialize json");
            }
        }
        return textFromResponse;
    }

    public String ResponseForTextAddressingToBot (String text) {
        CreateModelResponse createModelResponse = new CreateModelResponse("gpt-4o",
                text + ", ответ умести в 50 слов не считая пробелов: \"" + text + "\"", 120, 1.0);
        String url = "https://api.openai.com/v1/responses";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptToken);
        ResponseEntity<String> response = null;
        try {
            response = new RestTemplate()
                    .postForEntity(url, new HttpEntity<>(createModelResponse, headers),
                            String.class);
        } catch (Exception e) {
            System.out.println("error while requesting to gpt: " + e.getMessage());
        }
        String textFromResponse = "";
        if (response != null) {
            try {
                JsonNode outputNode = new ObjectMapper().readTree(response.getBody()).path("output");
                JsonNode contentNode = outputNode.get(0).path("content");
                textFromResponse = contentNode.get(0).path("text").asText();
            } catch (JsonProcessingException e) {
                System.out.println("unable to deserialize json");
            }
        }
        return textFromResponse;
    }
}
