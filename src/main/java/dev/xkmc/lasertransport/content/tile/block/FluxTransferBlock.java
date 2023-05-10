package dev.xkmc.lasertransport.content.tile.block;

import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.one.BlockEntityBlockMethod;
import dev.xkmc.lasertransport.content.tile.flux.*;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;

public class FluxTransferBlock {

	public static final BlockEntityBlockMethod<SimpleFluxNodeBlockEntity> SIMPLE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUX_SIMPLE, SimpleFluxNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<OrderedFluxNodeBlockEntity> ORDERED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUX_ORDERED, OrderedFluxNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<DistributeFluxNodeBlockEntity> DISTRIBUTE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUX_DISTRIBUTE, DistributeFluxNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<SyncedFluxNodeBlockEntity> SYNCED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUX_SYNCED, SyncedFluxNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<RetrieverFluxNodeBlockEntity> RETRIEVE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUX_RETRIEVE, RetrieverFluxNodeBlockEntity.class);

}
