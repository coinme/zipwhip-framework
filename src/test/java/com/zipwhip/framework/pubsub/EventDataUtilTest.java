/**
 *
 */
package com.zipwhip.framework.pubsub;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author jdinsel
 */
public class EventDataUtilTest {

    @Test
    public void testToString() {
        Object[] extras = new Object[]{"Hello", "world"};

        EventData eventData = new EventData(extras);
        String string = eventData.toString();
        assertEquals("Hello", string);

        eventData = new EventData();
        assertEquals("No data in the event.", eventData.toString());
    }
}
