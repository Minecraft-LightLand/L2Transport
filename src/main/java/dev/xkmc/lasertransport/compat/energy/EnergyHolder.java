package dev.xkmc.lasertransport.compat.energy;

import dev.xkmc.lasertransport.content.capability.generic.GenericHolder;
import dev.xkmc.lasertransport.content.capability.generic.HandlerWrapper;
import dev.xkmc.lasertransport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.capability.wrapper.ForgeCapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.configurables.NumericConfigurator;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHolder implements ICapabilityEntry<IEnergyStorage> {

	public static final EnergyHolder ENERGY = new EnergyHolder();

	public final ResourceLocation TYPE_ID = new ResourceLocation("forge", "energy");
	public final ResourceLocation UNIT = new ResourceLocation("forge", "unit");

	@Override
	public ResourceLocation id() {
		return TYPE_ID;
	}

	@Override
	public ICapabilityHolder<IEnergyStorage> cap() {
		return new ForgeCapabilityHolder<>(ForgeCapabilities.ENERGY);
	}

	@Override
	public HandlerWrapper parse(IEnergyStorage cap) {
		return new EnergyWrapper(cap);
	}

	@Override
	public MutableComponent getKindDesc(ResourceLocation id) {
		return LangData.FLUX_ENERGY.get();
	}

	@Override
	public MutableComponent getDesc(ResourceLocation id, long amount) {
		return Component.literal(amount + " ").append(LangData.FLUX_ENERGY.get());
	}

	@Override
	public int getDefaultMax() {
		return 80000;
	}

	@Override
	public GenericHolder empty() {
		return new GenericHolder(this, UNIT, 0);
	}

	@Override
	public IEnergyStorage parseHandler(NodalGenericHandler handler) {
		return new NodalEnergyHandler(handler);
	}

	@Override
	public MutableComponent getTypeDesc() {
		return LangData.FLUX_ENERGY.get();
	}

	@Override
	public ItemStack getIcon() {
		return Items.FURNACE.getDefaultInstance().setHoverName(getTypeDesc());
	}

	@Override
	public NumericConfigurator getScale() {
		return new NumericConfigurator("FE", 0, 10);
	}

}
