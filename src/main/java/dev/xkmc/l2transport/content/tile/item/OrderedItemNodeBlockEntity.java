package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.connector.Connector;
import dev.xkmc.l2transport.content.connector.OrderedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class OrderedItemNodeBlockEntity extends AbstractItemNodeBlockEntity<OrderedItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final OrderedConnector connector = new OrderedConnector(this, this::getMaxCoolDown);

	public OrderedItemNodeBlockEntity(BlockEntityType<OrderedItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Connector getConnector() {
		return connector;
	}
}
