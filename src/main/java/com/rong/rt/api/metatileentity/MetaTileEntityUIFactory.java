package com.rong.rt.api.metatileentity;

import com.rong.rt.Values;
import com.rong.rt.api.gui.ModularUI;
import com.rong.rt.api.gui.UIFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * {@link UIFactory} implementation for {@link MetaTileEntity}
 */
public class MetaTileEntityUIFactory extends UIFactory<MetaTileEntityHolder> {

    public static final MetaTileEntityUIFactory INSTANCE = new MetaTileEntityUIFactory();
    private MetaTileEntityUIFactory() {}

    public void init() {
        UIFactory.FACTORY_REGISTRY.register(0, new ResourceLocation(Values.MOD_ID, "meta_tile_entity_factory"), this);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ModularUI createUITemplate(MetaTileEntityHolder holder, EntityPlayer entityPlayer) {
        return holder.getMetaTileEntity().createUI(entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected MetaTileEntityHolder readHolderFromSyncData(PacketBuffer syncData) {
        return (MetaTileEntityHolder) Minecraft.getMinecraft().world.getTileEntity(syncData.readBlockPos());
    }

    @Override
    protected void writeHolderToSyncData(PacketBuffer syncData, MetaTileEntityHolder holder) {
        syncData.writeBlockPos(holder.getPos());
    }

}
