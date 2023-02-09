package dev.xkmc.l2transport.content.items.tools;

public interface ILinker {

	boolean storesPos();

	default boolean toolSelect(){
		return true;
	}
}
