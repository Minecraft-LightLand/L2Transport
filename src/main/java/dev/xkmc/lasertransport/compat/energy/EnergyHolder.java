package dev.xkmc.lasertransport.compat.energy;

import dev.xkmc.lasertransport.content.capability.generic.GenericHolder;
import dev.xkmc.lasertransport.content.capability.generic.HandlerWrapper;
import dev.xkmc.lasertransport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.configurables.NumericConfigurator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.Capability;
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
	public Capability<IEnergyStorage> cap() {
		return ForgeCapabilities.ENERGY;
	}

	@Override
	public HandlerWrapper parse(IEnergyStorage cap) {
		return new EnergyWrapper(cap);
	}

	@Override
	public MutableComponent getKindDesc(ResourceLocation id) {
		return Component.translatable("flux.forge.energy");
	}

	@Override
	public MutableComponent getDesc(ResourceLocation id, long amount) {
		return Component.literal(amount + " ").append(Component.translatable("flux.forge.energy"));
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
		return Component.translatable("flux.forge.energy");
	}

	@Override
	public ItemStack getIcon() {
		return Items.COAL.getDefaultInstance();
	}

	@Override
	public NumericConfigurator getScale() {
		return new NumericConfigurator("FE", 0, 10);
	}

}
