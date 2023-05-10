package dev.xkmc.lasertransport.content.tile.fluid;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.lasertransport.content.configurables.ConfigConnectorType;
import dev.xkmc.lasertransport.content.configurables.FluidConfigurable;
import dev.xkmc.lasertransport.content.connector.IConnector;
import dev.xkmc.lasertransport.content.connector.SimpleConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SimpleFluidNodeBlockEntity extends AbstractFluidNodeBlockEntity<SimpleFluidNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluidConfigurable config = new FluidConfigurable(ConfigConnectorType.SIMPLE, this);

	@SerialClass.SerialField(toClient = true)
	private final SimpleConnector connector = new SimpleConnector(this::getMaxCoolDown, config);

	public SimpleFluidNodeBlockEntity(BlockEntityType<SimpleFluidNodeBlockEntity> type, BlockPos pos, BlockState state) {
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
