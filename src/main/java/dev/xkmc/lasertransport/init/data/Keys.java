package dev.xkmc.lasertransport.init.data;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public enum Keys {
	UP("key.lasertransport.up", GLFW.GLFW_KEY_UP),
	DOWN("key.lasertransport.down", GLFW.GLFW_KEY_DOWN),
	LEFT("key.lasertransport.left", GLFW.GLFW_KEY_LEFT),
	RIGHT("key.lasertransport.right", GLFW.GLFW_KEY_RIGHT);

	public final String id;
	public final int key;
	public final KeyMapping map;

	Keys(String id, int key) {
		this.id = id;
		this.key = key;
		this.map = new KeyMapping(id, key, "key.categories.lasertransport");
	}
}
