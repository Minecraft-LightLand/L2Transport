package dev.xkmc.lasertransport.compat.botania;

import dev.xkmc.lasertransport.content.capability.generic.GenericHolder;
import dev.xkmc.lasertransport.content.capability.generic.HandlerWrapper;
import dev.xkmc.lasertransport.content.flow.TransportHandler;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;

public record ManaWrapper(ManaReceiver be) implements HandlerWrapper {

	@Override
	public long insert(GenericHolder token, boolean simulate) {
		if (be instanceof NodalManaTile node) {
			long max = Math.min(token.getCount(), node.node().entity().getConfig().getMaxTransfer());
			return TransportHandler.insert(node.node(), ManaHolder.MANA.empty().getCopy(max), simulate);
		}
		if (be.isFull()) return 0;
		int maxAccept = getMaxMana() - be.getCurrentMana();
		int accept = (int) Math.min(token.amount(), maxAccept);
		if (!simulate) be.receiveMana(accept);
		return accept;
	}

	@Override
	public GenericHolder extract(int slot, long max, boolean simulate) {
		int avail = be.getCurrentMana();
		int extract = (int) Math.min(avail, max);
		if (!simulate) be.receiveMana(-extract);
		return ManaHolder.MANA.empty().getCopy(extract);
	}

	@Override
	public int getSize() {
		return 1;
	}

	public int getMaxMana() {
		if (be instanceof ManaPool pool) {
			return pool.getMaxMana();
		}
		if (be instanceof ManaCollector collector) {
			return collector.getMaxMana();
		}
		if (be.isFull()) return be.getCurrentMana();
		return be.getCurrentMana() + ManaHolder.MANA.getDefaultMax();
	}

}
