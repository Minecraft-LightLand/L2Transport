package dev.xkmc.lasertransport.content.craft;

public interface IItemHolderNode extends DelaySyncBlockEntity {

	ItemInventory getHolder();

	boolean canInsert();

}
