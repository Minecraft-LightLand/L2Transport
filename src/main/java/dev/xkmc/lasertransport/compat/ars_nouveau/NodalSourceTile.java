package dev.xkmc.lasertransport.compat.ars_nouveau;

import com.hollingsworth.arsnouveau.api.source.ISourceTile;
import dev.xkmc.lasertransport.content.capability.generic.INodeHandlerWrapper;
import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.flow.TransportHandler;

public record NodalSourceTile(NodalGenericHandler node) implements INodeHandlerWrapper, ISourceTile {

	@Override
	public int getTransferRate() {
		return SourceHolder.SOURCE.getDefaultMax();
	}

	@Override
	public boolean canAcceptSource() {
		return false;
	}

	@Override
	public int getSource() {
		return 0;
	}

	@Override
	public int getMaxSource() {
		return 0;
	}

	@Override
	public void setMaxSource(int i) {

	}

	@Override
	public int setSource(int i) {
		return 0;
	}

	@Override
	public int addSource(int i) {
		TransportHandler.insert(node, SourceHolder.SOURCE.empty().getCopy(i), false);
		return 0;
	}

	@Override
	public int removeSource(int i) {
		return 0;
	}

}
