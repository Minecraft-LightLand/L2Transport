package dev.xkmc.lasertransport.content.tile.extend;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MultiLevelTarget(ResourceLocation dim, BlockPos pos) implements Comparable<MultiLevelTarget> {

	public static final ResourceLocation EMPTY = new ResourceLocation("empty");

	public static final MultiLevelTarget NULL = new MultiLevelTarget(EMPTY, BlockPos.ZERO);

	public static MultiLevelTarget of(Level level, BlockPos pos) {
		return new MultiLevelTarget(level.dimension().location(), pos);
	}

	@Override
	public int compareTo(@NotNull MultiLevelTarget o) {
		int ans = dim.compareTo(o.dim());
		if (ans == 0) {
			ans = pos.compareTo(o.pos());
		}
		return ans;
	}

	@Nullable
	public Level getLevel(Level level) {
		if (level.getServer() == null) return null;
		return level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, dim()));
	}
}
