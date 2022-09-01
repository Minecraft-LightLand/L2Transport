package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.connector.Connector;
import dev.xkmc.l2transport.content.connector.DistributeConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class DistributeItemNodeBlockEntity extends AbstractItemNodeBlockEntity<DistributeItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final DistributeConnector connector = new DistributeConnector(80);

	public DistributeItemNodeBlockEntity(BlockEntityType<DistributeItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Connector getConnector() {
		return connector;
	}
}
