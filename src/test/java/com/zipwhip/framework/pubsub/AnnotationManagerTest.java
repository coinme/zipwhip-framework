package com.zipwhip.framework.pubsub;

import com.zipwhip.executors.SimpleExecutor;
import com.zipwhip.framework.Application;
import com.zipwhip.framework.Feature;
import com.zipwhip.util.Converter;
import com.zipwhip.util.DataConversionException;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.*;

/**
 * Date: 3/28/13
 * Time: 10:17 AM
 *
 * @author Michael
 * @version 1
 */
public class AnnotationManagerTest {

    Application application;


    @Test
    public void testNoAnnotationPresentObject() throws Exception {
        AnnotationManager.attach(new MockBroker(), new Object());
    }

    @Test
    public void testNoAnnotationPresent() throws Exception {
        AnnotationManager.attach(new MockBroker(), new Feature(){
            @Override
            protected void onInit() {

            }
        });
    }

    @Test
    public void testApplicationCase() throws Exception {
        MockFeature feature = new MockFeature();

        application = new Application();
        application.setBroker(new MemoryBroker(new SimpleExecutor()));
        application.addPlugin(feature);
        application.init(null);

        assertFalse(feature.isReceived());
        assertEquals(1, feature.getCountDownLatch().getCount());

        application.getBroker().publish("/message/received");

        assertTrue(feature.getCountDownLatch().await(5, TimeUnit.SECONDS));
        assertEquals(0, feature.getCountDownLatch().getCount());
        assertTrue("Should have been received", feature.isReceived());
    }

    @Test
    public void testEventStatus() throws Exception {
        AnnotationManager.register("string", new Converter<EventData, Object>() {
            @Override
            public Object convert(EventData eventData) throws DataConversionException {
                return EventDataUtil.getString(eventData);
            }
        });

        EventStatusMockFeature feature1 = new EventStatusMockFeature();

        application = new Application();
        application.setBroker(new MemoryBroker(new SimpleExecutor()));
        application.addPlugin(feature1);
        application.init(null);

        assertFalse(feature1.isReceived());
        assertEquals(1, feature1.getCountDownLatch().getCount());

        application.getBroker().publish("/message/received", "test");

        assertTrue(feature1.getCountDownLatch().await(5, TimeUnit.SECONDS));
        assertEquals(0, feature1.getCountDownLatch().getCount());
        assertTrue("Should have been received", feature1.isReceived());
        assertEquals("test", feature1.getStatus().getData());
    }

    private static class MockFeature extends Feature {

        private CountDownLatch latch = new CountDownLatch(1);
        private boolean received;

        @Override
        protected void onInit() {

        }

        @Subscribe(uri = "/message/received")
        public void onMessageReceived(String uri, EventData eventData){
            received = true;
            latch.countDown();
        }

        public CountDownLatch getCountDownLatch() {
            return latch;
        }

        public boolean isReceived() {
            return received;
        }
    }

    private static class EventStatusMockFeature extends Feature {

        private CountDownLatch latch = new CountDownLatch(1);
        private boolean received;
        private EventStatus<String> status;

        @Override
        protected void onInit() {

        }

        @Subscribe(uri = "/message/received", converter = "string")
        public void onMessageReceived(EventStatus<String> status){
            this.status = status;
            this.received = true;
            this.latch.countDown();
        }

        public EventStatus<String> getStatus() {
            return status;
        }

        public CountDownLatch getCountDownLatch() {
            return latch;
        }

        public boolean isReceived() {
            return received;
        }
    }

}
