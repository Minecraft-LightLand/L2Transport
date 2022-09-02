package dev.xkmc.l2transport.content.tile.fluid;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.connector.Connector;
import dev.xkmc.l2transport.content.connector.SimpleConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SimpleFluidNodeBlockEntity extends AbstractFluidNodeBlockEntity<SimpleFluidNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final SimpleConnector connector = new SimpleConnector(this::getMaxCoolDown, this::getLimit);

	public SimpleFluidNodeBlockEntity(BlockEntityType<SimpleFluidNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Connector getConnector() {
		return connector;
	}
}
