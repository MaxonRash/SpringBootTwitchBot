package com.bot.springboottwitchbot.gpt;

public enum FilterMode {
    AI("AI"), OLD("OLD");

    private final String name;

    FilterMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
