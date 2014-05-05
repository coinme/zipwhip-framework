package com.zipwhip.framework.pubsub;

/**
 * @author Michael
 * @date 5/5/2014
 */
public class EventStatus<T> {

    private EventData eventData;
    private String uri;
    private Broker broker;
    private UriAgent agent;
    private T data;

    public void succeed(Object... extras) {
        EventDataUtil.succeed(broker, uri, eventData, agent, extras);
    }

    public void fail(Throwable throwable) {
        EventDataUtil.fail(broker, uri, eventData, agent, throwable);
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public UriAgent getAgent() {
        return agent;
    }

    public void setAgent(UriAgent agent) {
        this.agent = agent;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
