package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.client.overlay.TooltipType;
import dev.xkmc.l2transport.content.configurables.BaseConfigurable;
import dev.xkmc.l2transport.content.configurables.IConfigurableFilter;
import dev.xkmc.l2transport.content.tile.base.CoolDownType;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

@SerialClass
public class ExtractConnector extends SimpleConnector implements IExtractor {

	private final Supplier<BlockPos> target;

	@SerialClass.SerialField(toClient = true)
	private int extractCoolDown = 0;

	@SerialClass.SerialField(toClient = true)
	private CoolDownType extractColor = CoolDownType.GREY;

	public ExtractConnector(IntSupplier max, BaseConfigurable limit, Supplier<BlockPos> target) {
		super(max, limit);
		this.target = target;
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
		return getPos() == null ? List.of(target.get()) : List.of(getPos(), target.get());
	}

	@Override
	public List<BlockPos> getAvailableTarget() {
		return getPos() == null ? List.of() : List.of(getPos());
	}

	@Override
	public int getCoolDown(BlockPos pos) {
		return pos.equals(target.get()) ? Math.min(getMaxCoolDown(pos), extractCoolDown) : super.getCoolDown(pos);
	}

	@Override
	public CoolDownType getType(BlockPos pos) {
		return pos.equals(target.get()) ? extractColor : super.getType(pos);
	}

	@Override
	public <T> void addTooltips(TooltipBuilder list, IConfigurableFilter filter) {
		if (filter.shouldDisplay()) {
			list.add(TooltipType.FILTER, LangData.INFO_FILTER.get(filter.getFilterDesc()));
		}
		list.add(TooltipType.GATE, LangData.INFO_EXTRACT.getLiteral(filter.getMaxTransfer()));
		list.add(TooltipType.STAT, LangData.INFO_SPEED.getLiteral(maxCoolDown.getAsInt() / 20f));
		list.add(TooltipType.DESC, LangData.RETRIEVE.get());
	}

	public void performExtract(boolean success) {
		extractCoolDown = getMaxCoolDown(target.get());
		extractColor = success ? CoolDownType.RETRIEVE : CoolDownType.INVALID_RETRIEVE;
	}

	public boolean mayExtract() {
		return extractCoolDown == 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (extractCoolDown > 0) {
			extractCoolDown--;
			if (extractCoolDown == 0) {
				extractColor = CoolDownType.RETRIEVE;
			}
		}
	}

}
