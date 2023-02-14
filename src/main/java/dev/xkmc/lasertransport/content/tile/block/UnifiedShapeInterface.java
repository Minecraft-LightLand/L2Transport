package dev.xkmc.lasertransport.content.tile.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface UnifiedShapeInterface {

	@Nullable
	VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx);

}
