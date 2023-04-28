package dev.xkmc.lasertransport.init;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2library.serial.network.PacketHandler;
import dev.xkmc.lasertransport.compat.CompatHandler;
import dev.xkmc.lasertransport.content.items.select.SelectorRegistry;
import dev.xkmc.lasertransport.events.ItemConvertEvents;
import dev.xkmc.lasertransport.init.data.LTModConfig;
import dev.xkmc.lasertransport.init.data.LangData;
import dev.xkmc.lasertransport.init.data.RecipeGen;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import dev.xkmc.lasertransport.init.registrate.LTItems;
import dev.xkmc.lasertransport.init.registrate.LTMenus;
import dev.xkmc.lasertransport.network.SetFluidFilterToServer;
import dev.xkmc.lasertransport.network.SetItemFilterToServer;
import dev.xkmc.lasertransport.network.SetNumberToServer;
import dev.xkmc.lasertransport.network.SetSelectedToServer;
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
@Mod(LaserTransport.MODID)
public class LaserTransport {

	public static final String MODID = "lasertransport";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandler HANDLER = new PacketHandler(new ResourceLocation(MODID, "main"), 1,
			e -> e.create(SetSelectedToServer.class, NetworkDirection.PLAY_TO_SERVER),
			e -> e.create(SetNumberToServer.class, NetworkDirection.PLAY_TO_SERVER),
			e -> e.create(SetItemFilterToServer.class, NetworkDirection.PLAY_TO_SERVER),
			e -> e.create(SetFluidFilterToServer.class, NetworkDirection.PLAY_TO_SERVER)
	);

	private static void registerRegistrates(IEventBus bus) {
		LTBlocks.register();
		LTItems.register();
		LTMenus.register();
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::addTranslations);
		CompatHandler.register();
	}

	private static void registerForgeEvents() {
		MinecraftForge.EVENT_BUS.register(ItemConvertEvents.class);
	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(LaserTransport::setup);
		bus.addListener(EventPriority.LOWEST, LaserTransport::gatherData);
	}

	public LaserTransport() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		registerModBusEvents(bus);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> LaserTransportClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
		registerRegistrates(bus);
		registerForgeEvents();
		LTModConfig.init();
	}

	private static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SelectorRegistry.register();

		});
	}

	public static void gatherData(GatherDataEvent event) {
	}

}
