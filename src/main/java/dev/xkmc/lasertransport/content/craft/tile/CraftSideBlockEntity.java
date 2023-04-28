package dev.xkmc.lasertransport.content.craft.tile;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.util.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class CraftSideBlockEntity extends ItemHolderBlockEntity {

	@SerialClass.SerialField(toClient = true)
	public Holder target = new Holder(null);

	public CraftSideBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Nullable
	public CraftCoreBlockEntity getParent() {
		if (target.t() != null && level != null) {
			if (level.getBlockEntity(target.t()) instanceof CraftCoreBlockEntity be) {
				return be;
			}
		}
		return null;
	}

	@Override
	public void markDirty() {
		if (level != null && !level.isClientSide()) {
			CraftCoreBlockEntity be = getParent();
			if (be != null) {
				be.markDirty();
			}
		}
		super.markDirty();
	}

	@Override
	public List<ItemStack> popContents() {
		CraftCoreBlockEntity be = getParent();
		if (be != null) {
			return be.popContents();
		}
		return super.popContents();
	}

	protected List<ItemStack> popContentsImpl() {
		return super.popContents();
	}

}
