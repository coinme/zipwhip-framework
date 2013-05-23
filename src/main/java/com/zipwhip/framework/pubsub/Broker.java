package com.zipwhip.framework.pubsub;

import com.zipwhip.lifecycle.Destroyable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/16/10
 * Time: 8:09 PM
 * <p/>
 * A broker enables communication between objects.
 */
public interface Broker extends Destroyable {

    /**
     * Publish with only args (no callback)
     * <p/>
     * The Subscribers would need to interpret the args appropriately.
     *
     * @param uri
     * @param args
     */
    void publish(String uri, Object... args);

    /**
     * Subscribe, so when the URI is hit, your callback is hit.
     *
     * @param uri
     * @param callback
     */
    void subscribe(String uri, Callback callback);

    /**
     * Remove your subscription
     *
     * @param uri
     * @param callback
     */
    void unsubscribe(String uri, Callback callback);

}
