package dev.xkmc.lasertransport.content.capability.item;

import dev.xkmc.lasertransport.content.capability.base.INodeBlockEntity;
import dev.xkmc.lasertransport.content.configurables.ItemConfigurable;

public interface IItemNodeBlockEntity extends INodeBlockEntity {

	ItemConfigurable getConfig();

}
