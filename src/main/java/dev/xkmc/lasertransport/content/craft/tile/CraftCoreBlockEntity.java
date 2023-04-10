package dev.xkmc.lasertransport.content.craft.tile;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.craft.block.ConnectBlockMethod;
import dev.xkmc.lasertransport.content.craft.block.ItemHolderNodeBlock;
import dev.xkmc.lasertransport.content.craft.block.Orientation;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import dev.xkmc.lasertransport.util.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.TreeSet;

@SerialClass
public class CraftCoreBlockEntity extends ItemHolderBlockEntity {

	@SerialClass.SerialField(toClient = true)
	private final ArrayList<BlockPos> targets = new ArrayList<>();

	@SerialClass.SerialField
	private boolean shouldRevalidate = true;

	public CraftCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (shouldRevalidate) {
			shouldRevalidate = false;
			if (level != null && !level.isClientSide())
				revalidate();
		}
		super.tick();
	}

	@Override
	public boolean canInsert() {
		return false;
	}

	@Override
	public void setRemoved() {
		if (level != null) {
			for (BlockPos pos : targets) {
				level.destroyBlock(pos, true);
			}
			targets.clear();
		}
		super.setRemoved();
	}

	public void markRevalidate() {
		shouldRevalidate = true;
	}

	private void revalidate() {
		assert level != null;
		var scanned = scan();
		if (!scanned.getSecond().isEmpty()) {
			for (BlockPos pos : scanned.getSecond()) {
				level.destroyBlock(pos, true);
			}
			for (BlockPos pos : scanned.getFirst()) {
				level.destroyBlock(pos, true);
			}
			level.destroyBlock(getBlockPos(), true);
			return;
		}
		Orientation base_orient = Orientation.from(getBlockState().getValue(ItemHolderNodeBlock.ORIENTATION_CORE));
		for (BlockPos pos : targets) {
			if (!scanned.getFirst().contains(pos)) {
				level.destroyBlock(pos, true);
			}
		}
		for (BlockPos pos : scanned.getFirst()) {
			Orientation orientation = ConnectBlockMethod.connect(level, pos, base_orient.facing);
			BlockState state = LTBlocks.B_CRAFT_SIDE.getDefaultState()
					.setValue(ItemHolderNodeBlock.ORIENTATION_SIDE, orientation.ordinal);
			if (level.getBlockEntity(pos) instanceof CraftSideBlockEntity be) {
				be.target = new Holder(getBlockPos());
				be.markDirty();
			}
			level.setBlock(pos, state, 2);
		}
		targets.clear();
		targets.addAll(scanned.getFirst());
		level.setBlock(getBlockPos(), getBlockState().setValue(ItemHolderNodeBlock.ORIENTATION_CORE,
				ConnectBlockMethod.connect(level, getBlockPos(), base_orient.facing).ordinal), 2);
		markDirty();
	}

	private Pair<TreeSet<BlockPos>, TreeSet<BlockPos>> scan() {
		assert level != null;
		Orientation o = Orientation.from(getBlockState().getValue(ItemHolderNodeBlock.ORIENTATION_CORE));
		Queue<BlockPos> queue = new ArrayDeque<>();
		TreeSet<BlockPos> list = new TreeSet<>();
		TreeSet<BlockPos> error = new TreeSet<>();
		TreeSet<BlockPos> visited = new TreeSet<>();
		visited.add(getBlockPos());
		queue.add(getBlockPos());
		while (queue.size() > 0) {
			BlockPos current = queue.poll();
			for (Direction dire : o.sides) {
				BlockPos next = current.relative(dire);
				if (visited.contains(next)) continue;
				visited.add(next);
				BlockState state = level.getBlockState(next);
				if (state.getBlock() == LTBlocks.B_CRAFT_CORE.get()) {
					queue.add(next);
					error.add(next);
				}
				if (state.getBlock() == LTBlocks.B_CRAFT_SIDE.get()) {
					queue.add(next);
					list.add(next);
				}
			}
		}
		return Pair.of(list, error);
	}

}
