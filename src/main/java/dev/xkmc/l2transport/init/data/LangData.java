package dev.xkmc.l2transport.init.data;

import dev.xkmc.l2transport.init.L2Transport;
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

	SIMPLE("connect.simple", "Connects to only one target. When adding filter, only allow content matching filter to pass, and gate content amount to the filter amount.", 0, ChatFormatting.GRAY),
	DISTRIBUTE("connect.distribute", "Connects to multiple targets. When receiving content, iterate through all targets until finding one valid target. Only send to 1 target, and start from the next one next time.", 0, ChatFormatting.GRAY),
	SYNCED("connect.synced", "Connects to multiple targets. When receiving content, attempt to send the same amount to all target, and otherwise send no content. Send 1 by default, and when applying filter, send the same amount as the filter amount.", 0, ChatFormatting.GRAY),
	ORDERED("connect.ordered", "Connects to multiple targets with different distance. Cannot connect to targets with the same distance. When receiving content, iterate through all targets in order of distance until all contents transferred.", 0, ChatFormatting.GRAY),
	RETRIEVE("connect.receive", "Connects to only one target. Actively extracts content from attached block. When applying filter, it will only extract the amount the filter specified. It also acts as a simple node.", 0, ChatFormatting.GRAY),
	INVALID("connect.invalid", "This node is connected to one or more invalid target. To remove unused connections, use validator wand to clear them.", 0, ChatFormatting.RED),

	EXTENDED("connect.extended", "Connects to only one target. This node acts as a shadow reference of the target's side, allowing all interfacing through that side of the target block.", 0, ChatFormatting.GRAY),

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

	UI_LOCK("screen.lock", "Lock", 0, null),
	UI_WHITELIST("screen.whitelist", "Whitelist", 0, null),
	UI_BLACKLIST("screen.blacklist", "Blacklist", 0, null),
	UI_MATCH_TAG("screen.match_tag", "Match NBT", 0, null),
	;


	private final String id, def;
	private final int count;
	private final @Nullable ChatFormatting format;

	LangData(String id, String def, int count, @Nullable ChatFormatting format) {
		this.id = L2Transport.MODID + "." + id;
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
		pvd.accept("itemGroup.l2transport.transport", "L2 Transport");
		pvd.accept("key.categories.l2transport", "L2Transport Keys");
		pvd.accept(Keys.UP.id, "Select Up");
		pvd.accept(Keys.DOWN.id, "Select Down");
		pvd.accept(Keys.LEFT.id, "Select Left");
		pvd.accept(Keys.RIGHT.id, "Select Right");
	}

}
