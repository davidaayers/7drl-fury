package com.wwflgames.fury.item.neffect.damage;

public abstract class Damage {

    public static final Damage CRUSH_DAMAGE = new CrushDamage();
    public static final Damage SLASH_DAMAGE = new SlashDamage();
    public static final Damage STAB_DAMAGE = new StabDamage();

    public static final Damage[] ALL_DAMAGE_TYPES = {
            CRUSH_DAMAGE,
            SLASH_DAMAGE,
            STAB_DAMAGE
    };

    public static Damage forType(String type) {
        for (Damage damage : ALL_DAMAGE_TYPES) {
            if (type.equalsIgnoreCase(damage.getType())) {
                return damage;
            }
        }
        return null;
    }

    private String type;

    public Damage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
