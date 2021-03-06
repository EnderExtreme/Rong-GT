package com.rong.rt.common.blocks;

import com.rong.rt.api.unification.stonetypes.StoneType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OreItemBlock extends ItemBlock {

	private BlockOre oreBlock;

	public OreItemBlock(BlockOre oreBlock) {
		super(oreBlock);
		this.oreBlock = oreBlock;
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@SuppressWarnings("deprecation")
	protected IBlockState getBlockState(ItemStack stack) {
		return oreBlock.getStateFromMeta(getMetadata(stack.getItemDamage()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		IBlockState blockState = getBlockState(stack);
		StoneType stoneType = blockState.getValue(oreBlock.STONE_TYPE);
		return stoneType.processingPrefix.getLocalNameForItem(oreBlock.material);
	}

}
