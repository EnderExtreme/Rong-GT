package com.rong.rt.api.unification.materials.types;

import com.google.common.collect.ImmutableList;
import com.rong.rt.api.unification.OreDictUnifier;
import com.rong.rt.api.unification.materials.MaterialIconSet;

/**
 * MarkerMaterial is type of material used for generic things like material re-registration and use in recipes
 * Marker material cannot be used to generate any meta items
 * Marker material can be used only for marking other materials (re-registering) equal to it and then using it in recipes or in getting items
 * Marker material is not presented in material registry and cannot be used for persistence
 */

public final class MarkerMaterial extends Material {

    private final String name;

    public MarkerMaterial(String name) {
        super(-1, name,
                0xFFFFFF,
                MaterialIconSet.NONE,
                ImmutableList.of(),
                0,
                null);
        this.name = name;
        OreDictUnifier.registerMarkerMaterial(this);
    }

    @Override
    protected void registerMaterial(int metaItemSubId, String name) {
    }

    @Override
    //since we're not registered, return overriden name
    public String toString() {
        return name;
    }
}
