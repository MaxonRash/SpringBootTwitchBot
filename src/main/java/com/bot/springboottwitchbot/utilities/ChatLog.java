package com.bot.springboottwitchbot.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes a full chat transcript (incoming user messages and outgoing bot messages) to the dedicated
 * {@code "CHAT"} logger, which {@code logback-spring.xml} routes to a rolling {@code chat.log}
 * (one file per day, last 7 days kept). This lets the whole chat be reviewed after the fact — e.g. to
 * see how commands behaved while nobody was watching — independently of the console log level/profile.
 */
public final class ChatLog {

    private static final Logger log = LoggerFactory.getLogger("CHAT");

    private ChatLog() {
    }

    /** An incoming chat message from a viewer. */
    public static void in(String channel, String user, String message) {
        log.info("IN  [{}] {}: {}", channel, user, message);
    }

    /** An outgoing message sent by the bot. */
    public static void out(String channel, String message) {
        log.info("OUT [{}] {}", channel, message);
    }
}
