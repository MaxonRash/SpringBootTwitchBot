package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.gpt.FilterMode;
import com.bot.springboottwitchbot.gpt.openai.GPT4o;
import com.bot.springboottwitchbot.utilities.TwitchText;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The swear filter (passive, runs on every message). Migrated from {@code timeoutForMat} (both handlers)
 * + {@code changeFilterMode}/{@code !filter} ({@link FilterToggleCommand}). Message prep is shared: lowercase,
 * strip the invisible tag, collapse runs of repeated chars, then a space-stripped copy for substring scans.
 * <p>Two modes, shared across all channels via {@link FilterModeState} (one {@code !filter} toggle affects
 * every channel): <b>AI</b> first short-circuits on a "not so bad" whitelist, then — if a bad-word fragment
 * is present — asks {@link GPT4o} to confirm before timing out (and sends GPT's explanation). <b>OLD</b> uses
 * a hard-coded whitelist + a big regex and a fixed "Мат в чате запрещён!" reply. Timeouts are 600s on main,
 * 5s on test.
 * <p>Drift preserved via {@link ChannelContext#isSandbox()}: test never skips the bot's own messages and its
 * bad-word list has the extra {@code eba/ebl/ebu} while its whitelist lacks main's {@code говн/насилов/хрен/хер};
 * main skips the bot's own messages. (The mode itself is no longer per-channel — it is shared.)
 */
@Component
public class FilterCommand implements ChatCommand {

    private static final Logger log = LoggerFactory.getLogger(FilterCommand.class);

    private static final List<String> BAD_WORDS = List.of("хуа", "хуе", "хуё", "хуи", "хуй", "хул", "хуу", "хуэ", "хую", "хуя",
            "еба", "ебб,", "ебе", "ебё", "еби", "ебк", "ебл", "ебн", "ебо", "ебс", "ебу", "ебц", "ебч", "ебщ", "ебъ", "ебы", "ебь", "ебэ", "ебю", "ебя",
            "аеб", "иеб", "йеб", "оеб", "уеб", "ъеб", "ыеб", "ьеб",
            "ёб",
            "эба", "эбб,", "эбе", "эбё", "эби", "эбк", "эбл", "эбн", "эбо", "эбс", "эбу", "эбц", "эбч", "эбщ", "эбъ", "эбы", "эбь", "эбэ", "эбю", "эбя",
            "пизд", "пезд", "пёзд", "пэзд",
            "писд", "песд", "пёсд", "пэсд",
            "педи", "педа", "педо", "педе", "педр",
            "пиди", "пида", "пидо", "пиде", "пидр",
            "бля", "бле", "блэ",
            "blya", "pizd", "hui");

    private static final List<String> NOT_SO_BAD = List.of("мудила", "мудак", "мудень", "мудозвон", "huecruch", "дебил", "пидиди");

    // Per-channel variants (see class javadoc): sandbox adds eba/ebl/ebu to bad words; main adds the softer
    // говн/насилов/хрен/хер to the whitelist.
    private static final List<String> BAD_WORDS_SANDBOX = concat(BAD_WORDS, "eba", "ebl", "ebu");
    private static final List<String> NOT_SO_BAD_MAIN = concat(NOT_SO_BAD, "говн", "насилов", "хрен", "хер");

    // OLD-mode regex, lifted verbatim from the original handler (do not retype — injected by build tooling).
    private static final Pattern OLD_FILTER_PATTERN = Pattern.compile("(?iu)\\b(([уyu]|[нзnz3][аa]|(хитро|не)?[вvwb][зz3]?[ыьъi]|[сsc][ьъ']|(и|[рpr][аa4])[зсzs]ъ?|([оo0][тбtb6]|[пp][оo0][дd9])[ьъ']?|(.\\B)+?[оаеиeo])?-?([еёe][бb6](?!о[рй])|и[пб][ае][тц]).*?|([нn][иеаaie]|([дпdp]|[вv][еe3][рpr][тt])[оo0]|[рpr][аa][зсzc3]|[з3z]?[аa]|с(ме)?|[оo0]([тt]|дно)?|апч)?-?[хxh][уuy]([яйиеёюuie]|ли(?!ган)).*?|([вvw][зы3z]|(три|два|четыре)жды|(н|[сc][уuy][кk])[аa])?-?[бb6][лl]([яy](?!(х|ш[кн]|мб)[ауеыио]).*?|[еэe][дтdt][ь']?)|([рp][аa][сзc3z]|[знzn][аa]|[соsc]|[вv][ыi]?|[пp]([еe][рpr][еe]|[рrp][оиioеe]|[оo0][дd])|и[зс]ъ?|[аоao][тt])?[пpn][иеёieu][зz3][дd9].*?|([зz3][аa])?[пp][иеieu][дd][аоеaoe]?[рrp](ну.*?|[оаoa][мm]|([аa][сcs])?([иiu]([лl][иiu])?[нщктлtlsn]ь?)?|([оo](ч[еиei])?|[аa][сcs])?[кk]([оo]й)?|[юu][гg])[ауеыauyei]?|[мm][аa][нnh][дd]([ауеыayueiи]([лl]([иi][сзc3щ])?[ауеыauyei])?|[оo][йi]|[аоao][вvwb][оo](ш|sh)[ь']?([e]?[кk][ауеayue])?|юк(ов|[ауи])?)|[мm][уuy][дd6]([яyаиоaiuo0].*?|[еe]?[нhn]([ьюия'uiya]|ей))|мля([тд]ь)?|лять|([нз]а|по)х|м[ао]л[ао]фь([яию]|[её]й))\\b");

    private final GPT4o gpt4o;
    private final FilterModeState filterModeState;

    @Autowired
    public FilterCommand(GPT4o gpt4o, FilterModeState filterModeState) {
        this.gpt4o = gpt4o;
        this.filterModeState = filterModeState;
    }

    private static List<String> concat(List<String> base, String... extra) {
        List<String> list = new ArrayList<>(base);
        list.addAll(Arrays.asList(extra));
        return List.copyOf(list);
    }

    @Override
    public boolean matches(ChannelMessageEvent event, ChannelContext ctx) {
        // Main skips the bot's own messages; test did not.
        return ctx.isSandbox() || !event.getUser().getName().equals(ctx.getBotAccountName());
    }

    @Override
    public void execute(ChannelMessageEvent event, ChannelContext ctx) {
        String newMessage = event.getMessage().toLowerCase();
        newMessage = newMessage.replace(TwitchText.INVISIBLE_TAG, "");
        newMessage = newMessage.replaceAll("(.)\\1+", "$1");
        String trimmedNewMessage = newMessage.replace(" ", "");

        List<String> notSoBad = ctx.isSandbox() ? NOT_SO_BAD : NOT_SO_BAD_MAIN;
        List<String> badWords = ctx.isSandbox() ? BAD_WORDS_SANDBOX : BAD_WORDS;

        if (filterModeState.getMode() == FilterMode.AI) {
            try {
                if (notSoBad.stream().anyMatch(trimmedNewMessage::contains)) {
                    // whitelisted — ignore
                } else if (badWords.stream().anyMatch(trimmedNewMessage::contains)) {
                    boolean isBadWord = gpt4o.isTextContainingBadWord(newMessage);
                    log.debug("isBadWord: {}", isBadWord);
                    if (isBadWord) {
                        String textAboutBadWord = gpt4o.TextContainingBadWord(newMessage);
                        ctx.send("@" + event.getUser().getName() + " " + textAboutBadWord);
                        ctx.timeoutById(event.getUser().getId(), ctx.isSandbox() ? 5 : 600, "bad word bot");
                    }
                }
            } catch (IOException e) {
                log.error("timeoutForMat failed", e);
            }
        } else {
            if (newMessage.contains("мудила") || newMessage.contains("мудак") || newMessage.contains("мудень")
                    || newMessage.contains("мудозвон") || newMessage.contains("huecruch") || newMessage.contains("дебил")) {
                // whitelisted — ignore
            } else if (OLD_FILTER_PATTERN.matcher(newMessage).find()) {
                try {
                    ctx.send("@" + event.getUser().getName() + " Мат в чате запрещён!");
                    ctx.timeoutById(event.getUser().getId(), ctx.isSandbox() ? 5 : 600, "bad word bot");
                } catch (IOException e) {
                    log.error("timeoutForMat (old filter) failed", e);
                }
            }
        }
    }
}
