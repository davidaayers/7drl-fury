package com.wwflgames.fury.player;

import com.wwflgames.fury.util.Log;

public class PlayerFactory {

    private ProfessionFactory professionFactory;

    public PlayerFactory(ProfessionFactory professionFactory) {
        this.professionFactory = professionFactory;
    }

    public Player createForProfession(Profession profession) {
        Log.debug("Profession chosen: " + profession);
        Player player = new Player(profession.getName(), profession);
        player.installDeck(1, profession.getStarterDeck());
        player.setDefaultDeck(1);
        profession.installStarterStatsOnPlayer(player);
        return player;
    }

}
