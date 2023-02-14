package dev.xkmc.lasertransport.content.capability.generic;

public interface HandlerWrapper {

	long insert(GenericHolder token, boolean simulate);

	GenericHolder extract(int slot, long max, boolean b);

	int getSize();

}
