package dev.xkmc.l2transport.init.registrate;

import dev.xkmc.l2library.block.BlockProxy;
import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2transport.content.tile.base.SidedBlockEntity;
import dev.xkmc.l2transport.content.tile.block.ItemTransferBlock;
import dev.xkmc.l2transport.content.tile.block.NodeSetFilter;
import dev.xkmc.l2transport.content.tile.client.ItemNodeRenderer;
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

	public static final BlockEntry<DelegateBlock> B_SIDED, B_ITEM_SIMPLE, B_ITEM_ORDERED, B_ITEM_SYNCED, B_ITEM_DISTRIBUTE, B_ITEM_RETRIEVE;

	public static final BlockEntityEntry<SimpleItemNodeBlockEntity> TE_ITEM_SIMPLE;
	public static final BlockEntityEntry<OrderedItemNodeBlockEntity> TE_ITEM_ORDERED;
	public static final BlockEntityEntry<SyncedItemNodeBlockEntity> TE_ITEM_SYNCED;
	public static final BlockEntityEntry<DistributeItemNodeBlockEntity> TE_ITEM_DISTRIBUTE;
	public static final BlockEntityEntry<RetrieverItemNodeBlockEntity> TE_ITEM_RETRIEVE;
	public static final BlockEntityEntry<SidedBlockEntity> TE_SIDED;

	static {
		{

			DelegateBlockProperties NOLIT = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.noOcclusion().lightLevel(bs -> 7)
					.isRedstoneConductor((a, b, c) -> false));

			DelegateBlockProperties LIT = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
					.isRedstoneConductor((a, b, c) -> false));

			B_ITEM_SIMPLE = L2Transport.REGISTRATE.block("node_item_simple",
							(p) -> DelegateBlock.newBaseBlock(LIT, NodeSetFilter.INSTANCE, ItemTransferBlock.SIMPLE))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_ORDERED = L2Transport.REGISTRATE.block("node_item_ordered",
							(p) -> DelegateBlock.newBaseBlock(LIT, NodeSetFilter.INSTANCE, ItemTransferBlock.ORDERED))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_SYNCED = L2Transport.REGISTRATE.block("node_item_synced",
							(p) -> DelegateBlock.newBaseBlock(LIT, NodeSetFilter.INSTANCE, ItemTransferBlock.SYNCED))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_DISTRIBUTE = L2Transport.REGISTRATE.block("node_item_distribute",
							(p) -> DelegateBlock.newBaseBlock(LIT, NodeSetFilter.INSTANCE, ItemTransferBlock.DISTRIBUTE))
					.blockstate(LTBlocks::genNodeModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();

			B_ITEM_RETRIEVE = L2Transport.REGISTRATE.block("node_item_retrieve",
							(p) -> DelegateBlock.newBaseBlock(LIT, NodeSetFilter.INSTANCE, BlockProxy.ALL_DIRECTION, ItemTransferBlock.RETRIEVE))
					.blockstate(LTBlocks::genFacingModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultLoot().defaultLang().simpleItem().register();


			B_SIDED = L2Transport.REGISTRATE.block("node_sided",
							(p) -> DelegateBlock.newBaseBlock(NOLIT, BlockProxy.ALL_DIRECTION, ItemTransferBlock.SIDED))
					.blockstate(LTBlocks::genFacingModel).tag(BlockTags.MINEABLE_WITH_PICKAXE)
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
			TE_SIDED = L2Transport.REGISTRATE.blockEntity("node_sided", SidedBlockEntity::new)
					.validBlock(B_SIDED).register();
		}
	}

	private static void genNodeModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.getEntry()).forAllStates(bs -> {
			boolean lit = bs.getValue(BlockStateProperties.LIT);
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

	private static void genFacingModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.directionalBlock(ctx.getEntry(), bs -> {
			boolean lit = bs.hasProperty(BlockStateProperties.LIT) && bs.getValue(BlockStateProperties.LIT);
			String model = ctx.getName() + (lit ? "_lit" : "");
			String name = ctx.getName().replace('_', '/') + (lit ? "_lit" : "");
			return pvd.models()
					.withExistingParent(model, lit ?
							new ResourceLocation(L2Transport.MODID, "block/node_side_large") :
							new ResourceLocation(L2Transport.MODID, "block/node_side"))
					.texture("all", new ResourceLocation(L2Transport.MODID, "block/" + name))
					.renderType("cutout");
		});
	}

	public static void register() {
	}

}
