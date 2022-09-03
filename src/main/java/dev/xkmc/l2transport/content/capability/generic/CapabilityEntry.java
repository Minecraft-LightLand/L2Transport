package dev.xkmc.l2transport.content.capability.generic;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.function.Function;

public record CapabilityEntry<T>(ResourceLocation id, Capability<T> cap, Function<T, HandlerWrapper> parser) {

}
