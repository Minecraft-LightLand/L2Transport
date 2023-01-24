package dev.xkmc.l2transport.content.flow;

/**
 * Token of content,
 */
public interface IContentToken<T> {

	/**
	 * get the immutable content representing the original stack
	 */
	IContentHolder<T> get();

	/**
	 * get the remaining available content
	 */
	long getAvailable();

	/**
	 * deduct content from available. Can deduct more than available
	 */
	void consume(long count);

	/**
	 * get the remaining content
	 */
	default T getRemain() {
		return get().getCopy(getAvailable());
	}

}
