package com.zipwhip.framework.pubsub;

import com.zipwhip.util.CollectionUtil;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/18/10
 * Time: 3:57 AM
 * <p/>
 * Wraps a callback into a group.
 */
public class CallbackGroup implements Callback {

    List<Callback> callbackList;

    public CallbackGroup() {
    }

    public CallbackGroup(List<Callback> callbackList) {
        this();
        this.callbackList = callbackList;
    }

    public CallbackGroup(Callback... callbacks) {
        this(CollectionUtil.asList(callbacks));
    }

    @Override
    public void notify(String uri, EventData eventData) {
        if (CollectionUtil.isNullOrEmpty(callbackList)) {
            return;
        }

        for (Callback callback : callbackList) {
            if (callback == null) {
                continue;
            }
            try {
                callback.notify(uri, eventData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Callback callback) {
        callbackList = (List<Callback>) CollectionUtil.add(callbackList, callback);
    }

    public void remove(Callback callback) {
        callbackList = CollectionUtil.remove(callbackList, callback);
    }

    public static Callback add(Callback success, Callback callback) {
        if (success instanceof CallbackGroup) {
            ((CallbackGroup) success).add(callback);
        } else {
            success = new CallbackGroup(success, callback);
        }
        return success;
    }
}
