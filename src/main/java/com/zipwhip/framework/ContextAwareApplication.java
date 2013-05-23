package com.zipwhip.framework;

import com.zipwhip.util.Factory;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/12/11
 * Time: 2:40 PM
 */
public class ContextAwareApplication<TConfiguration extends Configuration, TContext extends Context> extends Application<TConfiguration> {

    private TContext context;
    private Factory<TContext>  contextFactory;

    public ContextAwareApplication() {

    }

    public TContext getContext() {
        if (context == null) {
            try {
                reloadContext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return context;
    }

    public void setContext(TContext context) {
        if (this.context != null) {
            this.context.destroy();
        }
        this.context = context;
    }

    public void reloadContext() throws Exception {
        this.setContext(contextFactory.create());
    }

    public void unloadContext() throws Exception {
        this.context.destroy();
        this.context = null;
    }

    public Factory<TContext> getContextFactory() {
        return contextFactory;
    }

    public void setContextFactory(Factory<TContext> contextFactory) {
        this.contextFactory = contextFactory;
    }

}
