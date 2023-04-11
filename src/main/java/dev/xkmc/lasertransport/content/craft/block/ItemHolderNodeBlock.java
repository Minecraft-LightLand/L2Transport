package dev.xkmc.lasertransport.content.craft.block;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.lasertransport.content.craft.tile.CraftCoreBlockEntity;
import dev.xkmc.lasertransport.content.craft.tile.CraftSideBlockEntity;
import dev.xkmc.lasertransport.content.craft.tile.ItemHolderBlockEntity;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ItemHolderNodeBlock {

	public static final BlockEntityBlockMethod<ItemHolderBlockEntity> HOLDER = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_ITEM_HOLDER, ItemHolderBlockEntity.class);
	public static final BlockEntityBlockMethod<CraftSideBlockEntity> SIDE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_CRAFT_SIDE, CraftSideBlockEntity.class);
	public static final BlockEntityBlockMethod<CraftCoreBlockEntity> CORE = new BlockEntityBlockMethodImpl<>(LTBlocks.TE_CRAFT_CORE, CraftCoreBlockEntity.class);

	public static final IntegerProperty ORIENTATION_SIDE = IntegerProperty.create("orientation", 0, Orientation.size() - 1);
	public static final IntegerProperty ORIENTATION_CORE = IntegerProperty.create("orientation", 0, Orientation.size() - 2);

	public static final ItemHolderSetItem SET_ONE = new ItemHolderSetItem(true);
	public static final ItemHolderSetItem SET_ALL = new ItemHolderSetItem(false);
	public static final ItemHolderTakeItem TAKE = new ItemHolderTakeItem();
	public static final RemoveDestroyMethod REMOVE = new RemoveDestroyMethod();
	public static final CraftRestructureDetector UPDATE = new CraftRestructureDetector();
	public static final ConnectBlockMethod CONN_SIDE = new ConnectBlockMethod(ORIENTATION_SIDE);
	public static final ConnectBlockMethod CONN_CORE = new ConnectBlockMethod(ORIENTATION_CORE);

}
