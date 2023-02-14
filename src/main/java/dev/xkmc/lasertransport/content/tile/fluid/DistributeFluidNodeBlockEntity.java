package dev.xkmc.lasertransport.content.tile.fluid;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.configurables.ConfigConnectorType;
import dev.xkmc.lasertransport.content.configurables.FluidConfigurable;
import dev.xkmc.lasertransport.content.connector.DistributeConnector;
import dev.xkmc.lasertransport.content.connector.IConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class DistributeFluidNodeBlockEntity extends AbstractFluidNodeBlockEntity<DistributeFluidNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluidConfigurable config = new FluidConfigurable(ConfigConnectorType.DISTRIBUTE, this);

	@SerialClass.SerialField(toClient = true)
	private final DistributeConnector connector = new DistributeConnector(this::getMaxCoolDown);

	public DistributeFluidNodeBlockEntity(BlockEntityType<DistributeFluidNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}

	@Override
	public FluidConfigurable getConfig() {
		return config;
	}

}
