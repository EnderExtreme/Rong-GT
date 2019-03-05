package gregtech.common.blocks;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.DustMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDrillHead extends VariantBlock<BlockDrillHead.DrillHeadType> {

    public BlockDrillHead() {
        super(Material.IRON);
        setUnlocalizedName("drill_head");
        setHardness(5.0f);
        setResistance(10.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("wrench", 2);
        setDefaultState(getState(DrillHeadType.DIAMOND));
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
        return false;
    }

    public enum DrillHeadType implements IStringSerializable {

    	//Base speed of each operation would be 320
    	//Speed is in 'percentage to discount for speed, energy will be slightly less than tier specified'
    	STEEL("steel", 1, Materials.Steel, GTValues.V[GTValues.MV]),
        DIAMOND("diamond", 5, Materials.Diamond, GTValues.V[GTValues.HV]),
        TUNGSTEN("tungsten", 10, Materials.Tungsten, GTValues.V[GTValues.EV]),
        TITANIUM("titanium", 20, Materials.Titanium, GTValues.V[GTValues.IV]),
    	ADAMANTIUM("adamantium", 30, Materials.Adamantium, GTValues.V[GTValues.LuV]),
    	OSMIRIDIUM("osmiridium", 60, Materials.Osmiridium, GTValues.V[GTValues.UV]);

        private final String name;
        private final int speed;
        private final DustMaterial material;
        private final long energyLevel;

        DrillHeadType(String name, int speed, DustMaterial material, long energyLevel) {
            this.name = name;
            this.speed = speed;
            this.material = material;
            this.energyLevel = energyLevel;
        }

        @Override
        public String getName() {
            return this.name;
        }
        
        public int getSpeed() {
        	return this.speed;
        }
        
        public DustMaterial getMaterial() {
            return material;
        }
        
        public long getEnergyLevel() {
        	return this.energyLevel;
        }

    }

}
