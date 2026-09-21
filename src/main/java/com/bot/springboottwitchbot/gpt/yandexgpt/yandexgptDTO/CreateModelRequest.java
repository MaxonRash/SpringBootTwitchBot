package com.bot.springboottwitchbot.gpt.yandexgpt.yandexgptDTO;

import java.util.List;
import java.util.Map;

public record CreateModelRequest(String modelUri, Map<String, Object> completionOptions, List<Map<String, String>> messages) {
}
