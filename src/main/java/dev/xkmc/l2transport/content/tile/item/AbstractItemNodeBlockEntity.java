package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.item.IItemNodeBlockEntity;
import dev.xkmc.l2transport.content.capability.item.NodalItemHandler;
import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.items.upgrades.UpgradeFlag;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableItemNode;
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
	public Capability<?> getValidTarget() {
		return ForgeCapabilities.ITEM_HANDLER;
	}

	public ItemStack getItem() {
		return getConfig().getDisplayItem();
	}

	@Override
	public TooltipBuilder getTooltips() {
		var ans = super.getTooltips();
		getConnector().addTooltips(ans, getConfig());
		return ans;
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
