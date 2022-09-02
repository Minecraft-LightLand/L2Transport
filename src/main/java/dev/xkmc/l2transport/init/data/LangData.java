package dev.xkmc.l2transport.init.data;

import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public enum LangData {
	INFO_FILTER("info.filter", "Filter: %s", 1, ChatFormatting.AQUA),
	INFO_GATED("info.gated", "Transfer gated to %s", 1, ChatFormatting.AQUA),
	INFO_EXTRACT("info.extract", "Extract in %s", 1, ChatFormatting.AQUA),
	INFO_SPEED("info.speed", "Transfer cool down: %s", 1, ChatFormatting.AQUA),
	INFO_SYNC("info.sync", "Force transfer %s to all targets", 1, ChatFormatting.AQUA),

	SIMPLE("connect.simple", "Connects to only one target. When adding filter, only allow content matching filter to pass, and gate content amount to the filter amount.", 0, ChatFormatting.GRAY),
	DISTRIBUTE("connect.distribute", "Connects to multiple targets. When receiving content, iterate through all targets until finding one valid target. Only send to 1 target, and start from the next one next time.", 0, ChatFormatting.GRAY),
	SYNCED("connect.synced", "Connects to multiple targets. When receiving content, attempt to send the same amount to all target, and otherwise send no content. Send 1 by default, and when applying filter, send the same amount as the filter amount.", 0, ChatFormatting.GRAY),
	ORDERED("connect.ordered", "Connects to multiple targets with different distance. Cannot connect to targets with the same distance. When receiving content, iterate through all targets in order of distance until all contents transferred.", 0, ChatFormatting.GRAY),
	RETRIEVE("connect.receive", "Connects to only one target. Actively extracts content from attached block. When applying filter, it will only extract the amount the filter specified. It also acts as a simple node.", 0, ChatFormatting.GRAY);

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
	}

}
