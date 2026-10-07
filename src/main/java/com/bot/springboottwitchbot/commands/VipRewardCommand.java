package com.bot.springboottwitchbot.commands;

import com.bot.springboottwitchbot.utilities.UtilityCommandsMainChannel;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Channel-points reward "Я персона VIP VIP": grants the redeemer VIP for 24 hours, then removes it.
 * Migrated from {@code vipUserForPoints}.
 */
@Component
public class VipRewardCommand implements RewardCommand {

    private static final Logger log = LoggerFactory.getLogger(VipRewardCommand.class);

    private static final String REWARD_TITLE = "Я персона VIP VIP";

    @Override
    public boolean matches(RewardRedeemedEvent event, ChannelContext ctx) {
        ChannelPointsReward reward = event.getRedemption().getReward();
        log.debug("channel points reward: {}", reward);
        return reward.getTitle().toLowerCase().equalsIgnoreCase(REWARD_TITLE);
    }

    @Override
    public void execute(RewardRedeemedEvent event, ChannelContext ctx) throws IOException {
        ChannelPointsRedemption redemption = event.getRedemption();
        String userLogin = redemption.getUser().getLogin();
        UtilityCommandsMainChannel.vipUser(userLogin);
        Thread unVIPTimer = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(86400 * 1000L);
                    UtilityCommandsMainChannel.unVipUser(userLogin);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        unVIPTimer.start();
    }
}
