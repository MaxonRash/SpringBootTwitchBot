package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Channel-points reward "ТАЙМАЧ БРАТУЗЕ!": times out the user named in the redemption input for 180s.
 * Migrated from {@code timeoutUserForPoints}.
 */
@Component
public class TimeoutRewardCommand implements RewardCommand {

    private static final Logger log = LoggerFactory.getLogger(TimeoutRewardCommand.class);

    private static final String REWARD_TITLE = "ТАЙМАЧ БРАТУЗЕ!";

    @Override
    public boolean matches(RewardRedeemedEvent event, ChannelContext ctx) {
        ChannelPointsReward reward = event.getRedemption().getReward();
        log.debug("channel points reward: {}", reward);
        return reward.getTitle().toLowerCase().equalsIgnoreCase(REWARD_TITLE);
    }

    @Override
    public void execute(RewardRedeemedEvent event, ChannelContext ctx) throws Exception {
        ChannelPointsRedemption redemption = event.getRedemption();
        String userToTimeout = redemption.getUserInput().split(" ")[0];
        if (userToTimeout.startsWith("@")) {
            userToTimeout = userToTimeout.substring(1);
        }
        ctx.timeoutByName(userToTimeout, 180, "Timeout for points");
    }
}
