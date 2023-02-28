package dev.xkmc.lasertransport.compat.ars_nouveau;

import com.hollingsworth.arsnouveau.api.source.ISourceTile;
import dev.xkmc.lasertransport.content.capability.generic.GenericHolder;
import dev.xkmc.lasertransport.content.capability.generic.HandlerWrapper;
import dev.xkmc.lasertransport.content.flow.TransportHandler;

public record SourceWrapper(ISourceTile be) implements HandlerWrapper {

	@Override
	public long insert(GenericHolder token, boolean simulate) {
		if (be instanceof NodalSourceTile node) {
			long max = Math.min(token.getCount(), node.node().entity().getConfig().getMaxTransfer());
			return TransportHandler.insert(node.node(), SourceHolder.SOURCE.empty().getCopy(max), simulate);
		}
		if (!be.canAcceptSource()) return 0;
		int maxAccept = be.getMaxSource() - be.getSource();
		int accept = (int) Math.min(token.amount(), maxAccept);
		if (!simulate) be.addSource(accept);
		return accept;
	}

	@Override
	public GenericHolder extract(int slot, long max, boolean simulate) {
		int avail = be.getSource();
		int extract = (int) Math.min(avail, max);
		if (!simulate) be.removeSource(extract);
		return SourceHolder.SOURCE.empty().getCopy(extract);
	}

	@Override
	public int getSize() {
		return 1;
	}

}
