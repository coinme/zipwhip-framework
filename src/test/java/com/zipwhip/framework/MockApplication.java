/**
 *
 */
package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.Broker;
import com.zipwhip.framework.pubsub.MockBroker;

/**
 * @author jdinsel
 */
public class MockApplication extends Application {

    @Override
    public Broker getBroker() {
        return new MockBroker();
    }
}
