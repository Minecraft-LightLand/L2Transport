package dev.xkmc.lasertransport.init.data;

import dev.xkmc.lasertransport.init.LaserTransport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagGen {

	public static final TagKey<Item> SELECTABLE = ItemTags.create(new ResourceLocation(LaserTransport.MODID, "selectable"));

	public static final TagKey<Block> RETRIEVABLE = BlockTags.create(new ResourceLocation(LaserTransport.MODID, "retrievable"));

}
