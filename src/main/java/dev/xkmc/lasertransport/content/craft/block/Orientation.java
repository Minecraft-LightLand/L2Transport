package dev.xkmc.lasertransport.content.craft.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

import java.util.ArrayList;
import java.util.List;

public class Orientation {

	public interface IRotate {

		Direction rotate(Direction dire);

	}

	public static final Orientation[] VALUES;

	static {
		VALUES = new Orientation[6 * 16 + 1];
		int ind = 0;
		for (Direction front : Direction.values()) {
			List<Direction> list = new ArrayList<>();
			for (Direction side : Direction.values()) {
				if (side.getAxis() == front.getAxis()) continue;
				list.add(side);
			}
			Direction[] sides = list.toArray(new Direction[4]);
			for (int i = 0; i < 16; i++) {
				VALUES[ind] = new Orientation(ind, front, sides, i);
				ind++;
			}
		}
		VALUES[ind] = new Orientation(ind);
	}

	public static int of(Direction front) {
		return front.ordinal() * 16;
	}

	public static int rotate(int value, IRotate rot) {
		Orientation o = VALUES[value];
		if (!o.connected) return o.ordinal;
		Direction newFront = rot.rotate(o.facing);
		Orientation base = VALUES[newFront.ordinal() * 16];
		for (int i = 0; i < 4; i++) {
			if ((o.selected & (1 << i)) != 0) {
				base = base.toggle(rot.rotate(o.sides[i]));
			}
		}
		return base.ordinal;
	}

	public final int ordinal;
	public final boolean connected;
	public final Direction facing;
	public final Direction[] sides;
	public final int connection, selected;

	Orientation(int ordinal) {
		this.connected = false;
		this.facing = Direction.UP;
		this.sides = new Direction[0];
		this.connection = 0;
		this.selected = 0;
		this.ordinal = ordinal;
	}

	Orientation(int ordinal, Direction front, Direction[] side, int select) {
		this.connected = true;
		this.facing = front;
		this.sides = side;
		int ans = 0;
		for (int i = 0; i < 4; i++) {
			if ((select & (1 << i)) == 0) continue;
			ans |= 1 << sides[i].get3DDataValue();
		}
		this.connection = ans;
		this.selected = select;
		this.ordinal = ordinal;
	}

	public Orientation toggle(Direction side) {
		for (int i = 0; i < 4; i++) {
			if (sides[i] == side) {
				return VALUES[facing.ordinal() * 16 + (selected ^ (1 << i))];
			}
		}
		throw new IllegalStateException("this should not happen");
	}

	public boolean isConnected(Direction dire) {
		if (dire.getAxis() == facing.getAxis()) {
			return false;
		}
		for (int i = 0; i < 4; i++) {
			if (sides[i] == dire) {
				return (selected & (1 << i)) != 0;
			}
		}
		return false;
	}

}
