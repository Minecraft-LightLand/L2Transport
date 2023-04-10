package dev.xkmc.lasertransport.content.craft.tile;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

@SerialClass
public class CraftCoreBlockEntity extends ItemHolderBlockEntity {

	@SerialClass.SerialField(toClient = true)
	private final ArrayList<BlockPos> targets = new ArrayList<>();

	@SerialClass.SerialField
	private boolean shouldRevalidate = false;

	public CraftCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (shouldRevalidate) {
			shouldRevalidate = false;
			revalidate();
		}
		super.tick();
	}

	@Override
	public boolean canInsert() {
		return false;
	}

	public void markRevalidate() {
		shouldRevalidate = true;
	}

	private void revalidate() {
		// TODO
	}

}
