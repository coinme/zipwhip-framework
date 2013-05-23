package com.zipwhip.framework;

import com.zipwhip.lifecycle.CascadingDestroyableBase;
import com.zipwhip.util.CollectionUtil;
import com.zipwhip.util.HashCodeComparator;
import com.zipwhip.util.InputRunnable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;


/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/17/10
 * Time: 12:07 AM
 */
public abstract class PluginBase<T extends Plugin<?>> extends CascadingDestroyableBase implements Plugin<T> {

	private boolean initialized = false;
	protected boolean clearPluginsOnInitForEfficiency = true;
	protected boolean keepTrackOfChildren = true;
	public T parent;

	// this is only used for initialization, it is quickly destroyed.
	private static final Comparator<Plugin> COMPARATOR = new HashCodeComparator<Plugin>();
	protected Collection<Plugin> plugins = null;

	@Override
	public void init(T parent) {
		if (this.initialized) {
			throw new RuntimeException("Already initialized");
		}

		this.parent = parent;

		// initialize ourselves.
		this.onInit();

		// init the children if we have any.
		if (plugins != null) {
			//            List<Plugin> p = plugins.subList(0, plugins.size());
			// int size = plugins.size();
			synchronized (plugins) {

				for (Plugin plugin : plugins) {
					plugin.init(this);
				}
			}
			// we dont need to keep track of this. lets kill it for memory efficiency.
			// android hates memory users. just remember that.
			if (clearPluginsOnInitForEfficiency) {
				plugins.clear();
				plugins = null;
			}
		}

		this.initialized = true;

	}

	protected synchronized void createPluginsList()
	{
		plugins = Collections.synchronizedCollection(new TreeSet<Plugin>(COMPARATOR));
	}

	protected void runOnPlugins(InputRunnable<Plugin> runnable) {
		if (CollectionUtil.isNullOrEmpty(plugins)) {
			return;
		}

		for (Plugin plugin : plugins) {
			runnable.run(plugin);
		}
	}

	protected abstract void onInit();

	//    public void addPlugin(Plugin plugin){
	//        link(plugin);
	//
	//        if (!this.initialized){
	//            this.plugins = (List<Plugin>) CollectionUtil.add(this.plugins, plugin);
	//            return;
	//        }
	//
	//        plugin.init(this);
	//    }

	@Override
	public void addPlugin(Plugin plugin) {
		link(plugin);
		if (keepTrackOfChildren) {

			if (plugins == null) {
				createPluginsList();
			}

			this.plugins.add(plugin);
		}

		if (this.initialized) {
			plugin.init(this);
		}
	}

	@Override
	public void removePlugin(Plugin plugin)
	{
		unlink(plugin);
		if (keepTrackOfChildren && (plugins != null)) {
			plugins.remove(plugin);
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		if (!CollectionUtil.isNullOrEmpty(plugins)) {
			plugins.clear();
			plugins = null;
		}
	}
}
