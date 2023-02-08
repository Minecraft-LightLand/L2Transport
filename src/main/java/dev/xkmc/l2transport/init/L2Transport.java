package dev.xkmc.l2transport.init;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2library.serial.network.PacketHandler;
import dev.xkmc.l2transport.compat.CompatHandler;
import dev.xkmc.l2transport.init.data.LTModConfig;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.init.data.RecipeGen;
import dev.xkmc.l2transport.init.registrate.LTBlocks;
import dev.xkmc.l2transport.init.registrate.LTItems;
import dev.xkmc.l2transport.network.SetSelectedToServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Transport.MODID)
public class L2Transport {

	public static final String MODID = "l2transport";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandler HANDLER = new PacketHandler(new ResourceLocation(MODID, "main"), 1,
			e -> e.create(SetSelectedToServer.class, NetworkDirection.PLAY_TO_SERVER));

	private static void registerRegistrates(IEventBus bus) {
		LTBlocks.register();
		LTItems.register();
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		CompatHandler.register();
	}

	private static void registerForgeEvents() {
	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(L2Transport::setup);
		bus.addListener(EventPriority.LOWEST, L2Transport::gatherData);
	}

	public L2Transport() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		registerModBusEvents(bus);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> L2TransportClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
		registerRegistrates(bus);
		registerForgeEvents();
		LTModConfig.init();
	}

	private static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	public static void gatherData(GatherDataEvent event) {
		LangData.addTranslations(REGISTRATE::addRawLang);
	}

}
