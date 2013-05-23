/**
 *
 */
package com.zipwhip.framework.pubsub;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock Broker -- does not throw NPE and keeps track of data that was added to the queues
 *
 * @author jdinsel
 */
public class MockBroker implements Broker {

    Map<String, List<Object>> queue = new LinkedHashMap<String, List<Object>>();

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isDestroyed() {
        // TODO Auto-generated method stub
        return false;
    }

    public Map<String, List<Object>> getQueues() {
        return queue;
    }

    public List<Object> getQueue(String uri) {
        return queue.get(uri);
    }

    @Override
    public void publish(String uri, Object... args) {
        List<Object> list = queue.get(uri);
        if (list == null) {
            subscribe(uri, null);
            list = queue.get(uri);
        }
        if (args != null) {
            for (Object o : args) {
                list.add(o);
            }
        }
    }

    @Override
    public void subscribe(String uri, Callback callback) {
        if (queue.containsKey(uri)) {
            return;
        } else {
            List<Object> list = new ArrayList<Object>();
            queue.put(uri, list);
        }

    }

    @Override
    public void unsubscribe(String uri, Callback callback) {
        // TODO Auto-generated method stub
    }

}
