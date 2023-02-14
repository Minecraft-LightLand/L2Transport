package dev.xkmc.lasertransport.content.tile.block;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.lasertransport.content.tile.fluid.*;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;

public class FluidTransferBlock {

	public static final BlockEntityBlockMethod<SimpleFluidNodeBlockEntity> SIMPLE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUID_SIMPLE, SimpleFluidNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<OrderedFluidNodeBlockEntity> ORDERED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUID_ORDERED, OrderedFluidNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<SyncedFluidNodeBlockEntity> SYNCED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUID_SYNCED, SyncedFluidNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<DistributeFluidNodeBlockEntity> DISTRIBUTE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUID_DISTRIBUTE, DistributeFluidNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<RetrieverFluidNodeBlockEntity> RETRIEVE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_FLUID_RETRIEVE, RetrieverFluidNodeBlockEntity.class);

}
