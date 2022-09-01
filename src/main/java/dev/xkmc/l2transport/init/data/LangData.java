package dev.xkmc.l2transport.init.data;

import java.util.function.BiConsumer;

public class LangData {

	public static void addTranslations(BiConsumer<String, String> pvd) {
		pvd.accept("itemGroup.l2transport.transport", "L2 Transport");
	}

}
