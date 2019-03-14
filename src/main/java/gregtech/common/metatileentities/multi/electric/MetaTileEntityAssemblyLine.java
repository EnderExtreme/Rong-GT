package gregtech.common.metatileentities.multi.electric;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockMachineCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockMetalCasing.MetalCasingType;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.BlockMultiblockCasing.MultiblockCasingType;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class MetaTileEntityAssemblyLine extends RecipeMapMultiblockController {
	public MetaTileEntityAssemblyLine(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId, RecipeMaps.ASSEMBLY_LINE_RECIPES);
	}

	@Override
	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new MetaTileEntityAssemblyLine(metaTileEntityId);
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start(LEFT, DOWN, BACK)
				.aisle("#Y#", "GAG", "RTR", "COC")
				.aisle("#Y#", "GAG", "RTR", "FIF")
				.setRepeatable(3, 14).aisle("#Y#", "GSG", "RTR", "FIF")
				.where('S', selfPredicate())
				.where('C', statePredicate(getCasingState()))
				.where('F', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.IMPORT_FLUIDS)))
				.where('O', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.EXPORT_ITEMS)))
				.where('Y', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.INPUT_ENERGY)))
				.where('I', tilePredicate((state, tile) -> {
					return tile.metaTileEntityId.equals(MetaTileEntities.ITEM_IMPORT_BUS[0].metaTileEntityId);
					}))
				.where('G', statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(MultiblockCasingType.GRATE_CASING)))
				.where('A', statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(MultiblockCasingType.ASSEMBLER_CASING)))
				.where('R', statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(MultiblockCasingType.REINFORCED_GLASS)))
				.where('T', statePredicate(MetaBlocks.METAL_CASING.getState(MetalCasingType.TUNGSTENSTEEL_ROBUST)))
				.where('#', (tile) -> {
			return true;
		})
				.build();
	}

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return Textures.SOLID_STEEL_CASING;
	}

	protected IBlockState getCasingState() {
		return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID);
	}
}