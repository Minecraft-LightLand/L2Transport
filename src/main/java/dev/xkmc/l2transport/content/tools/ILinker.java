package dev.xkmc.l2transport.content.tools;

public interface ILinker {

	boolean storesPos();

	default boolean toolSelect(){
		return true;
	}
}
