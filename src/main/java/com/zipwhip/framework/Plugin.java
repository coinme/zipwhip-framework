package com.zipwhip.framework;

import com.zipwhip.lifecycle.CascadingDestroyable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/17/10
 * Time: 12:06 AM
 */
public interface Plugin<T extends Plugin<?>> extends CascadingDestroyable {

	public void init(T parent);

	/**
	 * When you add a child plugin, it also does a link (destroyable).
	 *
	 * @param plugin
	 * @return the same thing you passed in.
	 */
	public void addPlugin(Plugin<? extends Plugin<?>> plugin);

	public void removePlugin(Plugin<? extends Plugin<?>> plugin);

}
