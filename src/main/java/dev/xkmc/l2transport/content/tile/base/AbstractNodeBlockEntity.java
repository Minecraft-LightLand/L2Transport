package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.connector.IConnector;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.content.upgrades.Upgrade;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;
import dev.xkmc.l2transport.content.upgrades.UpgradeItem;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.init.data.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SerialClass
public abstract class AbstractNodeBlockEntity<BE extends AbstractNodeBlockEntity<BE>> extends ConnectionRenderBlockEntity
		implements TickableBlockEntity, IRenderableNode, ILinkableNode, INodeBlockEntity {

	protected final Set<UpgradeFlag> flags = new HashSet<>();

	@SerialClass.SerialField(toClient = true)
	private final LinkedHashSet<Item> upgrades = new LinkedHashSet<>();

	public AbstractNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		flags.add(UpgradeFlag.COOL_DOWN);
		flags.add(UpgradeFlag.DISTANCE);
	}

	// upgrade related

	public List<Upgrade> getUpgrades() {
		return upgrades.stream().map(e -> ((UpgradeItem) e).getUpgrade()).collect(Collectors.toList());
	}

	public boolean addUpgrade(UpgradeItem item) {
		//TODO upgrae add/remove mechanics
		if (upgrades.contains(item)) {
			return false;
		}
		if (!flags.contains(item.getFlag())) {
			return false;
		}
		upgrades.add(item);
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
		return ans;
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		return getCapability(getValidTarget(), pos).resolve().isPresent();
	}

	public abstract Capability<?> getValidTarget();

}
