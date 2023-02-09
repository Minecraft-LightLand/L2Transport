package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.generic.GenericHolder;
import dev.xkmc.l2transport.content.capability.generic.IGenericNodeBlockEntity;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class FluxConfigurable extends BaseConfigurable implements IConfigurableFilter {

	private final IGenericNodeBlockEntity generic;

	@SerialClass.SerialField(toClient = true)
	private final ResourceLocation filter = GenericHolder.EMPTY_ID;

	public FluxConfigurable(ConfigConnectorType type, IGenericNodeBlockEntity node) {
		super(type, node);
		this.generic = node;
	}

	@Override
	public MutableComponent getFilterDesc() {
		return generic.getCapType().getDesc(filter, getMaxTransfer());
	}

	@Override
	public boolean shouldDisplay() {
		return !filter.equals(GenericHolder.EMPTY_ID);
	}

	@Override
	protected int getTypeDefaultMax() {
		return generic.getCapType().getDefaultMax();
	}

	public boolean isContentValid(GenericHolder parse) {
		if (parse.type() != generic.getCapType()) return false;
		return !shouldDisplay() || parse.id().equals(filter);
	}

}
