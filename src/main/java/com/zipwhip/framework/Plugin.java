package com.zipwhip.framework;

import com.zipwhip.lifecycle.CascadingDestroyable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/17/10
 * Time: 12:06 AM
 *
 * A plugin can support children plugin.
 * A plugin does not know about its children directly (aside from the fact that it has some).
 * A plugin knows about its parent.
 * A plugin provides some functionality, generally through its parent.
 *
 * This is how we can divide up units of functionality. Generally Plugins are used within the context of PubSub. Plugins
 * generally react to actions of their parents. Through reflection, they know exactly who they are plugged into.
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
