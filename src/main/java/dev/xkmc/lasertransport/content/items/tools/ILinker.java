package dev.xkmc.lasertransport.content.items.tools;

public interface ILinker {

	boolean storesPos();

	default boolean toolSelect() {
		return true;
	}
}
