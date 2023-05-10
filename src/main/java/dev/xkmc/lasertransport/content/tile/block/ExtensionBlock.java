package dev.xkmc.lasertransport.content.tile.block;

import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.one.BlockEntityBlockMethod;
import dev.xkmc.lasertransport.content.tile.extend.EnderExtendedBlockEntity;
import dev.xkmc.lasertransport.content.tile.extend.ExtendedBlockEntity;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExtensionBlock {

	public static final BlockEntityBlockMethod<ExtendedBlockEntity> EXTENDED = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_EXTENDED, ExtendedBlockEntity.class);
	public static final BlockEntityBlockMethod<EnderExtendedBlockEntity> ENDER = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ENDER, EnderExtendedBlockEntity.class);

	private static final VoxelShape SHAPE_NODE = Shapes.box(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875);
	private static final VoxelShape SHAPE_FULL = Shapes.block();

	public static final UnifiedShapeBlockMethod SIMPLE = new UnifiedShapeBlockMethod((state, level, pos, ctx) -> SHAPE_NODE);
	public static final UnifiedShapeBlockMethod FILTER = new UnifiedShapeBlockMethod((state, level, pos, ctx) -> state.getValue(BlockStateProperties.LIT) ? SHAPE_FULL : SHAPE_NODE);

}
