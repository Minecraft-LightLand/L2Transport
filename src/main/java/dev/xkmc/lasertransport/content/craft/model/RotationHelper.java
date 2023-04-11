package dev.xkmc.lasertransport.content.craft.model;

import dev.xkmc.lasertransport.content.craft.block.Orientation;
import net.minecraft.core.Direction;

public record RotationHelper(int x, int y) implements Orientation.IRotate {

	private static Direction rotateClockwise(Direction dire, Direction normal) {
		return normal.getAxisDirection().getStep() > 0 ?
				dire.getClockWise(normal.getAxis()) :
				dire.getCounterClockWise(normal.getAxis());
	}

	private Direction rotateY(Direction dire) {
		for (int i = 0; i < y; i++) {
			dire = rotateClockwise(dire, Direction.UP);
		}
		return dire;
	}

	private Direction rotateX(Direction dire, Direction normal) {
		for (int i = 0; i < x; i++) {
			dire = rotateClockwise(dire, normal);
		}
		return dire;
	}

	@Override
	public Direction rotate(Direction dire) {
		Direction ans = rotateY(dire);
		Direction x_pos = rotateY(Direction.EAST);
		return rotateX(ans, x_pos);
	}

}
