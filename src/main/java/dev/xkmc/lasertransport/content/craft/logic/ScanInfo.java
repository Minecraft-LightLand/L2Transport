package dev.xkmc.lasertransport.content.craft.logic;

import dev.xkmc.lasertransport.content.client.tooltip.CraftTooltip;
import dev.xkmc.lasertransport.content.craft.tile.CraftSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

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

	public CraftTooltip compost(Level level, BlockPos self, ArrayList<BlockPos> targets) {
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
		return new CraftTooltip(h, w, tooltip);
	}
}
