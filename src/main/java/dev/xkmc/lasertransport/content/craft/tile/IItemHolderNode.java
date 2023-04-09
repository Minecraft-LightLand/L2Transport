package dev.xkmc.lasertransport.content.craft.tile;

public interface IItemHolderNode extends DelaySyncBlockEntity {

	ItemInventory getHolder();

	boolean canInsert();

}
