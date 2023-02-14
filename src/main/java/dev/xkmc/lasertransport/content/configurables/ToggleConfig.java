package dev.xkmc.lasertransport.content.configurables;

public record ToggleConfig(CommonConfigurable<?> config) {

	public boolean isBlacklist() {
		return !config.whitelist;
	}

	public void toggleBlacklist() {
		config.whitelist = !config.whitelist;
	}

	public boolean isTagMatch() {
		return config.match_tag;
	}

	public void toggleTagMatch() {
		config.match_tag = !config.match_tag;
	}

	public boolean isLocked() {
		return config.locked;
	}

	public void toggleLocked() {
		config.locked = !config.locked;
	}

}
