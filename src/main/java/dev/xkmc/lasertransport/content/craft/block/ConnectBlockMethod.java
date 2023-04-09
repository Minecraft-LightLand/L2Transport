package dev.xkmc.lasertransport.content.craft.block;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.PlacementBlockMethod;
import dev.xkmc.l2library.block.mult.ShapeUpdateBlockMethod;
import dev.xkmc.l2library.block.one.MirrorRotateBlockMethod;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public record ConnectBlockMethod(IntegerProperty prop) implements
		CreateBlockStateBlockMethod, PlacementBlockMethod, MirrorRotateBlockMethod, ShapeUpdateBlockMethod {

	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(prop);
	}

	public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
		return prop == ItemHolderNodeBlock.ORIENTATION_SIDE ? def.setValue(prop, Orientation.VALUES.length - 1) :
				def.setValue(prop, Orientation.of(context.getClickedFace().getOpposite()));
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(prop, Orientation.rotate(state.getValue(prop), rot::rotate));
	}

	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(prop, Orientation.rotate(state.getValue(prop), mirror::mirror));
	}

	@Override
	public BlockState updateShape(Block block, BlockState current, BlockState self, Direction dire, BlockState other, LevelAccessor level, BlockPos selfPos, BlockPos otherPos) {
		Orientation orientation = Orientation.VALUES[current.getValue(prop)];
		if (!orientation.connected || dire.getAxis() == orientation.facing.getAxis())
			return current;
		if (orientation.isConnected(dire) == (self.is(LTBlocks.B_CRAFT_CORE.get()) || self.is(LTBlocks.B_CRAFT_SIDE.get())))
			return current;
		return current.setValue(prop, orientation.toggle(dire).ordinal);
	}

}
