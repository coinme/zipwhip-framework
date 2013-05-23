package com.zipwhip.framework;

import com.zipwhip.framework.pubsub.EventData;
import com.zipwhip.util.DataConversionException;
import com.zipwhip.util.Validatable;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 2:49 PM
 *
 *
 */
public class ValidatingEventDataConverterAdapter<T extends Validatable>  implements EventDataConverter<T> {

    EventDataConverter<T> converter;

    protected ValidatingEventDataConverterAdapter(EventDataConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public T convert(EventData eventData) throws DataConversionException {

//        NullEventDataConverter will take care of this
//        if (CollectionUtil.isNullOrEmpty(eventData.getExtras())){
//            throw new DataConversionException("Can't convert this");
//        }

        T data = converter.convert(eventData);

        try {
            if (!data.validate()){
                throw new DataConversionException("Not valid data");
            }
        } catch (Exception e) {
            throw new DataConversionException(e);
        }

        return data;
    }

}
