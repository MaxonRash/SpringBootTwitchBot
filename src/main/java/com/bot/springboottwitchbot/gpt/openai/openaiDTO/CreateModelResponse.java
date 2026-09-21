package com.bot.springboottwitchbot.gpt.openai.openaiDTO;

public record CreateModelResponse(String model, String input, int max_output_tokens, double temperature) {
}
