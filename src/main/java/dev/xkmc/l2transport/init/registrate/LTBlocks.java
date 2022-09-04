package dev.xkmc.l2transport.init.registrate;

import dev.xkmc.l2library.block.BlockProxy;
import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2transport.content.tile.block.*;
import dev.xkmc.l2transport.content.tile.client.FluidNodeRenderer;
import dev.xkmc.l2transport.content.tile.client.ItemNodeRenderer;
import dev.xkmc.l2transport.content.tile.client.NodeRenderer;
import dev.xkmc.l2transport.content.tile.extend.ExtendedBlockEntity;
import dev.xkmc.l2transport.content.tile.extend.SidedBlockEntity;
import dev.xkmc.l2transport.content.tile.fluid.*;
import dev.xkmc.l2transport.content.tile.flux.OrderedFluxNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.flux.RetrieverFluxNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.flux.SimpleFluxNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.item.*;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

/**
 * handles blocks and block entities
 */
public class LTBlocks {

	static {
		L2Transport.REGISTRATE.creativeModeTab(() -> LTItems.TAB_MAIN);
	}

	public static final BlockEntry<DelegateBlock> B_SIDED, B_EXTENDED,
			B_ITEM_SIMPLE, B_ITEM_ORDERED, B_ITEM_SYNCED, B_ITEM_DISTRIBUTE, B_ITEM_RETRIEVE,
			B_FLUID_SIMPLE, B_FLUID_ORDERED, B_FLUID_SYNCED, B_FLUID_DISTRIBUTE, B_FLUID_RETRIEVE,
			B_FLUX_SIMPLE, B_FLUX_ORDERED, B_FLUX_RETRIEVE;

	public static final BlockEntityEntry<SidedBlockEntity> TE_SIDED;
	public static final BlockEntityEntry<ExtendedBlockEntity> TE_EXTENDED;

	public static final BlockEntityEntry<SimpleItemNodeBlockEntity> TE_ITEM_SIMPLE;
	public static final BlockEntityEntry<OrderedItemNodeBlockEntity> TE_ITEM_ORDERED;
	public static final BlockEntityEntry<SyncedItemNodeBlockEntity> TE_ITEM_SYNCED;
	public static final BlockEntityEntry<DistributeItemNodeBlockEntity> TE_ITEM_DISTRIBUTE;
	public static final BlockEntityEntry<RetrieverItemNodeBlockEntity> TE_ITEM_RETRIEVE;

	public static final BlockEntityEntry<SimpleFluidNodeBlockEntity> TE_FLUID_SIMPLE;
	public static final BlockEntityEntry<OrderedFluidNodeBlockEntity> TE_FLUID_ORDERED;
	public static final BlockEntityEntry<SyncedFluidNodeBlockEntity> TE_FLUID_SYNCED;
	public static final BlockEntityEntry<DistributeFluidNodeBlockEntity> TE_FLUID_DISTRIBUTE;
	public static final BlockEntityEntry<RetrieverFluidNodeBlockEntity> TE_FLUID_RETRIEVE;

	public static final BlockEntityEntry<SimpleFluxNodeBlockEntity> TE_FLUX_SIMPLE;
	public static final BlockEntityEntry<OrderedFluxNodeBlockEntity> TE_FLUX_ORDERED;
	public static final BlockEntityEntry<RetrieverFluxNodeBlockEntity> TE_FLUX_RETRIEVE;

	static {

		DelegateBlockProperties NOLIT = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
				.noOcclusion().lightLevel(bs -> 7)
				.isRedstoneConductor((a, b, c) -> false));

		DelegateBlockProperties LIT = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
				.noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
				.isRedstoneConductor((a, b, c) -> false));

		{

			B_SIDED = L2Transport.REGISTRATE.block("node_sided",
							(p) -> DelegateBlock.newBaseBlock(NOLIT, BlockProxy.ALL_DIRECTION, ExtensionBlock.SIDED, ExtensionBlock.SIMPLE))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			TE_SIDED = L2Transport.REGISTRATE.blockEntity("node_sided", SidedBlockEntity::new)
					.validBlock(B_SIDED).renderer(() -> NodeRenderer::new).register();

			B_EXTENDED = L2Transport.REGISTRATE.block("node_extended",
							(p) -> DelegateBlock.newBaseBlock(NOLIT, ExtensionBlock.EXTENDED, ExtensionBlock.SIMPLE))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			TE_EXTENDED = L2Transport.REGISTRATE.blockEntity("node_extended", ExtendedBlockEntity::new)
					.validBlock(B_EXTENDED).renderer(() -> NodeRenderer::new).register();
		}
		{
			B_ITEM_SIMPLE = L2Transport.REGISTRATE.block("node_item_simple",
							(p) -> DelegateBlock.newBaseBlock(LIT, ItemNodeSetFilter.ITEM, ItemTransferBlock.SIMPLE, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_ORDERED = L2Transport.REGISTRATE.block("node_item_ordered",
							(p) -> DelegateBlock.newBaseBlock(LIT, ItemNodeSetFilter.ITEM, ItemTransferBlock.ORDERED, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_SYNCED = L2Transport.REGISTRATE.block("node_item_synced",
							(p) -> DelegateBlock.newBaseBlock(LIT, ItemNodeSetFilter.ITEM, ItemTransferBlock.SYNCED, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_DISTRIBUTE = L2Transport.REGISTRATE.block("node_item_distribute",
							(p) -> DelegateBlock.newBaseBlock(LIT, ItemNodeSetFilter.ITEM, ItemTransferBlock.DISTRIBUTE, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_RETRIEVE = L2Transport.REGISTRATE.block("node_item_retrieve",
							(p) -> DelegateBlock.newBaseBlock(LIT, ItemNodeSetFilter.ITEM, BlockProxy.ALL_DIRECTION, ItemTransferBlock.RETRIEVE, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();


			TE_ITEM_SIMPLE = L2Transport.REGISTRATE.blockEntity("node_item_simple", SimpleItemNodeBlockEntity::new)
					.validBlock(B_ITEM_SIMPLE).renderer(() -> ItemNodeRenderer::new).register();
			TE_ITEM_ORDERED = L2Transport.REGISTRATE.blockEntity("node_item_ordered", OrderedItemNodeBlockEntity::new)
					.validBlock(B_ITEM_ORDERED).renderer(() -> ItemNodeRenderer::new).register();
			TE_ITEM_SYNCED = L2Transport.REGISTRATE.blockEntity("node_item_synced", SyncedItemNodeBlockEntity::new)
					.validBlock(B_ITEM_SYNCED).renderer(() -> ItemNodeRenderer::new).register();
			TE_ITEM_DISTRIBUTE = L2Transport.REGISTRATE.blockEntity("node_item_distribute", DistributeItemNodeBlockEntity::new)
					.validBlock(B_ITEM_DISTRIBUTE).renderer(() -> ItemNodeRenderer::new).register();
			TE_ITEM_RETRIEVE = L2Transport.REGISTRATE.blockEntity("node_item_retrieve", RetrieverItemNodeBlockEntity::new)
					.validBlock(B_ITEM_RETRIEVE).renderer(() -> ItemNodeRenderer::new).register();
		}
		{
			B_FLUID_SIMPLE = L2Transport.REGISTRATE.block("node_fluid_simple",
							(p) -> DelegateBlock.newBaseBlock(LIT, FluidNodeSetFilter.FLUID, FluidTransferBlock.SIMPLE, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_FLUID_ORDERED = L2Transport.REGISTRATE.block("node_fluid_ordered",
							(p) -> DelegateBlock.newBaseBlock(LIT, FluidNodeSetFilter.FLUID, FluidTransferBlock.ORDERED, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_FLUID_SYNCED = L2Transport.REGISTRATE.block("node_fluid_synced",
							(p) -> DelegateBlock.newBaseBlock(LIT, FluidNodeSetFilter.FLUID, FluidTransferBlock.SYNCED, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_FLUID_DISTRIBUTE = L2Transport.REGISTRATE.block("node_fluid_distribute",
							(p) -> DelegateBlock.newBaseBlock(LIT, FluidNodeSetFilter.FLUID, FluidTransferBlock.DISTRIBUTE, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_FLUID_RETRIEVE = L2Transport.REGISTRATE.block("node_fluid_retrieve",
							(p) -> DelegateBlock.newBaseBlock(LIT, FluidNodeSetFilter.FLUID, BlockProxy.ALL_DIRECTION, FluidTransferBlock.RETRIEVE, ExtensionBlock.FILTER))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();


			TE_FLUID_SIMPLE = L2Transport.REGISTRATE.blockEntity("node_fluid_simple", SimpleFluidNodeBlockEntity::new)
					.validBlock(B_FLUID_SIMPLE).renderer(() -> FluidNodeRenderer::new).register();
			TE_FLUID_ORDERED = L2Transport.REGISTRATE.blockEntity("node_fluid_ordered", OrderedFluidNodeBlockEntity::new)
					.validBlock(B_FLUID_ORDERED).renderer(() -> FluidNodeRenderer::new).register();
			TE_FLUID_SYNCED = L2Transport.REGISTRATE.blockEntity("node_fluid_synced", SyncedFluidNodeBlockEntity::new)
					.validBlock(B_FLUID_SYNCED).renderer(() -> FluidNodeRenderer::new).register();
			TE_FLUID_DISTRIBUTE = L2Transport.REGISTRATE.blockEntity("node_fluid_distribute", DistributeFluidNodeBlockEntity::new)
					.validBlock(B_FLUID_DISTRIBUTE).renderer(() -> FluidNodeRenderer::new).register();
			TE_FLUID_RETRIEVE = L2Transport.REGISTRATE.blockEntity("node_fluid_retrieve", RetrieverFluidNodeBlockEntity::new)
					.validBlock(B_FLUID_RETRIEVE).renderer(() -> FluidNodeRenderer::new).register();
		}
		{
			B_FLUX_SIMPLE = L2Transport.REGISTRATE.block("node_flux_simple",
							(p) -> DelegateBlock.newBaseBlock(NOLIT, FluxTransferBlock.SIMPLE, ExtensionBlock.SIMPLE))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_FLUX_ORDERED = L2Transport.REGISTRATE.block("node_flux_ordered",
							(p) -> DelegateBlock.newBaseBlock(NOLIT, FluxTransferBlock.ORDERED, ExtensionBlock.SIMPLE))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_FLUX_RETRIEVE = L2Transport.REGISTRATE.block("node_flux_retrieve",
							(p) -> DelegateBlock.newBaseBlock(NOLIT, BlockProxy.ALL_DIRECTION, FluxTransferBlock.RETRIEVE, ExtensionBlock.SIMPLE))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			TE_FLUX_SIMPLE = L2Transport.REGISTRATE.blockEntity("node_flux_simple", SimpleFluxNodeBlockEntity::new)
					.validBlock(B_FLUX_SIMPLE).renderer(() -> NodeRenderer::new).register();
			TE_FLUX_ORDERED = L2Transport.REGISTRATE.blockEntity("node_flux_ordered", OrderedFluxNodeBlockEntity::new)
					.validBlock(B_FLUX_ORDERED).renderer(() -> NodeRenderer::new).register();
			TE_FLUX_RETRIEVE = L2Transport.REGISTRATE.blockEntity("node_flux_retrieve", RetrieverFluxNodeBlockEntity::new)
					.validBlock(B_FLUX_RETRIEVE).renderer(() -> NodeRenderer::new).register();
		}
	}

	private static void genNodeModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.getEntry()).forAllStates(bs -> {
			boolean lit = bs.hasProperty(BlockStateProperties.LIT) && bs.getValue(BlockStateProperties.LIT);
			String model = ctx.getName() + (lit ? "_lit" : "");
			String name = ctx.getName().replace('_', '/') + (lit ? "_lit" : "");
			return ConfiguredModel.builder().modelFile(pvd.models()
					.withExistingParent(model, lit ?
							new ResourceLocation("block/cube_all") :
							new ResourceLocation(L2Transport.MODID, "block/node_small"))
					.texture("all", new ResourceLocation(L2Transport.MODID, "block/" + name))
					.renderType("cutout")).build();
		});
	}

	public static void register() {
	}

}
