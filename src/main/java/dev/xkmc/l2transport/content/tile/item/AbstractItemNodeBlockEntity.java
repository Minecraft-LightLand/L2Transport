package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.item.IItemNodeBlockEntity;
import dev.xkmc.l2transport.content.capability.item.ItemHolder;
import dev.xkmc.l2transport.content.capability.item.NodalItemHandler;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableItemNode;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.upgrades.Upgrade;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;
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

	@SerialClass.SerialField(toClient = true)
	public ItemStack filter = ItemStack.EMPTY;

	@Override
	public boolean isTargetValid(BlockPos pos) {
		assert level != null;
		return super.isTargetValid(pos) || level.getBlockState(pos).getBlock() == Blocks.COMPOSTER;
	}

	@Override
	public Capability<?> getValidTarget() {
		return ForgeCapabilities.ITEM_HANDLER;
	}

	public ItemStack getItem() {
		return filter;
	}

	public final boolean isItemStackValid(ItemStack stack) {
		if (filter.isEmpty()) {
			return true;
		}
		return stack.getItem() == filter.getItem();
	}

	@Override
	public TooltipBuilder getTooltips() {
		var ans = super.getTooltips();
		getConnector().addTooltips(ans, new ItemHolder(getItem()));
		return ans;
	}

	@Override
	public int getMaxTransfer() {
		int cd = 64;
		for (Upgrade u : getUpgrades()) {
			cd = u.getMaxTransfer(cd);
		}
		return cd;
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

	protected int getLimit() {
		return getItem().isEmpty() ? getMaxTransfer() : getItem().getCount();
	}

}
