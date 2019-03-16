package me.zeroeightsix.serversimplified.commands;

public class FeedCommand extends PlayerActionCommand {

    public FeedCommand() {
        super(player -> player.getHungerManager().setFoodLevel(20), o -> "Fed");
    }

    @Override
    protected String getName() {
        return "feed";
    }
}
