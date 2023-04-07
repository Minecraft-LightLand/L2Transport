package dev.xkmc.lasertransport.compat.botania;

import dev.xkmc.lasertransport.content.capability.generic.GenericHolder;
import dev.xkmc.lasertransport.content.capability.generic.HandlerWrapper;
import dev.xkmc.lasertransport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.capability.wrapper.FakeCapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.configurables.NumericConfigurator;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.BotaniaBlocks;

public class ManaHolder implements ICapabilityEntry<ManaReceiver> {

	public static final ManaHolder MANA = new ManaHolder();

	public final ResourceLocation TYPE_ID = new ResourceLocation("botania", "mana");
	public final ResourceLocation UNIT = new ResourceLocation("botania", "unit");

	@Override
	public ResourceLocation id() {
		return TYPE_ID;
	}

	@Override
	public ICapabilityHolder<ManaReceiver> cap() {
		return new FakeCapabilityHolder<>(ManaReceiver.class);
	}

	@Override
	public HandlerWrapper parse(ManaReceiver iSourceTile) {
		return new ManaWrapper(iSourceTile);
	}

	@Override
	public MutableComponent getKindDesc(ResourceLocation id) {
		return LangData.FLUX_MANA.get();
	}

	@Override
	public MutableComponent getDesc(ResourceLocation id, long amount) {
		return Component.literal(amount + " ").append(LangData.FLUX_MANA.get());
	}

	@Override
	public int getDefaultMax() {
		return 6400;
	}

	@Override
	public GenericHolder empty() {
		return new GenericHolder(this, UNIT, 0);
	}

	@Override
	public ManaReceiver parseHandler(NodalGenericHandler handler) {
		return new NodalManaTile(handler);
	}

	@Override
	public MutableComponent getTypeDesc() {
		return LangData.FLUX_MANA.get();
	}

	@Override
	public ItemStack getIcon() {
		return BotaniaBlocks.manaPool.asItem().getDefaultInstance().setHoverName(getTypeDesc());
	}

	@Override
	public NumericConfigurator getScale() {
		return new NumericConfigurator("Mana", 0, 10);//TODO name of source
	}
}
