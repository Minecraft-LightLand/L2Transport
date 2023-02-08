package dev.xkmc.l2transport.init.data;

import net.minecraft.client.KeyMapping;

public enum Keys {
    UP("key.l2backpack.up", 265),
    DOWN("key.l2backpack.down", 264);

    public final String id;
    public final int key;
    public final KeyMapping map;

    Keys(String id, int key) {
        this.id = id;
        this.key = key;
        this.map = new KeyMapping(id, key, "key.categories.l2transport");
    }
}
