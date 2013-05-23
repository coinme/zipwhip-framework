package com.zipwhip.framework;

import com.zipwhip.lifecycle.Destroyable;
import com.zipwhip.util.CollectionUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 1/27/11
 * Time: 7:15 PM
 * <p/>
 * Just a simple host, to hold plugins. This is because Activities cannot extend from Plugin.
 */
public class PluginHost<T, TConfig extends Configuration>
        extends PluginBase<NullPlugin>
        implements ConfigurationAware<TConfig> {

    public T context = null;
    public TConfig config;

    public PluginHost(T context, TConfig config) {
        super.clearPluginsOnInitForEfficiency = false;
        this.context = context;
        this.config = config;
    }

    @Override
    public void init(NullPlugin parent) {
        super.init(parent);

        load(config);
    }

    @Override
    protected void onInit() {

    }

    public void load(TConfig data) {
        if (CollectionUtil.isNullOrEmpty(destroyables) || data == null) {
            return;
        }

        // we know that destroyables are kept around. I hope this is a safe assumption.
        for (Destroyable destroyable : destroyables) {
            if (destroyable instanceof ConfigurationAware) {
                ConfigurationAware<TConfig> configurable = (ConfigurationAware<TConfig>) destroyable;

                configurable.load(data);
            }
        }
    }

    @Override
    protected void onDestroy() {

    }

}
