package dev.xkmc.lasertransport.content.craft;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.block.BlockContainer;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class ItemHolderBlockEntity extends BaseBlockEntity implements
		IItemHolderNode, DelaySyncBlockEntity, TickableBlockEntity, BlockContainer {

	@SerialClass.SerialField(toClient = true)
	public final ItemInventory items = new ItemInventory(this);

	@SerialClass.SerialField
	private boolean needSync = false;

	public ItemHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return LazyOptional.of(() -> items).cast();
		}
		return super.getCapability(cap, side);
	}

	public void markDirty() {
		needSync = true;
	}

	@Override
	public void tick() {
		if (needSync) {
			needSync = false;
			sync();
		}
	}

	@Override
	public ItemInventory getHolder() {
		return items;
	}

	@Override
	public List<Container> getContainers() {
		return List.of(new SimpleContainer(items.getStackInSlot(0)));
	}

	@Override
	public boolean canInsert() {
		return true;
	}

}
