package com.zipwhip.framework.pubsub;

import java.util.concurrent.CountDownLatch;

/**
 * Date: 5/14/13
 * Time: 6:15 PM
 *
 * @author Michael
 * @version 1
 */
public class TestCallback implements Callback {

    private String lastUri;
    private Object[] lastItem;
    private boolean called;
    private int hitCount;
    private CountDownLatch latch;

    public TestCallback() {
        this(1);
    }

    public TestCallback(int count) {
        this.latch = new CountDownLatch(count);
    }

    @Override
    public void notify(String uri, EventData eventData) throws Exception {
        this.lastUri = uri;
        this.lastItem = eventData.getExtras();
        this.called = true;
        this.hitCount++;
        this.latch.countDown();
    }

    public String getLastUri() {
        return lastUri;
    }

    public void setLastUri(String lastUri) {
        this.lastUri = lastUri;
    }

    public Object[] getLastItem() {
        return lastItem;
    }

    public void setLastItem(Object[] lastItem) {
        this.lastItem = lastItem;
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}
