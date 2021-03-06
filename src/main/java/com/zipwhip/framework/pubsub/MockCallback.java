/**
 *
 */
package com.zipwhip.framework.pubsub;


/**
 * @author jdinsel
 */
public class MockCallback implements Callback {

    boolean notified = false;
    String uri = null;
    EventData eventData;
    boolean clone = true;

    /*
      * (non-Javadoc)
      *
      * @see com.zipwhip.framework.pubsub.Callback#notify(java.lang.String, com.zipwhip.framework.pubsub.EventData)
      */
    @Override
    public void notify(String uri, EventData eventData) throws Exception {
        this.eventData = clone ? new EventData(eventData.getExtras()) : eventData;
        this.notified = true;
        this.uri = uri;
    }

    public void reset() {
        this.eventData = null;
        this.notified = false;
        this.uri = null;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

    public boolean isClone() {
        return clone;
    }

    public void setClone(boolean clone) {
        this.clone = clone;
    }
}
