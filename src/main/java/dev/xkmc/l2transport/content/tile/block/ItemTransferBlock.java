package dev.xkmc.l2transport.content.tile.block;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2transport.content.tile.base.SidedBlockEntity;
import dev.xkmc.l2transport.content.tile.item.*;
import dev.xkmc.l2transport.init.registrate.LTBlocks;

public class ItemTransferBlock {

	public static final BlockEntityBlockMethod<SimpleItemNodeBlockEntity> SIMPLE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_SIMPLE, SimpleItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<OrderedItemNodeBlockEntity> ORDERED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_ORDERED, OrderedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<SyncedItemNodeBlockEntity> SYNCED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_SYNCED, SyncedItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<DistributeItemNodeBlockEntity> DISTRIBUTE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_DISTRIBUTE, DistributeItemNodeBlockEntity.class);
	public static final BlockEntityBlockMethod<RetrieverItemNodeBlockEntity> RETRIEVE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_RETRIEVE, RetrieverItemNodeBlockEntity.class);

	public static final BlockEntityBlockMethod<SidedBlockEntity> SIDED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_SIDED, SidedBlockEntity.class);

}
