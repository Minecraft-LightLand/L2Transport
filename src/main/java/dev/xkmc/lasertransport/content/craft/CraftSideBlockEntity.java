package dev.xkmc.lasertransport.content.craft;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.util.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class CraftSideBlockEntity extends ItemHolderBlockEntity {

	@SerialClass.SerialField(toClient = true)
	private Holder target;

	public CraftSideBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	

}
