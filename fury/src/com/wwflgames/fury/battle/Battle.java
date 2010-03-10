package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.List;

public class Battle {

    private Mob player;
    private List<Mob> enemies;
    private List<Mob> originalEnemies;
    private List<Mob> allBattleParticipants;
    private boolean playerInitiate;

    public Battle(Mob player, List<Mob> enemies, boolean playerInitiate) {
        this.player = player;
        this.enemies = enemies;
        this.playerInitiate = playerInitiate;
        originalEnemies = new ArrayList<Mob>();
        originalEnemies.addAll(enemies);
        allBattleParticipants = new ArrayList<Mob>();
        allBattleParticipants.add(player);
        allBattleParticipants.addAll(enemies);
    }

    public Mob getPlayer() {
        return player;
    }

    public List<Mob> getEnemies() {
        return enemies;
    }

    public List<Mob> getOriginalEnemies() {
        return originalEnemies;
    }

    public boolean isPlayerInitiate() {
        return playerInitiate;
    }

    public List<Mob> getAllBattleParticipants() {
        return allBattleParticipants;
    }

    public void removeEnemy(Mob enemyToRemove) {
        enemies.remove(enemyToRemove);   
    }

    public boolean allEnemiesDead() {
        return enemies.isEmpty();
    }
}
