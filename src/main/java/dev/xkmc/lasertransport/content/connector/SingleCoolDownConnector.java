package dev.xkmc.lasertransport.content.connector;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.lasertransport.content.tile.base.CoolDownType;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.function.IntSupplier;

@SerialClass
public abstract class SingleCoolDownConnector implements IConnector {

	@SerialClass.SerialField(toClient = true)
	private int coolDown;

	@SerialClass.SerialField(toClient = true)
	protected final HashMap<BlockPos, CoolDownType> color = new HashMap<>();

	private int simulatedCoolDown;

	public IntSupplier maxCoolDown;

	@Override
	public List<BlockPos> getAvailableTarget() {
		return getVisibleConnection();
	}

	protected SingleCoolDownConnector(IntSupplier maxCoolDown) {
		this.maxCoolDown = maxCoolDown;
	}

	@Override
	public int getMaxCoolDown(BlockPos pos) {
		return maxCoolDown.getAsInt();
	}

	@Override
	public int getCoolDown(BlockPos pos) {
		return Math.min(getMaxCoolDown(pos), coolDown);
	}

	@Override
	public void perform() {
		if (coolDown < simulatedCoolDown) {
			coolDown = simulatedCoolDown;
			simulatedCoolDown = 0;
		}
	}

	@Override
	public void tick() {
		if (coolDown > 0) {
			coolDown--;
			if (coolDown == 0) {
				color.clear();
			}
		}
	}

	@Override
	public void refreshCoolDown(BlockPos target, boolean success, boolean simulate) {
		if (simulate) simulatedCoolDown = getMaxCoolDown(target);
		else coolDown = getMaxCoolDown(target);
		color.put(target, success ? CoolDownType.GREEN : CoolDownType.RED);
	}

	public CoolDownType getType(BlockPos pos) {
		return color.getOrDefault(pos, CoolDownType.GREY);
	}

	@Override
	public boolean isReady() {
		return coolDown == 0;
	}

}
