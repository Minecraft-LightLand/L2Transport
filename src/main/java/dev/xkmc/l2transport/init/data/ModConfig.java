package dev.xkmc.l2transport.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

	public static class Common {

		public final ForgeConfigSpec.IntValue defaultCoolDown;
		public final ForgeConfigSpec.IntValue defaultNodeDistance;
		public final ForgeConfigSpec.IntValue defaultFluidPacket;

		Common(ForgeConfigSpec.Builder builder) {
			defaultCoolDown = builder.comment("Default node cool down in ticks")
					.defineInRange("defaultCoolDown", 80, 1, 8000);
			defaultNodeDistance = builder.comment("Default node distance")
					.defineInRange("defaultNodeDistance", 16, 1, 64);
			defaultFluidPacket = builder.comment("Default fluid transfer amount")
					.defineInRange("defaultFluidPacket", 1000, 1, 80000);
		}

	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);
	}


}
