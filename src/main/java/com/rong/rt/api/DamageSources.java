package com.rong.rt.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class DamageSources {

    private static DamageSource EXPLOSION = new DamageSource("explosion").setExplosion();
    private static DamageSource HEAT = new DamageSource("heat").setDamageBypassesArmor();
    private static DamageSource FROST = new DamageSource("frost").setDamageBypassesArmor();
    private static DamageSource ELECTRIC = new DamageSource("electric").setDamageBypassesArmor();
    private static DamageSource RADIATION = new DamageSource("radiation").setDamageBypassesArmor();
    private static DamageSource TURBINE = new DamageSource("turbine");

    public static DamageSource getElectricDamage() {
        return ELECTRIC;
    }

    public static DamageSource getRadioactiveDamage() {
        return RADIATION;
    }

    public static DamageSource getExplodingDamage() {
        return EXPLOSION;
    }

    public static DamageSource getHeatDamage() {
        return HEAT;
    }

    public static DamageSource getFrostDamage() {
        return FROST;
    }

    public static DamageSource getTurbineDamage() {
        return TURBINE;
    }
    
    public static DamageSource causeCombatDamage(String type, EntityLivingBase damager) {
        return new EntityDamageSource(type, damager);
    }

}