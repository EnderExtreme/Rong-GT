package com.rong.rt.integration.jei.utils;

import java.util.List;

import com.rong.rt.common.loaders.recipes.CustomItemReturnShapedOreRecipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import mezz.jei.gui.CraftingGridHelper;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.startup.ForgeModIdHelper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class CustomItemReturnRecipeWrapper extends ShapedOreRecipeWrapper implements ICustomCraftingRecipeWrapper {

	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;

	private CraftingGridHelper craftingGridHelper;
	private CustomItemReturnShapedOreRecipe customRecipe;

	public CustomItemReturnRecipeWrapper(IJeiHelpers jeiHelpers, CustomItemReturnShapedOreRecipe recipe) {
		super(jeiHelpers, recipe);
		this.customRecipe = recipe;
		this.craftingGridHelper = new CraftingGridHelper(craftInputSlot1, craftOutputSlot);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);

		craftingGridHelper.setInputs(guiItemStacks, inputs, getWidth(), getHeight());
		guiItemStacks.set(craftOutputSlot, outputs.get(0));

		ResourceLocation registryName = getRegistryName();
		guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if(slotIndex == craftOutputSlot && registryName != null) {
				String recipeModId = registryName.getResourceDomain();

				boolean modIdDifferent = false;
				ResourceLocation itemRegistryName = ingredient.getItem().getRegistryName();
				if(itemRegistryName != null) {
					String itemModId = itemRegistryName.getResourceDomain();
					modIdDifferent = !recipeModId.equals(itemModId);
				}

				if(modIdDifferent) {
					String modName = ForgeModIdHelper.getInstance().getFormattedModNameForModId(recipeModId);
					tooltip.add(TextFormatting.GRAY
							+ Translator.translateToLocalFormatted("jei.tooltip.recipe.by", modName));
				}

				boolean showAdvanced = Minecraft.getMinecraft().gameSettings.advancedItemTooltips
						|| GuiScreen.isShiftKeyDown();
				if(showAdvanced) {
					tooltip.add(TextFormatting.GRAY + registryName.getResourcePath());
				}
			}

			if(slotIndex != craftOutputSlot) {
				if(customRecipe.shouldItemReturn(ingredient)) {
					tooltip.add(I18n.format("gregtech.recipe.not_consumed"));
				}
			}
		});
	}
}
