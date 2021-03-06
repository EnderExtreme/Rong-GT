package com.rong.rt.common.items.tools;

import net.minecraft.item.ItemStack;

public class ToolMortar extends ToolBase {

    @Override
    public int getToolDamagePerBlockBreak(ItemStack stack) {
        return 1;
    }

    @Override
    public int getToolDamagePerContainerCraft(ItemStack stack) {
        return 5;
    }

    @Override
    public float getBaseDamage(ItemStack stack) {
        return 2.0F;
    }

}
