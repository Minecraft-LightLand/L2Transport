package dev.xkmc.lasertransport.content.tile.flux;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.lasertransport.content.configurables.ConfigConnectorType;
import dev.xkmc.lasertransport.content.configurables.FluxConfigurable;
import dev.xkmc.lasertransport.content.connector.IConnector;
import dev.xkmc.lasertransport.content.connector.OrderedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class OrderedFluxNodeBlockEntity extends AbstractFluxNodeBlockEntity<OrderedFluxNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluxConfigurable config = new FluxConfigurable(ConfigConnectorType.ORDERED, this);

	@SerialClass.SerialField(toClient = true)
	private final OrderedConnector connector = new OrderedConnector(this, this::getMaxCoolDown);

	public OrderedFluxNodeBlockEntity(BlockEntityType<OrderedFluxNodeBlockEntity> type, BlockPos pos, BlockState state) {
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
