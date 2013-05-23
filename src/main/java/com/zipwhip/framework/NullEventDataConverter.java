package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.EventData;
import com.zipwhip.util.DataConversionException;
import com.zipwhip.util.Validatable;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 3:43 PM
 *
 * Fake implementation when you dont care.
 */
public class NullEventDataConverter<T extends Validatable> implements EventDataConverter<T> {
//public class NullEventDataConverter<T extends Validatable> extends ValidatingEventDataConverterAdapter {

//    protected NullEventDataConverter(EventDataConverter eventDataConverter) {
//        super(eventDataConverter);
//    }

    @Override
    public T convert(EventData eventData) throws DataConversionException {
        return null;
    }

}
