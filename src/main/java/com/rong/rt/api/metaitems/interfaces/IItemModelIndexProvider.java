package com.rong.rt.api.metaitems.interfaces;

import net.minecraft.item.ItemStack;

public interface IItemModelIndexProvider extends IMetaItemStats {

    int getModelIndex(ItemStack itemStack);
}
