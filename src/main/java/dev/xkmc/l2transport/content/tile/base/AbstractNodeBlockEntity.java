package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2library.block.BlockContainer;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.connector.IConnector;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.content.upgrades.*;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.init.data.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@SerialClass
public abstract class AbstractNodeBlockEntity<BE extends AbstractNodeBlockEntity<BE>> extends ConnectionRenderBlockEntity
		implements TickableBlockEntity, ILinkableNode, INodeBlockEntity, BlockContainer, IUpgradableBlock {

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
		return Optional.of(ans);
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

	private boolean dirty = false;

	@Override
	public void tick() {
		if (level != null && !level.isClientSide && dirty) {
			getConnector().perform();
			sync();
			dirty = false;
		}
		getConnector().tick();
	}

	public int getMaxCoolDown() {
		int cd = ModConfig.COMMON.defaultCoolDown.get();
		for (Upgrade u : getUpgrades()) {
			cd = u.getMaxCoolDown(cd);
		}
		return Math.max(1, cd);
	}

	public abstract IConnector getConnector();

	public final void refreshCoolDown(BlockPos target, boolean success, boolean simulate) {
		getConnector().refreshCoolDown(target, success, simulate);
		dirty = true;
		if (level != null && !simulate && getUpgrade(UpgradeFlag.REDSTONE) instanceof WatchUpgrade) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.TRIGGERED, true));
			level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);
		}
	}

	// linkable

	@Override
	public int getMaxDistanceSqr() {
		int cd = ModConfig.COMMON.defaultNodeDistance.get();
		for (Upgrade u : getUpgrades()) {
			cd = u.getMaxDistance(cd);
		}
		return cd * cd;
	}

	@Override
	public void link(BlockPos pos) {
		if (pos.equals(getBlockPos()))
			return;
		getConnector().link(pos);
		sync();
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
		for (var e : getUpgrades()) {
			ans.add(TooltipType.UPGRADE, e.getDesc());
		}
		return ans;
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		return getCapability(getValidTarget(), pos).resolve().isPresent();
	}

	public abstract Capability<?> getValidTarget();

}
