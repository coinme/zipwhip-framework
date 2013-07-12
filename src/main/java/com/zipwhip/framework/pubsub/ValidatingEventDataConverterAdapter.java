package com.zipwhip.framework.pubsub;

import com.zipwhip.util.DataConversionException;
import com.zipwhip.util.Validatable;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 2:49 PM
 */
public class ValidatingEventDataConverterAdapter<T extends Validatable>  implements EventDataConverter<T> {

    private final EventDataConverter<T> converter;

    public ValidatingEventDataConverterAdapter(EventDataConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public T convert(EventData eventData) throws DataConversionException {
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
