package dev.xkmc.lasertransport.content.tile.block;

import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.one.BlockEntityBlockMethod;
import dev.xkmc.lasertransport.content.tile.item.*;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;

public class ItemTransferBlock {

	public static final BlockEntityBlockMethod<SimpleItemNodeBlockEntity> SIMPLE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_SIMPLE, SimpleItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<OrderedItemNodeBlockEntity> ORDERED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_ORDERED, OrderedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<SyncedItemNodeBlockEntity> SYNCED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_SYNCED, SyncedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<DistributeItemNodeBlockEntity> DISTRIBUTE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_DISTRIBUTE, DistributeItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<RetrieverItemNodeBlockEntity> RETRIEVE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_RETRIEVE, RetrieverItemNodeBlockEntity.class);

}
