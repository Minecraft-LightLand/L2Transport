package dev.xkmc.lasertransport.compat.mekanism;

import dev.xkmc.lasertransport.content.capability.generic.*;
import dev.xkmc.lasertransport.content.configurables.NumericConfigurator;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GasHolder implements ICapabilityEntry<IGasHandler> {

	public static final ResourceLocation TYPE_ID = new ResourceLocation("mekanism", "gas");

	public static final GasHolder GAS = new GasHolder();

	public static GasStack parse(GenericHolder holder) {
		if (holder.type() != GAS) return GasStack.EMPTY;
		var gas = MekanismAPI.gasRegistry().getValue(holder.id());
		if (gas == null) return GasStack.EMPTY;
		return new GasStack(gas, holder.getCount());
	}

	public static GenericHolder parse(GasStack stack) {
		return new GenericHolder(GAS, stack.getTypeRegistryName(), stack.getAmount());
	}

	@Override
	public ResourceLocation id() {
		return TYPE_ID;
	}

	@Override
	public ICapabilityHolder<IGasHandler> cap() {
		return new ForgeCapabilityHolder<>(Capabilities.GAS_HANDLER);
	}

	@Override
	public HandlerWrapper parse(IGasHandler handler) {
		return new GasWrapper(handler);
	}

	@Override
	public MutableComponent getKindDesc(ResourceLocation id) {
		var gas = MekanismAPI.gasRegistry().getValue(id);
		return gas == null ? Component.empty() : gas.getTextComponent().copy();
	}

	@Override
	public MutableComponent getDesc(ResourceLocation id, long amount) {
		var gas = MekanismAPI.gasRegistry().getValue(id);
		return (gas == null ? GasStack.EMPTY : new GasStack(gas, amount)).getTextComponent().copy();
	}

	@Override
	public int getDefaultMax() {
		return 1000;
	}

	@Override
	public GenericHolder empty() {
		return new GenericHolder(this, MekanismAPI.EMPTY_GAS.getRegistryName(), 0);
	}

	@Override
	public IGasHandler parseHandler(NodalGenericHandler handler) {
		return new NodalGasHandler(handler);
	}

	@Override
	public MutableComponent getTypeDesc() {
		return Component.translatable("flux.mekanism.gas");
	}

	@Override
	public ItemStack getIcon() {
		return MekanismBlocks.BASIC_CHEMICAL_TANK.getItemStack();
	}

	@Override
	public NumericConfigurator getScale() {
		return new NumericConfigurator("B", -1, 10);
	}

}
