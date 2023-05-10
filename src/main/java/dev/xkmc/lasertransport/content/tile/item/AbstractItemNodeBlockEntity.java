package dev.xkmc.lasertransport.content.tile.item;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.lasertransport.content.capability.item.IItemNodeBlockEntity;
import dev.xkmc.lasertransport.content.capability.item.NodalItemHandler;
import dev.xkmc.lasertransport.content.capability.wrapper.ForgeCapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.items.upgrades.UpgradeFlag;
import dev.xkmc.lasertransport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.lasertransport.content.tile.base.IRenderableItemNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class AbstractItemNodeBlockEntity<BE extends AbstractItemNodeBlockEntity<BE>> extends AbstractNodeBlockEntity<BE>
		implements IItemNodeBlockEntity, IRenderableItemNode {

	protected final LazyOptional<NodalItemHandler> itemHandler = LazyOptional.of(() -> new NodalItemHandler(this));

	public AbstractItemNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		flags.add(UpgradeFlag.THROUGH_PUT);
		flags.add(UpgradeFlag.LEVEL);
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		assert level != null;
		return super.isTargetValid(pos) || level.getBlockState(pos).getBlock() == Blocks.COMPOSTER;
	}

	@Override
	public ICapabilityHolder<?> getValidTarget() {
		return new ForgeCapabilityHolder<>(ForgeCapabilities.ITEM_HANDLER);
	}

	public ItemStack getItem() {
		return getConfig().getDisplayItem();
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	protected NodalItemHandler getHandler() {
		return itemHandler.resolve().get();
	}

}
