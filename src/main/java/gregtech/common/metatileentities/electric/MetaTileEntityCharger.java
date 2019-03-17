package gregtech.common.metatileentities.electric;

import gregtech.api.GTValues;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IElectricItem;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.ModularUI.Builder;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.TieredMetaTileEntity;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MetaTileEntityCharger extends TieredMetaTileEntity {

    private final int inventorySize;

    public MetaTileEntityCharger(ResourceLocation metaTileEntityId, int tier, int inventorySize) {
        super(metaTileEntityId, tier);
        this.inventorySize = inventorySize;
        initializeInventory();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityCharger(metaTileEntityId, getTier(), inventorySize);
    }

    @Override
    public void update() {
        super.update();
        if(!getWorld().isRemote && energyContainer.getEnergyStored() > 0) {
            long inputVoltage = Math.min(energyContainer.getInputVoltage(), energyContainer.getEnergyStored());
            long energyUsedUp = 0L;
            for(int i = 0; i < importItems.getSlots(); i++) {
                ItemStack batteryStack = importItems.getStackInSlot(i);
                IElectricItem electricItem = batteryStack.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
                if(electricItem != null && electricItem.charge(inputVoltage, getTier(), false, true) > 0) {
                    energyUsedUp += electricItem.charge(inputVoltage, getTier(), false, false);
                    importItems.setStackInSlot(i, batteryStack);
                    if(energyUsedUp >= energyContainer.getEnergyStored()) break;
                }
                if(batteryStack.isItemEqual(OreDictUnifier.get(OrePrefix.crystal, Materials.CertusQuartz)) && energyContainer.getEnergyCanBeInserted() > 10000) {
                	energyContainer.removeEnergy(10000);
                	importItems.extractItem(i, 1, false);
                	importItems.setStackInSlot(i, OreDictUnifier.get(OrePrefix.crystal, Materials.ChargedCertusQuartz));
                }
            }
            if(energyUsedUp > 0) {
                energyContainer.changeEnergy(-energyUsedUp);
            }
        }
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return new ItemStackHandler(inventorySize) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                IElectricItem electricItem = stack.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
                if(electricItem == null || electricItem.getTier() != getTier() ||
                    electricItem.charge(Long.MAX_VALUE, getTier(), false, true) == 0)
                    return stack;
                if(stack.isItemEqual(OreDictUnifier.get(OrePrefix.crystal, Materials.CertusQuartz)))
                	return stack;
                
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new ItemStackHandler(0);
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        this.itemInventory = importItems;
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        int rowSize = (int) Math.sqrt(inventorySize);
        Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176,
            18 + 18 * rowSize + 94)
            .label(10, 5, getMetaFullName());

        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int index = y * rowSize + x;
                builder.widget(new SlotWidget(importItems, index, 89 - rowSize * 9 + x * 18, 18 + y * 18, true, true)
                    .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.CHARGER_OVERLAY));
            }
        }
        builder.bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 8, 18 + 18 * rowSize + 12);
        return builder.build(getHolder(), entityPlayer);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.universal.tooltip.item_storage_capacity", inventorySize));
        tooltip.add(I18n.format("gregtech.universal.tooltip.voltage_in", energyContainer.getInputVoltage(), GTValues.VN[getTier()]));
        tooltip.add(I18n.format("gregtech.universal.tooltip.energy_storage_capacity", energyContainer.getEnergyCapacity()));
    }
}
