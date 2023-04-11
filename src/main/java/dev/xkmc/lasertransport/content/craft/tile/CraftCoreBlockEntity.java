package dev.xkmc.lasertransport.content.craft.tile;

import com.mojang.datafixers.util.Either;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.craft.block.ConnectBlockMethod;
import dev.xkmc.lasertransport.content.craft.block.ItemHolderNodeBlock;
import dev.xkmc.lasertransport.content.craft.block.Orientation;
import dev.xkmc.lasertransport.content.craft.logic.CraftGrid;
import dev.xkmc.lasertransport.content.craft.logic.DelegatedCraftContainer;
import dev.xkmc.lasertransport.content.craft.logic.ScanInfo;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import dev.xkmc.lasertransport.util.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SerialClass
public class CraftCoreBlockEntity extends ItemHolderBlockEntity {

	@SerialClass.SerialField(toClient = true)
	private final ArrayList<BlockPos> targets = new ArrayList<>();

	@SerialClass.SerialField
	private ScanInfo info = ScanInfo.dummy(Direction.NORTH);

	@SerialClass.SerialField
	private boolean shouldRevalidate = true, shouldCraft = false;

	@Nullable
	private CraftingRecipe recipe;

	public CraftCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (shouldRevalidate) {
			shouldRevalidate = false;
			if (level != null && !level.isClientSide())
				revalidate();
		}
		if (shouldCraft) {
			shouldCraft = false;
			if (level != null && !level.isClientSide) {
				tryCraft();
			}
		}
		super.tick();
	}

	@Override
	public void markDirty() {
		shouldCraft = true;
		super.markDirty();
	}

	@Override
	public boolean canInsert() {
		return false;
	}

	@Override
	public void setRemoved() {
		if (level != null) {
			for (BlockPos pos : targets) {
				level.destroyBlock(pos, true);
			}
			targets.clear();
		}
		super.setRemoved();
	}

	public void markRevalidate() {
		shouldRevalidate = true;
	}

	private void revalidate() {
		assert level != null;
		var scanned = ScanInfo.scan(level, getBlockPos(), getOrientation());
		if (!scanned.getSecond().isEmpty()) {
			for (BlockPos pos : scanned.getSecond()) {
				level.destroyBlock(pos, true);
			}
			for (BlockPos pos : scanned.getFirst()) {
				level.destroyBlock(pos, true);
			}
			level.destroyBlock(getBlockPos(), true);
			return;
		}
		Orientation base_orient = getOrientation();
		for (BlockPos pos : targets) {
			if (!scanned.getFirst().contains(pos)) {
				level.destroyBlock(pos, true);
			}
		}
		for (BlockPos pos : scanned.getFirst()) {
			Orientation orientation = ConnectBlockMethod.connect(level, pos, base_orient.facing);
			BlockState state = LTBlocks.B_CRAFT_SIDE.getDefaultState()
					.setValue(ItemHolderNodeBlock.ORIENTATION_SIDE, orientation.ordinal);
			if (level.getBlockEntity(pos) instanceof CraftSideBlockEntity be) {
				be.target = new Holder(getBlockPos());
				be.markDirty();
			}
			level.setBlock(pos, state, 2);
		}
		targets.clear();
		targets.addAll(scanned.getFirst());
		level.setBlock(getBlockPos(), getBlockState().setValue(ItemHolderNodeBlock.ORIENTATION_CORE,
				ConnectBlockMethod.connect(level, getBlockPos(), base_orient.facing).ordinal), 2);
		generateScanInfo(false);
		markDirty();
	}

	private Orientation getOrientation() {
		return Orientation.from(getBlockState().getValue(ItemHolderNodeBlock.ORIENTATION_CORE));
	}

	public void generateScanInfo(boolean rotate) {
		Direction y_neg = info.y_neg();
		Direction facing = getOrientation().facing;
		if (y_neg.getAxis() == facing.getAxis()) {
			y_neg = facing.getAxis() == Direction.Axis.Y ? Direction.NORTH : Direction.UP;
		}
		if (rotate) {
			y_neg = ScanInfo.getXAxis(facing, y_neg).getOpposite();
		}
		info = ScanInfo.from(facing, y_neg, getBlockPos(), targets);
		markDirty();
	}

	public List<Either<Component, TooltipComponent>> getTooltip() {
		List<Either<Component, TooltipComponent>> ans = new ArrayList<>();
		if (level != null) {
			CraftGrid tooltip = info.compost(level, getBlockPos(), targets);
			ans.add(Either.right(tooltip));
		}
		return ans;
	}

	public void tryCraft() {
		assert level != null;
		if (!items.getStackInSlot(0).isEmpty()) return;
		DelegatedCraftContainer cont = new DelegatedCraftContainer(info.compost(level, getBlockPos(), targets));
		if (cont.isEmpty()) return;
		if (recipe != null) {
			if (recipe.matches(cont, level)) {
				assemble(recipe, cont);
				return;
			}
		}
		var opt = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, cont, level);
		if (opt.isPresent()) {
			recipe = opt.get();
			assemble(recipe, cont);
		}
	}

	private void assemble(CraftingRecipe recipe, DelegatedCraftContainer cont) {
		assert level != null;
		ItemStack stack = recipe.assemble(cont);
		List<ItemStack> remainder = new ArrayList<>();
		remainder.add(stack);
		for (BlockPos pos : targets) {
			if (level.getBlockEntity(pos) instanceof CraftSideBlockEntity be) {
				ItemStack consume = be.items.extractItem(0, cont.min, false);
				if (!consume.isEmpty()) {
					ItemStack remain = consume.getCraftingRemainingItem();
					remainder.add(remain);
				}
			}
		}
		for (ItemStack result : remainder) {
			ItemStack toAdd = result.copy();
			toAdd.setCount(result.getCount() * cont.min);
			items.forceAdd(toAdd);
		}
	}

}
