package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.AnnotationManager;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 2/8/11
 * Time: 4:10 PM
 * <p/>
 * You should extend this and put your special globals here.
 */
public class Application<TConfiguration extends Configuration> extends BrokerPlugin<NullPlugin> {

    public TConfiguration configuration;

    public Application(TConfiguration configuration) {
        this.configuration = configuration;
    }

    public Application() {

    }

    @Override
    protected void onInit() {

    }

    @Override
    public void addPlugin(Plugin plugin) {
        super.addPlugin(plugin);

        if (this.getBroker() == null || plugin == null) {
            return;
        }

        AnnotationManager.attach(this.getBroker(), plugin);
    }

    public TConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(TConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void onDestroy() {
        getBroker().destroy();
    }

}
