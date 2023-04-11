package dev.xkmc.lasertransport.content.craft.tile;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.util.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class CraftSideBlockEntity extends ItemHolderBlockEntity {

	@SerialClass.SerialField(toClient = true)
	public Holder target = new Holder(null);

	public CraftSideBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void markDirty() {
		if (target.t() != null && level != null && !level.isClientSide()) {
			if (level.getBlockEntity(target.t()) instanceof CraftCoreBlockEntity be) {
				be.markDirty();
			}
		}
		super.markDirty();
	}

}
