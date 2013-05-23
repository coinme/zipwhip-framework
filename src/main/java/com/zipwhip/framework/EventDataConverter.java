package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.EventData;
import com.zipwhip.util.Converter;
import com.zipwhip.util.Validatable;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 2:20 PM
 *
 * Allows you to convert from 1 type to another
 */
public interface EventDataConverter<T extends Validatable> extends Converter<EventData, T> {

    // todo: maybe add cool shit here

}
