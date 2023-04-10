package dev.xkmc.lasertransport.content.craft.model;

import dev.xkmc.lasertransport.content.craft.block.Orientation;
import net.minecraft.core.Direction;

// 1: top, 2: right, 4: bottom, 8: left
public enum FaceType {
	FULL(0),
	NO_TOP(1), VERTICAL(5), NO_TOP_RIGHT(3), BOTTOM_ONLY(11),
	EMPTY(15),
	NO_RIGHT(2), HORIZONTAL(10), NO_BOTTOM_RIGHT(6), LEFT_ONLY(7);

	private static final Direction[] DEFAULT_FACE = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

	private final int select;

	FaceType(int select) {
		this.select = select;
	}

	public Orientation toOrientation() {
		Orientation base = Orientation.of(Direction.UP);
		for (int i = 0; i < 4; i++) {
			if ((select & (1 << i)) == 0) continue;
			base = base.toggle(DEFAULT_FACE[i]);
		}
		return base;
	}
}
