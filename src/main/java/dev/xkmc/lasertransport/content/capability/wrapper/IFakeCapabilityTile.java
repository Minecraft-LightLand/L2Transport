package dev.xkmc.lasertransport.content.capability.wrapper;

import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public interface IFakeCapabilityTile {

	<C> LazyOptional<C> getCapability(@NotNull ICapabilityHolder<C> cap);

}
