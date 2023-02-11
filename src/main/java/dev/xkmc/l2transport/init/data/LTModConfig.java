package dev.xkmc.l2transport.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class LTModConfig {

	public static class Client {

		public final ForgeConfigSpec.BooleanValue renderLinks;
		public final ForgeConfigSpec.IntValue renderRange;
		public final ForgeConfigSpec.BooleanValue selectionDisplayRequireShift;
		public final ForgeConfigSpec.BooleanValue selectionScrollRequireShift;
		public final ForgeConfigSpec.IntValue scrollDelay;
		public final ForgeConfigSpec.DoubleValue scrollTick;

		Client(ForgeConfigSpec.Builder builder) {
			renderLinks = builder.comment("Render Links by default")
					.define("renderLinks", true);
			renderRange = builder.comment("Render Range")
					.defineInRange("renderRange", 64, 0, 256);
			selectionDisplayRequireShift = builder.comment("Render Selection only when pressing shift")
					.define("selectionDisplayRequireShift", false);
			selectionScrollRequireShift = builder.comment("Scroll for selection only when pressing shift")
					.define("selectionScrollRequireShift", true);
			scrollDelay = builder.comment("Filter adjustment delay")
					.defineInRange("scrollDelay", 10, 0, 200);
			scrollTick = builder.comment("Scroll sensitivity")
					.defineInRange("scrollTick", 1, 0.01, 10000);
		}

	}

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

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();

		final Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = clientPair.getRight();
		CLIENT = clientPair.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
	}


}
