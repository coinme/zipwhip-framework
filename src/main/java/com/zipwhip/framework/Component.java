package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.*;
import com.zipwhip.util.ListDirectory;
import com.zipwhip.util.LocalDirectory;
import com.zipwhip.util.Validatable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 1/29/11
 * Time: 12:43 AM
 * <p/>
 * A nice default implementation for use in an app. These are supposed to be "big things"
 */
public abstract class Component<T extends BrokerPlugin<?>> extends PluginBase<T> {

    private LocalDirectory<String, Callback> subscriptions;

    @Override
    protected void onInit() {
        // since we have the Subscribe annotation now, i'm going to let this component override the onInit method.
    }

    protected void publish(UriAgent agent, Object... params) {
        parent.getBroker().publish(agent.getUri(), params);
    }

    protected void publish(String uri, EventData eventData) {
        if (parent.getBroker() instanceof MemoryBroker) {
            ((MemoryBroker) parent.getBroker()).publish(uri, eventData);
        } else {
            throw new RuntimeException("Can only be used with a MemoryBroker");
        }
    }

    protected void publish(String uri, Object... args) {
        parent.getBroker().publish(uri, args);
    }

    protected void subscribe(String uri, Callback callback) {
        if (subscriptions == null) {
            subscriptions = new ListDirectory<String, Callback>();
        }
        subscriptions.add(uri, callback);

        parent.getBroker().subscribe(uri, callback);
    }

    protected void subscribe(UriAgent uri, Callback callback) {
        subscribe(uri.getUri(), callback);
    }

    protected void publish(String uri, Callback callback) {
        Broker b = parent.getBroker();
        if (!(b instanceof MemoryBroker)) {
            throw new RuntimeException("Can't do this on a non-memory broker");
        }
        ((MemoryBroker) b).publish(uri, callback);
    }

    protected <T extends Validatable> void subscribe(UriAgent agent, DataCallback<T> callback, EventDataConverter<T> converter) {
        subscribe(agent.getUri(), callback, converter);
    }

    protected <T extends Validatable> void subscribe(String uri, DataCallback<T> callback, EventDataConverter<T> converter) {

        Callback c = new ConvertingCallbackAdapter<T>(
                new ValidatingEventDataConverterAdapter<T>(converter),
                callback);

        subscribe(uri, c);
    }


    @Override
    public void destroy() {
        super.destroy();

        // Return if there are no subscriptions
        if (subscriptions == null || subscriptions.isEmpty()) {
            return;
        }

        for (String uri : subscriptions.keySet()) {
            for (Callback callback : subscriptions.get(uri)) {
                parent.getBroker().unsubscribe(uri, callback);
            }
        }
    }

    protected void onDestroy() {

    }

}
