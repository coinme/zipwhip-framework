/**
 * 
 */
package com.zipwhip.framework.pubsub;

import com.zipwhip.executors.SimpleExecutor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author jdinsel
 *
 */
public class MemoryBrokerTest {

    MockCallback callback = new MockCallback();
    MemoryBroker broker = new MemoryBroker(new SimpleExecutor());

    @Before
    public void setUp() throws Exception {
        callback.setClone(false);
        broker.setPooling(false);
    }

    @Test
    public void testPublish() throws Exception {
        assertFalse(callback.isNotified());

        broker.subscribe("/asdf", callback);

        broker.publish("/asdf", (Object)null);

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

    /**
     * We need to test borrowing
     *
     * @throws Exception
     */
    @Test
    public void testBorrowing() throws Exception {
        assertFalse(callback.isNotified());

        broker.subscribe("/asdf", callback);

        broker.publish("/asdf", "a");

        assertTrue(callback.isNotified());
        assertNotNull(callback.getEventData());
        assertEquals("a", EventDataUtil.getString(callback.getEventData()));
    }

    /**
     * We need to test borrowing
     *
     * @throws Exception
     */
    @Test
    public void testNotBorrowing() throws Exception {
        assertFalse(callback.isNotified());

        broker.setPooling(true);
        broker.subscribe("/asdf", callback);

        broker.publish("/asdf", "a");

        assertTrue(callback.isNotified());
        assertNotNull(callback.getEventData());
        assertNull(EventDataUtil.getExtra(callback.getEventData()));
    }
}
