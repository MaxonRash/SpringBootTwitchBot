package com.bot.springboottwitchbot.utilities;

/**
 * Helpers for dealing with Twitch chat message text.
 */
public final class TwitchText {

    /**
     * The invisible Unicode "tag" character U+E0000 that some Twitch clients append to messages
     * (notably to bypass Twitch's "identical message" block — two visually-equal messages differ by
     * this hidden char). It is built from its code point so it does not appear as an invisible blank
     * in source. Strip it before matching command text, or prefix/equals/split checks can silently fail.
     */
    public static final String INVISIBLE_TAG = new String(Character.toChars(0xE0000));

    private TwitchText() {
    }

    /** Returns {@code text} with the invisible {@link #INVISIBLE_TAG} character removed. */
    public static String stripInvisible(String text) {
        return text.replace(INVISIBLE_TAG, "");
    }
}
