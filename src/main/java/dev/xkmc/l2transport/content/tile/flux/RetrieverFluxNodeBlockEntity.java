package dev.xkmc.l2transport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.generic.GenericHolder;
import dev.xkmc.l2transport.content.capability.generic.HandlerWrapper;
import dev.xkmc.l2transport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.l2transport.content.configurables.ConfigConnectorType;
import dev.xkmc.l2transport.content.configurables.FluxConfigurable;
import dev.xkmc.l2transport.content.connector.ExtractConnector;
import dev.xkmc.l2transport.content.connector.IConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@SerialClass
public class RetrieverFluxNodeBlockEntity extends AbstractFluxNodeBlockEntity<RetrieverFluxNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluxConfigurable config = new FluxConfigurable(ConfigConnectorType.EXTRACT, this);

	@SerialClass.SerialField(toClient = true)
	private final ExtractConnector connector = new ExtractConnector(this::getMaxCoolDown, config,
			() -> getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING)));

	public RetrieverFluxNodeBlockEntity(BlockEntityType<RetrieverFluxNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}

	@Override
	public FluxConfigurable getConfig() {
		return config;
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide() && isReady() && connector.mayExtract()) {
			Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos next = getBlockPos().relative(facing);
			BlockEntity target = level.getBlockEntity(next);
			if (target != null) {
				connector.performExtract(tryRetrieveTyped(getCapType(), target, facing));
				markDirty();
			}

		}
		super.tick();
	}

	private <T> boolean tryRetrieveTyped(ICapabilityEntry<T> type, BlockEntity target, Direction facing) {
		var lazyCap = target.getCapability(type.cap(), facing.getOpposite());
		if (lazyCap.resolve().isPresent()) {
			var cap = lazyCap.resolve().get();
			return tryRetrieve(type, cap);
		}
		return false;
	}

	protected <T> boolean tryRetrieve(ICapabilityEntry<T> type, T target) {
		HandlerWrapper wrapper = type.parse(target);
		HandlerWrapper self = type.parse(type.parseHandler(genericHandler));
		for (int i = 0; i < wrapper.getSize(); i++) {
			GenericHolder toExtract = wrapper.extract(i, (int) getConfig().getMaxTransfer(), true);
			if (toExtract.amount() == 0) continue;
			if (!getConfig().isContentValid(toExtract)) continue;
			if (!getConfig().allowExtract(toExtract.getCount())) continue;
			int toInsert = self.insert(toExtract, true);
			if (toInsert == 0) continue;
			while (toInsert != toExtract.amount()) {
				toExtract = wrapper.extract(i, toInsert, true);
				toInsert = self.insert(toExtract, true);
			}
			if (toInsert == 0) continue;
			toExtract = wrapper.extract(i, toInsert, false);
			self.insert(toExtract, false);
			return true;
		}
		return false;
	}

}
