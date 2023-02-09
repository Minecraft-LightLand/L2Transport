package dev.xkmc.l2transport.content.client.node;

public enum NodeRenderType {
	ALL(true, true),
	FILTER(false, true),
	NONE(false, false);

	private final boolean renderLinks;
	private final boolean renderEnable;

	NodeRenderType(boolean renderLinks, boolean renderEnable) {
		this.renderLinks = renderLinks;
		this.renderEnable = renderEnable;
	}

	public boolean renderLinks() {
		return renderLinks;
	}

	public boolean renderFilters() {
		return renderEnable;
	}

	public boolean renderEnable() {
		return renderEnable;
	}

}
