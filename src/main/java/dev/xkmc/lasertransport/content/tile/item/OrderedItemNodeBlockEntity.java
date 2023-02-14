package dev.xkmc.lasertransport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.configurables.ConfigConnectorType;
import dev.xkmc.lasertransport.content.configurables.ItemConfigurable;
import dev.xkmc.lasertransport.content.connector.IConnector;
import dev.xkmc.lasertransport.content.connector.OrderedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class OrderedItemNodeBlockEntity extends AbstractItemNodeBlockEntity<OrderedItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final ItemConfigurable config = new ItemConfigurable(ConfigConnectorType.ORDERED, this);

	@SerialClass.SerialField(toClient = true)
	private final OrderedConnector connector = new OrderedConnector(this, this::getMaxCoolDown);

	public OrderedItemNodeBlockEntity(BlockEntityType<OrderedItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
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

}
