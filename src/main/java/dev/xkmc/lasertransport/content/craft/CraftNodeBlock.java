package dev.xkmc.lasertransport.content.craft;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;

public class CraftNodeBlock {

	public static final BlockEntityBlockMethod<ItemHolderBlockEntity> SIDE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_CRAFT_SIDE, ItemHolderBlockEntity.class);

	public static final CraftNodeSetItem CLICK = new CraftNodeSetItem();

}
