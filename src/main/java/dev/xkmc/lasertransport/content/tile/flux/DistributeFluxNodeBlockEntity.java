package dev.xkmc.lasertransport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.configurables.ConfigConnectorType;
import dev.xkmc.lasertransport.content.configurables.FluxConfigurable;
import dev.xkmc.lasertransport.content.connector.DistributeConnector;
import dev.xkmc.lasertransport.content.connector.IConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class DistributeFluxNodeBlockEntity extends AbstractFluxNodeBlockEntity<DistributeFluxNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluxConfigurable config = new FluxConfigurable(ConfigConnectorType.DISTRIBUTE, this);

	@SerialClass.SerialField(toClient = true)
	private final DistributeConnector connector = new DistributeConnector(this::getMaxCoolDown);

	public DistributeFluxNodeBlockEntity(BlockEntityType<DistributeFluxNodeBlockEntity> type, BlockPos pos, BlockState state) {
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
}
