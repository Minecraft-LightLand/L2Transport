package dev.xkmc.lasertransport.content.craft.model;

import dev.xkmc.lasertransport.content.craft.block.Orientation;
import net.minecraft.core.Direction;

// 1: top, 2: right, 4: bottom, 8: left
public enum FaceType {
	FULL(0, 0, false),
	NO_TOP(1, 1, false),
	VERTICAL(2, 5, false),
	NO_TOP_RIGHT(3, 3, false),
	BOTTOM_ONLY(4, 11, false),
	EMPTY(5, 15, false),
	NO_RIGHT(1, 2, true),
	HORIZONTAL(2, 10, true),
	NO_BOTTOM_RIGHT(3, 6, true),
	LEFT_ONLY(4, 7, true);

	private static final Direction[] DEFAULT_FACE = {Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST};

	public final int id, select;
	public final boolean alt;

	FaceType(int id, int select, boolean alt) {
		this.id = id;
		this.select = select;
		this.alt = alt;
	}

	public Orientation toOrientation() {
		Orientation base = Orientation.of(Direction.NORTH);
		for (int i = 0; i < 4; i++) {
			if ((select & (1 << i)) == 0) continue;
			base = base.toggle(DEFAULT_FACE[i]);
		}
		return base;
	}

	public boolean open(Direction dire) {
		for (int i = 0; i < 4; i++) {
			if (DEFAULT_FACE[i] == dire) {
				return (select & (1 << i)) != 0;
			}
		}
		return false;
	}

}
