package com.rong.rt.common.loaders.recipes;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.rong.rt.api.utils.Utility;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CustomItemReturnShapedOreRecipe extends ShapedOreRecipe implements IRecipe {

	private final Predicate<ItemStack> itemsToReturn;

	public CustomItemReturnShapedOreRecipe(ResourceLocation group, @Nonnull ItemStack result,
			Predicate<ItemStack> itemsToReturn, Object... recipe) {
		super(group, result, recipe);
		this.itemsToReturn = itemsToReturn;
	}

	public CustomItemReturnShapedOreRecipe(ResourceLocation group, @Nonnull ItemStack result, ShapedPrimer primer,
			Predicate<ItemStack> itemsToReturn) {
		super(group, result, primer);
		this.itemsToReturn = itemsToReturn;
	}

	public boolean shouldItemReturn(ItemStack itemStack) {
		return itemsToReturn.test(itemStack);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> remainingItems = super.getRemainingItems(inv);
		for(int i = 0; i < remainingItems.size(); i++) {
			if(!remainingItems.get(i).isEmpty()) continue;
			ItemStack stackInSlot = inv.getStackInSlot(i);
			// if specified item should be returned back, copy it with amount 1 and add to
			// remaining items
			if(shouldItemReturn(stackInSlot)) {
				ItemStack remainingItem = Utility.copyAmount(1, stackInSlot);
				remainingItems.set(i, remainingItem);
			}
		}
		return remainingItems;
	}
}