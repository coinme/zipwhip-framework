package com.zipwhip.framework.pubsub;

import com.zipwhip.framework.pubsub.EventData;
import com.zipwhip.framework.pubsub.EventDataConverter;
import com.zipwhip.framework.pubsub.EventDataUtil;
import com.zipwhip.util.DataConversionException;

/**
 * Date: 6/19/13
 * Time: 11:50 AM
 *
 * A simple implementation. It uses the EventDataUtil to convert the EventData using types. This is used to clean up
 * the code since 90% of the time, most implementations of Features that use PubSub just use EventDataUtil anyway.
 *
 * @author Michael
 * @version 1
 */
public class StandardEventDataConverter<T> implements EventDataConverter<T> {

    private final Class<T> clazz;

    public StandardEventDataConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convert(EventData eventData) throws DataConversionException {
        return EventDataUtil.getExtra(clazz, eventData, 0);
    }
}
