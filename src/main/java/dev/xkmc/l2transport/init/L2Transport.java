package dev.xkmc.l2transport.init;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2library.serial.network.PacketHandler;
import dev.xkmc.l2transport.compat.CompatHandler;
import dev.xkmc.l2transport.content.items.select.ItemSelector;
import dev.xkmc.l2transport.content.items.tools.ILinker;
import dev.xkmc.l2transport.content.items.upgrades.UpgradeItem;
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
			new ItemSelector(e -> e instanceof ILinker,
					LTItems.LINKER.asStack(),
					LTItems.VALIDATOR.asStack(),
					LTItems.CLEAR.asStack(),
					LTItems.ROTATE.asStack(),
					LTItems.CONFIG.asStack());

			new ItemSelector(e -> e instanceof UpgradeItem,
					LTItems.VALVE_UP.asStack(),
					LTItems.WATCH_UP.asStack(),
					LTItems.SPEED_UP_0.asStack(),
					LTItems.DIST_UP_0.asStack(),
					LTItems.THR_UP_0.asStack(),
					LTItems.DROP_UP.asStack(),
					LTItems.PLACE_UP.asStack());

			new ItemSelector(
					LTBlocks.B_ITEM_RETRIEVE.asStack(),
					LTBlocks.B_ITEM_SIMPLE.asStack(),
					LTBlocks.B_ITEM_ORDERED.asStack(),
					LTBlocks.B_ITEM_DISTRIBUTE.asStack(),
					LTBlocks.B_ITEM_SYNCED.asStack()
			);

			new ItemSelector(
					LTBlocks.B_FLUID_RETRIEVE.asStack(),
					LTBlocks.B_FLUID_SIMPLE.asStack(),
					LTBlocks.B_FLUID_ORDERED.asStack(),
					LTBlocks.B_FLUID_DISTRIBUTE.asStack(),
					LTBlocks.B_FLUID_SYNCED.asStack()
			);

			new ItemSelector(
					LTBlocks.B_FLUX_RETRIEVE.asStack(),
					LTBlocks.B_FLUX_SIMPLE.asStack(),
					LTBlocks.B_FLUX_ORDERED.asStack()
			);

		});
	}

	public static void gatherData(GatherDataEvent event) {
		LangData.addTranslations(REGISTRATE::addRawLang);
	}

}
