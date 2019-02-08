package gregtech.common.blocks;

import java.util.Arrays;

import gregtech.api.GregTechAPI;
import gregtech.api.util.GTUtility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public class StoneBlock<T extends Enum<T> & IStringSerializable> extends Block {

    private static PropertyEnum<ChiselingVariant> CHISELING_VARIANT = PropertyEnum.create("chiseling", ChiselingVariant.class);

    private PropertyEnum<T> VARIANT;
    private T[] VALUES;

    public StoneBlock(Material materialIn) {
        super(materialIn);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        for(T variant : VALUES) {
            for(ChiselingVariant chiselingVariant : ChiselingVariant.values()) {
                list.add(getItemVariant(variant, chiselingVariant));
            }
        }
    }
    
    public T[] getVariantValues() {
        return Arrays.copyOf(VALUES, VALUES.length);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    public T getVariant(IBlockState blockState) {
        return blockState.getValue(VARIANT);
    }

    public ChiselingVariant getChiselingVariant(IBlockState blockState) {
        return blockState.getValue(CHISELING_VARIANT);
    }

    public IBlockState withVariant(T variant, ChiselingVariant chiselingVariant) {
        return getDefaultState().withProperty(VARIANT, variant).withProperty(CHISELING_VARIANT, chiselingVariant);
    }

    public ItemStack getItemVariant(T variant, ChiselingVariant chiselingVariant) {
    	return getItemVariant(variant, chiselingVariant, 1);
    }
    
    public ItemStack getItemVariant(T variant, ChiselingVariant chiselingVariant, int amount) {
        return new ItemStack(this, amount, chiselingVariant.ordinal() * 4 + variant.ordinal());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        Class<T> enumClass = GTUtility.getActualTypeParameter(getClass(), StoneBlock.class, 0);
        this.VARIANT = PropertyEnum.create("variant", enumClass);
        this.VALUES = enumClass.getEnumConstants();
        return new BlockStateContainer(this, VARIANT, CHISELING_VARIANT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CHISELING_VARIANT, ChiselingVariant.values()[meta / 4]).withProperty(VARIANT, VALUES[meta % 4]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(CHISELING_VARIANT).ordinal() * 4 + state.getValue(VARIANT).ordinal();
    }

    public enum ChiselingVariant implements IStringSerializable {

        NORMAL("normal"),
        CRACKED("cracked"),
        MOSSY("mossy"),
        CHISELED("chiseled");

        private final String name;

        ChiselingVariant(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

}
