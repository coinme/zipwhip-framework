package com.zipwhip.framework.pubsub;

import com.zipwhip.framework.pubsub.EventData;
import com.zipwhip.util.Converter;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 2:20 PM
 *
 * Allows you to convert from 1 type to another
 */
public interface EventDataConverter<T> extends Converter<EventData, T> {

    // todo: maybe add cool shit here

}
