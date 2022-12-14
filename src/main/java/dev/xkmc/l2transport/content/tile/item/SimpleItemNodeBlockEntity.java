package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.connector.IConnector;
import dev.xkmc.l2transport.content.connector.SimpleConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SimpleItemNodeBlockEntity extends AbstractItemNodeBlockEntity<SimpleItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final SimpleConnector connector = new SimpleConnector(this::getMaxCoolDown, this::getLimit);

	public SimpleItemNodeBlockEntity(BlockEntityType<SimpleItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}
}
