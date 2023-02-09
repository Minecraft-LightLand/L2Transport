package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.configurables.ItemConfigurable;

public interface IItemNodeBlockEntity extends INodeBlockEntity {

	ItemConfigurable getConfig();

}
