/**
 * 
 */
package com.zipwhip.framework.pubsub;

import com.zipwhip.executors.SimpleExecutor;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author jdinsel
 *
 */
public class MemoryBrokerTest {

    MockCallback callback = new MockCallback();
    Broker broker = new MemoryBroker(new SimpleExecutor());

    @Test
    public void testPublish() throws Exception {

        assertFalse(callback.isNotified());

        broker.subscribe("/asdf", callback);

        broker.publish("/asdf", null);

        assertTrue(callback.isNotified());

    }

    /**
	 * Test method for {@link MemoryBroker#publish(String, EventData)}.
	 */
	@Test
	public void testPublishStringEventData() {
		MemoryBroker memoryBroker = new MemoryBroker();
		EventData args = new EventData();
		String _uri = MemoryBrokerTest.class.getName();

		// if the Thread pool is empty, this will fail
		memoryBroker.publish(_uri, args);
	}

}
