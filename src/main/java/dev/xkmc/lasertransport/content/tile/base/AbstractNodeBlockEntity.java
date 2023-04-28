package dev.xkmc.lasertransport.content.tile.base;

import dev.xkmc.l2library.block.BlockContainer;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.lasertransport.content.capability.base.INodeBlockEntity;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.client.overlay.TooltipBuilder;
import dev.xkmc.lasertransport.content.client.overlay.TooltipType;
import dev.xkmc.lasertransport.content.connector.IConnector;
import dev.xkmc.lasertransport.content.items.upgrades.*;
import dev.xkmc.lasertransport.init.data.LTModConfig;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@SerialClass
public abstract class AbstractNodeBlockEntity<BE extends AbstractNodeBlockEntity<BE>> extends ConnectionRenderBlockEntity
		implements TickableBlockEntity, ILinkableNode, INodeBlockEntity, BlockContainer, IUpgradableBlock, PopContentTile {

	protected final Set<UpgradeFlag> flags = new HashSet<>();

	@SerialClass.SerialField(toClient = true)
	private final HashMap<UpgradeFlag, ItemStack> upgrades = new HashMap<>();

	public AbstractNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		flags.add(UpgradeFlag.COOL_DOWN);
		flags.add(UpgradeFlag.DISTANCE);
		flags.add(UpgradeFlag.REDSTONE);
	}

	// upgrade related

	public List<Upgrade> getUpgrades() {
		return upgrades.values().stream().map(e -> ((UpgradeItem) e.getItem()).getUpgrade()).collect(Collectors.toList());
	}

	@Nullable
	public Upgrade getUpgrade(UpgradeFlag flag) {
		return upgrades.containsKey(flag) ? ((UpgradeItem) upgrades.get(flag).getItem()).getUpgrade() : null;
	}

	public boolean acceptUpgrade(GenericItemStack<UpgradeItem> item) {
		return flags.contains(item.item().getFlag());
	}

	@ServerOnly
	public Optional<ItemStack> addUpgrade(GenericItemStack<UpgradeItem> item) {
		if (!acceptUpgrade(item)) {
			return Optional.empty();
		}
		ItemStack ans = upgrades.put(item.item().getFlag(), item.stack());
		if (ans == null) ans = ItemStack.EMPTY;
		sync();
		ans.setCount(1);
		return Optional.of(ans);
	}

	public List<ItemStack> popContents() {
		var ans = new ArrayList<>(upgrades.values());
		for (var stack : ans) {
			stack.setCount(1);
		}
		upgrades.clear();
		return ans;
	}

	@Override
	public List<Container> getContainers() {
		return List.of(new SimpleContainer(upgrades.values().toArray(ItemStack[]::new)));
	}

	public boolean isReady() {
		if (!INodeBlockEntity.super.isReady()) return false;
		if (level != null && getUpgrade(UpgradeFlag.REDSTONE) instanceof ValveUpgrade) {
			return !level.hasNeighborSignal(getThis().getBlockPos());
		}
		return true;
	}

	// base functionality

	private boolean needUpdate = false;
	private boolean needSync = false;

	@Override
	public void tick() {
		if (level != null && !level.isClientSide && needUpdate) {
			getConnector().perform();
			needUpdate = false;
			needSync = true;
		}
		if (level != null && !level.isClientSide && needSync) {
			sync();
			needSync = false;
		}
		getConnector().tick();
	}

	public void markDirty() {
		needSync = true;
	}

	public int getMaxCoolDown() {
		int cd = LTModConfig.COMMON.defaultCoolDown.get();
		for (Upgrade u : getUpgrades()) {
			cd = u.getMaxCoolDown(cd);
		}
		return Math.max(1, cd);
	}

	public abstract IConnector getConnector();

	public final void refreshCoolDown(BlockPos target, boolean success, boolean simulate) {
		getConnector().refreshCoolDown(target, success, simulate);
		needUpdate = true;
		if (level != null && !simulate && getUpgrade(UpgradeFlag.REDSTONE) instanceof WatchUpgrade) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.TRIGGERED, true));
			level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);
		}
	}

	// linkable

	@Override
	public int getMaxDistanceSqr() {
		int cd = LTModConfig.COMMON.defaultNodeDistance.get();
		for (Upgrade u : getUpgrades()) {
			cd = u.getMaxDistance(cd);
		}
		return cd * cd;
	}

	@Override
	public LangData link(BlockPos pos, Level level) {
		if (pos.equals(getBlockPos()) || level != this.level)
			return LangData.MSG_LINKER_CANCEL;
		getConnector().link(pos.immutable());
		sync();
		return LangData.MSG_LINKER_SUCCEED;
	}

	@Override
	public void validate() {
		getConnector().removeIf(e -> !isTargetValid(e));
		sync();
	}

	@Override
	public void removeAll() {
		getConnector().removeIf(e -> true);
		sync();
	}

	// render related

	@Override
	public TooltipBuilder getTooltips() {
		var ans = new TooltipBuilder();
		ans.add(TooltipType.NAME, Component.translatable(getBlockState().getBlock().getDescriptionId()).withStyle(ChatFormatting.YELLOW));
		if (getConnector().getVisibleConnection().stream().anyMatch(e -> !isTargetValid(e))) {
			ans.add(TooltipType.DESC, LangData.INVALID.get());
		}
		for (var e : upgrades.values()) {
			ans.add(TooltipType.UPGRADE, e.getHoverName().copy().withStyle(ChatFormatting.GOLD));
			if (ans.hasShiftDown()) {
				ans.add(TooltipType.UPGRADE, ((UpgradeItem) e.getItem()).getUpgrade().getDesc().withStyle(ChatFormatting.DARK_GREEN));
			}
		}
		if (!ans.hasShiftDown()) {
			ans.add(TooltipType.STAT, LangData.INFO_SPEED.getLiteral(getMaxCoolDown() / 20f));
			getConfig().addTooltips(ans);
		} else {
			ans.add(TooltipType.DESC, getConfig().getType().getDesc());
		}
		return ans;
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		if (getUpgrade(UpgradeFlag.LEVEL) != null) {
			return true;
		}
		if (level != null && (level.getBlockEntity(pos) instanceof ILinkableNode node && node.crossDimension())) {
			return true;
		}
		return getValidTarget().getHolder(this, pos).resolve().isPresent();
	}


	public abstract ICapabilityHolder<?> getValidTarget();

}
