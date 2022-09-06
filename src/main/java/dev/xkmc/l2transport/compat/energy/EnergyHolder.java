package dev.xkmc.l2transport.compat.energy;

import dev.xkmc.l2transport.content.capability.generic.GenericHolder;
import dev.xkmc.l2transport.content.capability.generic.HandlerWrapper;
import dev.xkmc.l2transport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.l2transport.content.capability.generic.NodalGenericHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
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
	public MutableComponent getDesc(ResourceLocation id, int amount) {
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
}
