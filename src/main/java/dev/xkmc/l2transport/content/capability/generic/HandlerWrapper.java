package dev.xkmc.l2transport.content.capability.generic;

public interface HandlerWrapper {

	int insert(GenericHolder token, boolean simulate);

	GenericHolder extract(int slot, int max, boolean b);

	int getSize();

}
