package me.zeroeightsix.serversimplified.commands;

public class HealCommand extends PlayerActionCommand {

    public HealCommand() {
        super(player -> player.setHealth(20f), o -> "Healed");
    }

    @Override
    protected String getName() {
        return "heal";
    }

}
