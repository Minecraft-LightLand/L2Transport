package dev.xkmc.l2transport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.configurables.ConfigConnectorType;
import dev.xkmc.l2transport.content.configurables.FluidConfigurable;
import dev.xkmc.l2transport.content.configurables.FluxConfigurable;
import dev.xkmc.l2transport.content.connector.IConnector;
import dev.xkmc.l2transport.content.connector.SimpleConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SimpleFluxNodeBlockEntity extends AbstractFluxNodeBlockEntity<SimpleFluxNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluxConfigurable config = new FluxConfigurable(ConfigConnectorType.SIMPLE, this);

	@SerialClass.SerialField(toClient = true)
	private final SimpleConnector connector = new SimpleConnector(this::getMaxCoolDown, config);

	public SimpleFluxNodeBlockEntity(BlockEntityType<SimpleFluxNodeBlockEntity> type, BlockPos pos, BlockState state) {
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
