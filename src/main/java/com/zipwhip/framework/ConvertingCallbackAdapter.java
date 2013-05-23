package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.Callback;
import com.zipwhip.framework.pubsub.EventData;
import com.zipwhip.util.Validatable;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 2:23 PM
 *
 * Does the work of converting, and then calling your callback.
 */
public class ConvertingCallbackAdapter<T extends Validatable> implements Callback {

    EventDataConverter<T> converter;
    DataCallback<T> callback;

    public ConvertingCallbackAdapter(EventDataConverter<T> converter, DataCallback<T> callback) {
        this.converter = converter;
        this.callback = callback;
    }

    @Override
    public void notify(String uri, EventData eventData) throws Exception {
        T data = converter.convert(eventData);

        callback.notify(uri, eventData, data);
    }

}
