package dev.xkmc.l2transport.content.tile.block;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2transport.content.tile.extend.ExtendedBlockEntity;
import dev.xkmc.l2transport.content.tile.extend.SidedBlockEntity;
import dev.xkmc.l2transport.init.registrate.LTBlocks;

public class ExtensionBlock {

	public static final BlockEntityBlockMethod<SidedBlockEntity> SIDED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_SIDED, SidedBlockEntity.class);
	public static final BlockEntityBlockMethod<ExtendedBlockEntity> EXTENDED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_EXTENDED, ExtendedBlockEntity.class);

}
