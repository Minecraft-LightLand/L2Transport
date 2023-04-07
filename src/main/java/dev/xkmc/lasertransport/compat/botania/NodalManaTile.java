package dev.xkmc.lasertransport.compat.botania;

import dev.xkmc.lasertransport.content.capability.generic.INodeHandlerWrapper;
import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.flow.TransportHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import vazkii.botania.api.mana.ManaReceiver;

import java.util.Objects;

public record NodalManaTile(NodalGenericHandler node) implements INodeHandlerWrapper, ManaReceiver {

	@Override
	public Level getManaReceiverLevel() {
		return Objects.requireNonNull(node.entity().getThis().getLevel());
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return node.entity().getThis().getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void receiveMana(int i) {
		TransportHandler.insert(node, ManaHolder.MANA.empty().getCopy(i), false);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}
}
