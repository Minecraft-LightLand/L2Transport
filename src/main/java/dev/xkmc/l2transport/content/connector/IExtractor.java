package dev.xkmc.l2transport.content.connector;

public interface IExtractor {

	void performExtract(boolean success);

	boolean mayExtract();

}
