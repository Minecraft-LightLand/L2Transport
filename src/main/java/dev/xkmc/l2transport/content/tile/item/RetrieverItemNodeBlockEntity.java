package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.configurables.ConfigConnectorType;
import dev.xkmc.l2transport.content.configurables.ItemConfigurable;
import dev.xkmc.l2transport.content.connector.ExtractConnector;
import dev.xkmc.l2transport.content.connector.IConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

@SerialClass
public class RetrieverItemNodeBlockEntity extends AbstractItemNodeBlockEntity<RetrieverItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final ItemConfigurable config = new ItemConfigurable(ConfigConnectorType.EXTRACT, this);

	@SerialClass.SerialField(toClient = true)
	private final ExtractConnector connector = new ExtractConnector(this::getMaxCoolDown, config,
			() -> getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING)));

	public RetrieverItemNodeBlockEntity(BlockEntityType<RetrieverItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}

	@Override
	public ItemConfigurable getConfig() {
		return config;
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide() && isReady() && connector.mayExtract()) {
			Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos next = getBlockPos().relative(facing);
			BlockEntity target = level.getBlockEntity(next);
			if (target != null) {
				var lazyCap = target.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite());
				if (lazyCap.resolve().isPresent()) {
					var cap = lazyCap.resolve().get();
					connector.performExtract(tryRetrieve(cap));
					markDirty();
				}
			} else {
				BlockState state = level.getBlockState(next);
				if (state.getBlock() instanceof WorldlyContainerHolder holder) {
					var c = holder.getContainer(state, level, next);
					connector.performExtract(tryRetrieve(new InvWrapper(c)));
					markDirty();
				}
			}

		}
		super.tick();
	}

	protected boolean tryRetrieve(IItemHandler target) {
		for (int i = 0; i < target.getSlots(); i++) {
			ItemStack stack = target.extractItem(i, (int) getConfig().getMaxTransfer(), true);
			if (stack.isEmpty()) continue;
			if (!getConfig().isStackValid(stack)) continue;
			if (!getConfig().allowExtract(stack.getCount())) continue;
			ItemStack attempt = getHandler().insertItem(0, stack, true);
			if (attempt.getCount() == stack.getCount()) continue;
			stack = target.extractItem(i, stack.getCount() - attempt.getCount(), false);
			ItemStack leftover = getHandler().insertItem(0, stack, false);
			leftover = ItemHandlerHelper.insertItemStacked(target, leftover, false);
			drop(leftover);
			return true;
		}
		return false;
	}

	private void drop(ItemStack stack) {
		if (stack.isEmpty()) return;
		if (level == null || level.isClientSide()) return;
		BlockPos pos = getBlockPos();
		level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack));
	}

}
