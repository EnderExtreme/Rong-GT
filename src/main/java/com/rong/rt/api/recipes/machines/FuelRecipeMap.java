package com.rong.rt.api.recipes.machines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rong.rt.api.recipes.FluidKey;
import com.rong.rt.api.recipes.recipes.FuelRecipe;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

public class FuelRecipeMap {

	private static final List<FuelRecipeMap> RECIPE_MAPS = new ArrayList<>();

	public final String unlocalizedName;

	private final Map<FluidKey, FuelRecipe> recipeFluidMap = new HashMap<>();
	private final List<FuelRecipe> recipeList = new ArrayList<>();

	public FuelRecipeMap(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		RECIPE_MAPS.add(this);
	}

	@ZenGetter("recipeMaps")
	public static List<FuelRecipeMap> getRecipeMaps() {
		return RECIPE_MAPS;
	}

	@ZenMethod
	public void addRecipe(FuelRecipe fuelRecipe) {
		FluidKey fluidKey = new FluidKey(fuelRecipe.getRecipeFluid());
		if(recipeFluidMap.containsKey(fluidKey)) {
			FuelRecipe oldRecipe = recipeFluidMap.remove(fluidKey);
			recipeList.remove(oldRecipe);
		}
		recipeFluidMap.put(fluidKey, fuelRecipe);
		recipeList.add(fuelRecipe);
	}

	@ZenMethod
	public boolean removeRecipe(FuelRecipe recipe) {
		if(recipeList.contains(recipe)) {
			this.recipeList.remove(recipe);
			this.recipeFluidMap.remove(new FluidKey(recipe.getRecipeFluid()));
			return true;
		}
		return false;
	}

	public FuelRecipe findRecipe(long maxVoltage, FluidStack inputFluid) {
		if(inputFluid == null) return null;
		FluidKey fluidKey = new FluidKey(inputFluid);
		FuelRecipe fuelRecipe = recipeFluidMap.get(fluidKey);
		return fuelRecipe != null && fuelRecipe.matches(maxVoltage, inputFluid) ? fuelRecipe : null;
	}

	@ZenMethod("findRecipe")
	@Method(modid = "crafttweaker")
	public FuelRecipe ctFindRecipe(long maxVoltage, ILiquidStack inputFluid) {
		return findRecipe(maxVoltage, CraftTweakerMC.getLiquidStack(inputFluid));
	}

	@ZenGetter("recipes")
	public List<FuelRecipe> getRecipeList() {
		return Collections.unmodifiableList(recipeList);
	}

	@SideOnly(Side.CLIENT)
	@ZenGetter("localizedName")
	public String getLocalizedName() {
		return I18n.format("recipemap." + unlocalizedName + ".name");
	}

	@ZenGetter("unlocalizedName")
	public String getUnlocalizedName() {
		return unlocalizedName;
	}

}
