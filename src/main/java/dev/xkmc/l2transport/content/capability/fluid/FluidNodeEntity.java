package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.connector.Connector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public interface FluidNodeEntity {

	boolean isFluidStackValid(FluidStack stack);

	Connector getConnector();

	@Nullable
	Level getLevel();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	BlockPos getBlockPos();

	int getMaxTransfer();

}
