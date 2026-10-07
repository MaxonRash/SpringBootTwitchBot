package com.bot.springboottwitchbot.commands;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.pubsub.domain.SubscriptionData;
import com.github.twitch4j.pubsub.events.ChannelSubscribeEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Dispatches the main channel's PubSub events — channel-points redemptions and subscriptions. Registered
 * only by {@code MainChannel} (the test client never subscribes to these topics), so it is the PubSub
 * counterpart to {@link ChatEventDispatcher}. For a redemption it runs every {@link RewardCommand} whose
 * {@link RewardCommand#matches} returns true (a failure in one is logged and doesn't stop the others);
 * the single sub-notification behavior is handled inline. Not a Spring {@code @Component}: it is
 * constructed with the channel's {@link ChannelContext} and the Spring-collected list of reward commands.
 */
public class PubSubEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(PubSubEventDispatcher.class);

    private final List<RewardCommand> rewardCommands;
    private final ChannelContext context;

    public PubSubEventDispatcher(List<RewardCommand> rewardCommands, ChannelContext context) {
        this.rewardCommands = rewardCommands;
        this.context = context;
    }

    @EventSubscriber
    public void onRewardRedeemed(RewardRedeemedEvent event) {
        for (RewardCommand command : rewardCommands) {
            try {
                if (command.matches(event, context)) {
                    command.execute(event, context);
                }
            } catch (Exception e) {
                log.error("Reward command {} failed", command.getClass().getSimpleName(), e);
            }
        }
    }

    @EventSubscriber
    public void onChannelSubscribe(ChannelSubscribeEvent event) {
        SubscriptionData subscriptionData = event.getData();
        log.info("Sub note Works");
        if (subscriptionData.getDisplayName().equalsIgnoreCase(context.getChannelName())) {
            context.send("@" + context.getChannelName() + " найс катаешь Kappa");
        } else {
            context.send("@" + subscriptionData.getDisplayName() +
                    " peepoClap peepoClap peepoClap peepoClap");
        }
    }
}
