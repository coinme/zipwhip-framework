package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.EventData;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 2:25 PM
 *
 * Callback, but with special data.
 */
public interface DataCallback<T> {

    void notify(String uri, EventData eventData, T data);

}
