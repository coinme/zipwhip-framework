package com.zipwhip.framework.pubsub;

import com.zipwhip.lifecycle.DestroyableBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for debugging and jUnit tests.  Encapsulates a broker, but keeps track of all messages that have been published,
 * to allow a test script to see the output after sending a message.
 */
public class DebugBroker extends DestroyableBase implements Broker {

    private List<BrokerMessage> messageCache;

    private Broker broker;
    public DebugBroker(Broker broker){
        this.messageCache = new ArrayList<BrokerMessage>();
        this.broker = broker;
    }

    @Override
    public void publish(String uri, Object... args) {
        messageCache.add(new BrokerMessage(uri, args));
        broker.publish(uri, args);
    }

    @Override
    public void subscribe(String uri, Callback callback) {
        broker.subscribe(uri, callback);
    }

    @Override
    public void unsubscribe(String uri, Callback callback) {
        broker.unsubscribe(uri, callback);
    }

    @Override
    protected void onDestroy() {
        broker.destroy();
    }

    public void clearMessageCache(){
        messageCache.clear();
    }

    public BrokerMessage[] getMessages(){
        return messageCache.toArray(new BrokerMessage[0]);
    }

    public BrokerMessage[] getMessages(String uri){
        List<BrokerMessage> retVal = new ArrayList<BrokerMessage>();
        for (BrokerMessage toCheck : this.messageCache){
            if (toCheck.uri == null) continue;
            if (toCheck.uri.equals(uri)){
                retVal.add(toCheck);
            } else if (uri.startsWith(toCheck.uri + "/")){
                retVal.add(toCheck);
            }
        }


        return retVal.toArray(new BrokerMessage[0]);
    }



    public class BrokerMessage {
        private String uri;
        private Object[] args;

        private BrokerMessage(String uri, Object[] args){
            this.uri = uri;
            this.args = args;
        }

        public String getUri(){
            return this.uri;
        }

        public Object[] getArgs(){
            return this.args;
        }
    }
}
