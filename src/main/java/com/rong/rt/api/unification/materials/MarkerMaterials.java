package com.rong.rt.api.unification.materials;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.rong.rt.api.unification.materials.types.MarkerMaterial;
import com.rong.rt.api.unification.materials.types.Material;
import com.rong.rt.api.unification.materials.MaterialIconSet;

import net.minecraft.item.EnumDyeColor;

public class MarkerMaterials {

	@SuppressWarnings("ResultOfMethodCallIgnored")
    public static void register() {
        Color.Colorless.toString();
        Tier.Basic.toString();
        Empty.toString();
    }

    /**
     * Marker materials without category
     */
    public static MarkerMaterial Empty = new MarkerMaterial("empty");

    /**
     * Color materials
     */
    public static class Color {

        /**
         * Can be used only by direct specifying
         * Means absence of color on OrePrefix
         * Often a default value for color prefixes
         */
        public static MarkerMaterial Colorless = new MarkerMaterial("colorless");

        public static MarkerMaterial Black = new MarkerMaterial("black");
        public static MarkerMaterial Red = new MarkerMaterial("red");
        public static MarkerMaterial Green = new MarkerMaterial("green");
        public static MarkerMaterial Brown = new MarkerMaterial("brown");
        public static MarkerMaterial Blue = new MarkerMaterial("blue");
        public static MarkerMaterial Purple = new MarkerMaterial("purple");
        public static MarkerMaterial Cyan = new MarkerMaterial("cyan");
        public static MarkerMaterial Silver = new MarkerMaterial("silver");
        public static MarkerMaterial Gray = new MarkerMaterial("gray");
        public static MarkerMaterial Pink = new MarkerMaterial("pink");
        public static MarkerMaterial Lime = new MarkerMaterial("lime");
        public static MarkerMaterial Yellow = new MarkerMaterial("yellow");
        public static MarkerMaterial LightBlue = new MarkerMaterial("light_blue");
        public static MarkerMaterial Magenta = new MarkerMaterial("magenta");
        public static MarkerMaterial Orange = new MarkerMaterial("orange");
        public static MarkerMaterial White = new MarkerMaterial("white");

        /**
         * Arrays containing all possible color values (without Colorless!)
         */
        public static final MarkerMaterial[] VALUES = new MarkerMaterial[] {
                Black, Red, Green, Brown, Blue, Purple, Cyan, Silver, Gray, Pink, Lime, Yellow, LightBlue, Magenta, Orange, White
        };

        /**
         * Gets color by it's name
         * Name format is equal to EnumDyeColor
         */
        public static MarkerMaterial valueOf(String string) {
            for(MarkerMaterial color : VALUES) {
                if(color.toString().equals(string)) {
                    return color;
                }
            }
            return null;
        }

        /**
         * Contains associations between MC EnumDyeColor and Color MarkerMaterial
         */
        public static final HashBiMap<EnumDyeColor, MarkerMaterial> COLORS = HashBiMap.create();

        static {
            for(EnumDyeColor color : EnumDyeColor.values()) {
                COLORS.put(color, Color.valueOf(color.getName()));
            }
        }

    }

    /**
     * Circuitry, batteries and other technical things
     */
    public static class Tier {
        public static Material Basic = new MarkerMaterial("basic");
        public static Material Intermediate = new MarkerMaterial("intermediate");
        public static Material Advanced = new MarkerMaterial("advanced");
        public static Material Elite = new MarkerMaterial("elite");
        public static Material Master = new MarkerMaterial("master");
        public static Material Expert = new MarkerMaterial("expert");
        public static Material Ultimate = new MarkerMaterial("ultimate");
        public static Material Superconductor = new Material(900, "superconductor", 0xFFFFFF, MaterialIconSet.NONE, ImmutableList.of(), 0L, null) {};
        public static Material Data = new MarkerMaterial("data");
        public static Material Magic = new MarkerMaterial("magic");
    }
}