package dev.xkmc.l2transport.init.data;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public enum Keys {
	UP("key.l2transport.up", GLFW.GLFW_KEY_UP),
	DOWN("key.l2transport.down", GLFW.GLFW_KEY_DOWN),
	LEFT("key.l2transport.left", GLFW.GLFW_KEY_LEFT),
	RIGHT("key.l2transport.right", GLFW.GLFW_KEY_RIGHT);

	public final String id;
	public final int key;
	public final KeyMapping map;

	Keys(String id, int key) {
		this.id = id;
		this.key = key;
		this.map = new KeyMapping(id, key, "key.categories.l2transport");
	}
}
