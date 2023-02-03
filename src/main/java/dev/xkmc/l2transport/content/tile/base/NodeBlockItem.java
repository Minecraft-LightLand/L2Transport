package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NodeBlockItem extends BlockItem {

	private final LangData data;

	public NodeBlockItem(Block block, Properties properties, LangData data) {
		super(block, properties);
		this.data = data;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(data.get());
		super.appendHoverText(stack, level, list, flag);
	}
}
