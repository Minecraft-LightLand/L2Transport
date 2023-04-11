package dev.xkmc.lasertransport.content.craft.logic;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.lasertransport.content.craft.block.Orientation;
import dev.xkmc.lasertransport.content.craft.tile.CraftSideBlockEntity;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public record ScanInfo(int count, Direction facing, Direction y_neg, int dx, int dy, int w, int h) {

	public static ScanInfo from(Direction facing, Direction y_neg, BlockPos self, List<BlockPos> targets) {
		Direction x_neg = ScanInfo.getXAxis(facing, y_neg);
		int x0 = 0, x1 = 0, y0 = 0, y1 = 0;
		for (BlockPos pos : targets) {
			BlockPos diff = pos.subtract(self);
			int x = -along(diff, x_neg);
			int y = -along(diff, y_neg);
			x0 = Math.min(x0, x);
			x1 = Math.max(x1, x);
			y0 = Math.min(y0, y);
			y1 = Math.max(y1, y);
		}
		return new ScanInfo(targets.size(), facing, y_neg, x0, y0, x1 - x0 + 1, y1 - y0 + 1);
	}

	private static int along(BlockPos pos, Direction dire) {
		return switch (dire.getAxis()) {
			case X -> pos.getX();
			case Y -> pos.getY();
			case Z -> pos.getZ();
		} * dire.getAxisDirection().getStep();
	}

	public static Direction getXAxis(Direction facing, Direction y_neg) {
		return facing.getAxisDirection().getStep() > 0 ? y_neg.getCounterClockWise(facing.getAxis()) : y_neg.getClockWise(facing.getAxis());
	}

	public static ScanInfo dummy(Direction facing) {
		return new ScanInfo(0, facing, facing.getAxis() == Direction.Axis.Y ? Direction.NORTH : Direction.UP, 0, 0, 1, 1);
	}

	public static Pair<TreeSet<BlockPos>, TreeSet<BlockPos>> scan(Level level, BlockPos self, Orientation o) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		TreeSet<BlockPos> list = new TreeSet<>();
		TreeSet<BlockPos> error = new TreeSet<>();
		TreeSet<BlockPos> visited = new TreeSet<>();
		visited.add(self);
		queue.add(self);
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

	public CraftGrid compost(Level level, BlockPos self, ArrayList<BlockPos> targets) {
		Direction x_neg = ScanInfo.getXAxis(facing, y_neg);
		CraftItemSlot[][] tooltip = new CraftItemSlot[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				tooltip[i][j] = new CraftItemSlot(CraftSlotType.EMPTY, ItemStack.EMPTY);
			}
		}
		tooltip[-dy][-dx] = new CraftItemSlot(CraftSlotType.RESULT, ItemStack.EMPTY);
		for (BlockPos pos : targets) {
			BlockPos diff = pos.subtract(self);
			int x = -along(diff, x_neg);
			int y = -along(diff, y_neg);
			ItemStack stack = ItemStack.EMPTY;
			if (level.getBlockEntity(pos) instanceof CraftSideBlockEntity be) {
				stack = be.getHolder().getStackInSlot(0);
			}
			tooltip[y - dy][x - dx] = new CraftItemSlot(CraftSlotType.ITEM, stack);
		}
		return new CraftGrid(h, w, tooltip);
	}

}
