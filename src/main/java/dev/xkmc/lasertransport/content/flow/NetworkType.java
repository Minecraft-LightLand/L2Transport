package dev.xkmc.lasertransport.content.flow;

public interface NetworkType {

	boolean testConsumption(long avail, long c);

	boolean shouldContinue(long available, long consumed, long size);

	long provide(long available, long consumed, long size);

}
