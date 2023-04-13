package dev.xkmc.lasertransport.compat;

import dev.xkmc.l2library.repack.registrate.providers.RegistrateLangProvider;
import dev.xkmc.lasertransport.init.LaserTransport;

public enum PatchouliLang {
	TITLE("title", "Laser Transport Guide"),
	LANDING("landing", "Welcome to Laser Transport, an efficient, low lag, pipe-less transportation mod that supports items, fluids, energy, and various mod resources in well known mods.");

	private final String key, def;

	PatchouliLang(String key, String def) {
		this.key = "patchouli." + LaserTransport.MODID + "." + key;
		this.def = def;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (PatchouliLang lang : PatchouliLang.values()) {
			pvd.add(lang.key, lang.def);
		}
	}

}
