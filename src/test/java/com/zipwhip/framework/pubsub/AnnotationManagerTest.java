package com.zipwhip.framework.pubsub;

import com.zipwhip.executors.SimpleExecutor;
import com.zipwhip.framework.Application;
import com.zipwhip.framework.Feature;
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

    MockFeature feature = new MockFeature();

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

}
