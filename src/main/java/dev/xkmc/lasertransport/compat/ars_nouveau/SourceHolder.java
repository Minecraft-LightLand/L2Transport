package dev.xkmc.lasertransport.compat.ars_nouveau;

import com.hollingsworth.arsnouveau.api.source.ISourceTile;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import dev.xkmc.lasertransport.content.capability.generic.*;
import dev.xkmc.lasertransport.content.capability.wrapper.FakeCapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.configurables.NumericConfigurator;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SourceHolder implements ICapabilityEntry<ISourceTile> {

	public static final SourceHolder SOURCE = new SourceHolder();

	public final ResourceLocation TYPE_ID = new ResourceLocation("ars_nouveau", "source");
	public final ResourceLocation UNIT = new ResourceLocation("ars_nouveau", "unit");

	@Override
	public ResourceLocation id() {
		return TYPE_ID;
	}

	@Override
	public ICapabilityHolder<ISourceTile> cap() {
		return new FakeCapabilityHolder<>(ISourceTile.class);
	}

	@Override
	public HandlerWrapper parse(ISourceTile iSourceTile) {
		return new SourceWrapper(iSourceTile);
	}

	@Override
	public MutableComponent getKindDesc(ResourceLocation id) {
		return LangData.FLUX_SOURCE.get();
	}

	@Override
	public MutableComponent getDesc(ResourceLocation id, long amount) {
		return Component.literal(amount + " ").append(LangData.FLUX_SOURCE.get());
	}

	@Override
	public int getDefaultMax() {
		return 1000;
	}

	@Override
	public GenericHolder empty() {
		return new GenericHolder(this, UNIT, 0);
	}

	@Override
	public ISourceTile parseHandler(NodalGenericHandler handler) {
		return new NodalSourceTile(handler);
	}

	@Override
	public MutableComponent getTypeDesc() {
		return LangData.FLUX_SOURCE.get();
	}

	@Override
	public ItemStack getIcon() {
		return ItemsRegistry.SOURCE_GEM.asItem().getDefaultInstance();
	}

	@Override
	public NumericConfigurator getScale() {
		return new NumericConfigurator("Source", 0, 10);//TODO name of source
	}
}
