package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.Broker;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 5/5/11
 * Time: 6:35 PM
 */
public class BrokerPlugin<T extends Plugin<? extends Plugin>> extends PluginBase<T> {

    private Broker broker;

    public BrokerPlugin() {

    }

    public BrokerPlugin(Broker broker) {
        this.broker = broker;
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onDestroy() {
        broker.destroy();
        broker = null;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

}
