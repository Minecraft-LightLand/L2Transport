package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.connector.Connector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public interface IFluidNodeBlockEntity extends INodeBlockEntity {

	boolean isFluidStackValid(FluidStack stack);

	int getMaxTransfer();

}
