/**
 *
 */
package com.zipwhip.framework.pubsub;

import com.zipwhip.util.ListDirectory;
import com.zipwhip.util.LocalDirectory;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Mock Broker -- does not throw NPE and keeps track of data that was added to the queues
 *
 * @author jdinsel
 */
public class MockBroker extends MemoryBroker {

    LocalDirectory<String, Object[]> queue = new ListDirectory<String, Object[]>();

    private boolean interceptPublishes = false;

    public MockBroker() {
    }

    public MockBroker(boolean interceptPublishes) {
        this.interceptPublishes = interceptPublishes;
    }

    public MockBroker(Executor executor, boolean interceptPublishes) {
        super(executor);
        this.interceptPublishes = interceptPublishes;
    }

    @Override
    public void publish(String uri, Object... args) {
        queue.add(uri, args);

        // publish to the actual callbacks.
        super.publish(uri, args);
    }

    public LocalDirectory<String, Callback> getDirectory() {
        return super.getDirectory();
    }

    public LocalDirectory<String, Object[]> getQueues() {
        return queue;
    }

    public List<Object[]> getQueue(String uri) {
        return (List<Object[]>) queue.get(uri);
    }

    /**
     * This method belongs at the bottom of the file.
     */
    @Override
    protected void onDestroy() {
        queue.clear();
    }
}
