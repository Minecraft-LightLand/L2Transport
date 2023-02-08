package dev.xkmc.l2transport.content.tile.base;

import net.minecraft.util.Mth;

public enum CoolDownType {
	GREY(0, 0, 0.5f),
	GREEN(0.67f, 1, 1),
	RED(0, 1, 1),
	RETRIEVE(0.67f, 1, 1, true),
	INVALID(0, 1, 1, false),
	INVALID_RETRIEVE(0, 1, 1, true);

	private final float s0;
	private final float b0;
	private final float h1;
	private final float s1;
	private final float b1;
	private final boolean isReversed;

	CoolDownType(float h, float s, float b) {
		this(0, 0.5f, h, s, b, false);
	}

	CoolDownType(float h, float s, float b, boolean isReversed) {
		this(0, 0.5f, h, s, b, isReversed);
	}

	CoolDownType(float s0, float b0, float h1, float s1, float b1, boolean isReversed) {
		this.s0 = s0;
		this.b0 = b0;
		this.h1 = h1;
		this.s1 = s1;
		this.b1 = b1;
		this.isReversed = isReversed;
	}

	public void setColor(float percentage, HSBConsumer consumer) {
		if (this == INVALID) {
			percentage = 1;
		}
		float sat = Mth.lerp(percentage, s0, s1);
		float bright = Mth.lerp(percentage, b0, b1);
		consumer.setColor(h1, sat, bright);
	}

	public boolean isReversed() {
		return isReversed;
	}

	public CoolDownType invalidate() {
		return isReversed ? INVALID_RETRIEVE : INVALID;
	}

	@FunctionalInterface
	public interface HSBConsumer {

		void setColor(float hue, float sat, float bright);

	}

}
