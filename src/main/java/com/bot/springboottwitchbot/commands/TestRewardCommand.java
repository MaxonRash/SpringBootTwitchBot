package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Owner-only test reward "-1000": times out a fixed test account for 10s. Migrated from
 * {@code TestForPoints}. Gated on {@link ChannelContext#isOwner(String)} of the redeemer's display name.
 */
@Component
public class TestRewardCommand implements RewardCommand {

    private static final Logger log = LoggerFactory.getLogger(TestRewardCommand.class);

    private static final String REWARD_TITLE = "-1000";

    @Override
    public boolean matches(RewardRedeemedEvent event, ChannelContext ctx) {
        ChannelPointsRedemption redemption = event.getRedemption();
        ChannelPointsReward reward = redemption.getReward();
        log.debug("channel points reward: {}", reward);
        return reward.getTitle().toLowerCase().equalsIgnoreCase(REWARD_TITLE)
                && ctx.isOwner(redemption.getUser().getDisplayName());
    }

    @Override
    public void execute(RewardRedeemedEvent event, ChannelContext ctx) throws Exception {
        ctx.timeoutByName("steyro", 10, "Timeout for points test");
    }
}
