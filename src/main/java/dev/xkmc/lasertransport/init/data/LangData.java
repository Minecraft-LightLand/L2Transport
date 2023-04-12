package dev.xkmc.lasertransport.init.data;

import dev.xkmc.lasertransport.init.LaserTransport;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public enum LangData {
	INFO_WHITELIST("info.whitelist", "Whitelist: %s", 1, ChatFormatting.AQUA),
	INFO_BLACKLIST("info.blacklist", "Blacklist: %s", 1, ChatFormatting.AQUA),
	INFO_GATED("info.gated", "Transfer gated to %s", 1, ChatFormatting.AQUA),
	INFO_EXTRACT("info.extract", "Extract in %s", 1, ChatFormatting.AQUA),
	INFO_SPEED("info.speed", "Transfer cool down: %s", 1, ChatFormatting.AQUA),
	INFO_SYNC("info.sync", "Force transfer %s to all targets", 1, ChatFormatting.AQUA),
	INFO_FLUX("info.flux", "Transmitting %s", 1, ChatFormatting.AQUA),

	SIMPLE("connect.simple", "Connects to only one target. When adding filter, only allow content matching filter to pass, and gate content amount to the filter amount.", 0, ChatFormatting.GRAY),
	DISTRIBUTE("connect.distribute", "Connects to multiple targets. When receiving content, iterate through all targets until finding one valid target. Only send to 1 target, and start from the next one next time.", 0, ChatFormatting.GRAY),
	SYNCED("connect.synced", "Connects to multiple targets. When receiving content, attempt to send the same amount to all target, and otherwise send no content. Send 1 by default, and when applying filter, send the same amount as the filter amount.", 0, ChatFormatting.GRAY),
	ORDERED("connect.ordered", "Connects to multiple targets with different distance. Cannot connect to targets with the same distance. When receiving content, iterate through all targets in order of distance until all contents transferred.", 0, ChatFormatting.GRAY),
	RETRIEVE("connect.receive", "Connects to only one target. Actively extracts content from attached block. When applying filter, it will only extract the amount the filter specified. It also acts as a simple node.", 0, ChatFormatting.GRAY),
	INVALID("connect.invalid", "This node is connected to one or more invalid target. To remove unused connections, use validator wand to clear them.", 0, ChatFormatting.RED),

	EXTENDED("connect.extended", "Connects to only one target. This node acts as a shadow reference of the target's side, allowing all interfacing through that side of the target block.", 0, ChatFormatting.GRAY),
	ENDER_EXTEND("connect.ender_extended", "Connects to one remote extended block. This node acts as a shadow reference of that block.", 0, ChatFormatting.GRAY),
	ITEM_HOLDER("connect.item_holder", "Holds one stack of item. Does not accept new items when it already has items, regardless if they may merge.", 0, ChatFormatting.GRAY),
	CRAFT_SIDE("connect.craft_ingredient_holder", "Holds ingredient in crafting matrix. Insert filler if slot is unused.", 0, ChatFormatting.GRAY),
	CRAFT_CORE("connect.craft_result_holder", "Holds result in crafting matrix. Can only have one per matrix. Use Rotate Wand to adjust grid orientation.", 0, ChatFormatting.GRAY),

	UP_SPEED("upgrade.speed", "Transfer speed: x%s", 1, null),
	UP_DIST("upgrade.distance", "Connection distance: x%s", 1, null),
	UP_THROUGHPUT("upgrade.throughput", "Throughput: x%s", 1, null),
	UP_VALVE("upgrade.valve", "Disable when receiving redstone signal", 0, null),
	UP_WATCH("upgrade.watch", "Emit redstone signal when transfer succeed", 0, null),
	UP_ENTITY("upgrade.level", "Allow dropping contents in world", 0, null),
	UP_PLACE("upgrade.place", "Allow placing contents in world", 0, null),

	CONFIG_FILTER("config.filter", "Complex Filter", 0, null),
	CONFIG_TAG("config.tag", "Match tag", 0, ChatFormatting.LIGHT_PURPLE),
	CONFIG_LOCK("config.lock", "Filter Locked", 0, ChatFormatting.LIGHT_PURPLE),

	UI_UNLOCKED("screen.unlock", "Unlocked. Click to lock filter outside of GUI.", 0, null),
	UI_LOCKED("screen.lock", "Locked. Click to unlock.", 0, null),
	UI_WHITELIST("screen.whitelist", "Using whitelist. Click to switch to blacklist.", 0, null),
	UI_BLACKLIST("screen.blacklist", "Using blacklist. Click to switch to whitelist.", 0, null),
	UI_MATCH_ITEM("screen.match_item", "Match item only. Click to match NBT.", 0, null),
	UI_MATCH_TAG("screen.match_tag", "Match item and NBT. Click to match item only.", 0, null),

	WAND_LINK("wand.link", "Right click a node to start linking. Right click another block to link. Right click the node itself to halt. Right click an already linked node to unlink.", 0, ChatFormatting.GRAY),
	WAND_VALIDATE("wand.validate", "Right click a node to remove all invalid links.", 0, ChatFormatting.GRAY),
	WAND_CLEAR("wand.clear", "Right click a node to clear all links. Shift right click a node to clear all upgrades. Shift right clock a node with no upgrades to retrieve the node.", 0, ChatFormatting.GRAY),
	WAND_ROTATE("wand.rotate", "Right click a block to rotate. Shift right click to rotate in the other direction.", 0, ChatFormatting.GRAY),
	WAND_CONFIG("wand.config", "Press direction keys or scroll to alter transfer limit. Right click node to open advanced filter config menu. Press Shift or Alt to disable so that you can use the item selector overlay.", 0, ChatFormatting.GRAY),
	WAND_FLUX("wand.flux", "Select the flux type, and right click a node to set the flux type. When multiple nodes are connected, all connected nodes will be set to the same flux type altogether.", 0, ChatFormatting.GRAY),

	FLUX_ENERGY("flux.forge.energy", "Energy (FE)", 0, null),
	FLUX_GAS("flux.mekanism.gas", "Gas (Mekanism)", 0, null),
	FLUX_SOURCE("flux.ars_nouveau.source", "Source (Ars Nouveau)", 0, null),
	FLUX_MANA("flux.botania.mana", "Mana (Botania)", 0, null),
	;


	private final String id, def;
	private final int count;
	private final @Nullable ChatFormatting format;

	LangData(String id, String def, int count, @Nullable ChatFormatting format) {
		this.id = LaserTransport.MODID + "." + id;
		this.def = def;
		this.count = count;
		this.format = format;
	}

	public MutableComponent get(Object... args) {
		if (args.length != count)
			throw new IllegalArgumentException("for " + name() + ": expect " + count + " parameters, got " + args.length);
		var ans = Component.translatable(id, args);
		if (format != null) ans = ans.withStyle(format);
		return ans;
	}

	public MutableComponent getLiteral(Object args) {
		return get(Component.literal("" + args).withStyle(ChatFormatting.WHITE));
	}


	public static void addTranslations(BiConsumer<String, String> pvd) {
		for (LangData lang : values()) {
			pvd.accept(lang.id, lang.def);
		}
		pvd.accept("itemGroup.lasertransport.transport", "Laser Transport");
		pvd.accept("key.categories.lasertransport", "Laser Transport Keys");
		pvd.accept(Keys.UP.id, "Select Up");
		pvd.accept(Keys.DOWN.id, "Select Down");
		pvd.accept(Keys.LEFT.id, "Select Left");
		pvd.accept(Keys.RIGHT.id, "Select Right");

	}

}
