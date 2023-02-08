package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.tile.client.RenderManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class ConnectionRenderBlockEntity extends BaseBlockEntity implements IRenderableNode {

	public ConnectionRenderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Nullable
	private AABB compiledBox;

	@Override
	public AABB getRenderBoundingBox() {
		if (!RenderManager.getRenderConfig(this).renderLinks())
			return new AABB(getBlockPos());
		if (compiledBox == null) {
			compiledBox = new AABB(getBlockPos());
			for (BlockPos pos : getConnector().getVisibleConnection()) {
				compiledBox = compiledBox.minmax(new AABB(pos));
			}
		}
		return compiledBox;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		compiledBox = null;
	}

}
