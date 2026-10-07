package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Channel-points reward "ЕMОTЕMОDЕ" (title mixes Cyrillic/Latin lookalikes — escaped below to match
 * exactly what Twitch sends): turns on emote-only chat for 5 minutes, then off. Migrated from
 * {@code emoteOnlyForPoints}.
 */
@Component
public class EmoteOnlyRewardCommand implements RewardCommand {

    private static final Logger log = LoggerFactory.getLogger(EmoteOnlyRewardCommand.class);

    // "ЕMОTЕMОDЕ" = U+0415 004D 041E 0054 0415 004D 041E 0044 0415
    private static final String REWARD_TITLE = "ЕMОTЕMОDЕ";

    @Override
    public boolean matches(RewardRedeemedEvent event, ChannelContext ctx) {
        ChannelPointsReward reward = event.getRedemption().getReward();
        log.debug("channel points reward: {}", reward);
        return reward.getTitle().toLowerCase().equalsIgnoreCase(REWARD_TITLE);
    }

    @Override
    public void execute(RewardRedeemedEvent event, ChannelContext ctx) throws IOException {
        UtilityCommandsMainChannel.emoteOnlyMode(true);
        Thread emoteOffTimer = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300 * 1000L);
                    UtilityCommandsMainChannel.emoteOnlyMode(false);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        emoteOffTimer.start();
    }
}
