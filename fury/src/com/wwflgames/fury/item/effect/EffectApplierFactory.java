package com.wwflgames.fury.item.effect;

import java.util.HashMap;
import java.util.Map;

public class EffectApplierFactory {

    Map<DamageType, DamageApplier> damageAppliers = new HashMap<DamageType, DamageApplier>();

    public EffectApplierFactory() {
        damageAppliers.put(DamageType.MELEE_CRUSH, new CrushDamageApplier());
    }

    public EffectApplier applierFor(ItemEffect effect) {

        if (effect instanceof Damage) {
            return damageApplierFor((Damage) effect);
        }

        return null;
    }

    private EffectApplier damageApplierFor(Damage damage) {
        return damageAppliers.get(damage.getType());

    }

}
