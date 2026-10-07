package com.bot.springboottwitchbot.commands;

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;

/**
 * A channel-points reward handler, analogous to {@link ChatCommand} but for {@link RewardRedeemedEvent}
 * (delivered via PubSub). Only the main channel subscribes to the channel-points topic (see
 * {@code MainChannel.run()}), so these run on main only. The {@link PubSubEventDispatcher} runs every
 * reward command whose {@link #matches} returns true for a redemption.
 */
public interface RewardCommand {

    boolean matches(RewardRedeemedEvent event, ChannelContext ctx);

    void execute(RewardRedeemedEvent event, ChannelContext ctx) throws Exception;
}
