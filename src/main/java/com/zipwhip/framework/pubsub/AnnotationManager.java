package com.zipwhip.framework.pubsub;

import com.zipwhip.util.CollectionUtil;
import com.zipwhip.util.Converter;
import com.zipwhip.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 3/28/13
 * Time: 10:05 AM
 *
 * @author Michael
 * @version 1
 */
public class AnnotationManager {

    private static final Map<String, Converter<EventData, ?>> CONVERTERS = Collections.synchronizedMap(new HashMap<String, Converter<EventData, ?>>());

    public static void register(String converterName, Converter<EventData, ?> converter) {
        CONVERTERS.put(converterName, converter);
    }

    public static void attach(final Broker broker, final Object object) {
        if (broker == null) {
            throw new IllegalArgumentException("The broker cannot be null");
        } else if (object == null) {
            throw new IllegalArgumentException("The object cannot be null");
        }

        Method[] methods = object.getClass().getMethods();

        if (CollectionUtil.isNullOrEmpty(methods)) {
            return;
        }

        for (final Method method : methods) {
            if (!method.isAnnotationPresent(Subscribe.class)) {
                continue;
            }

            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            String uri = subscribe.uri();

            if (StringUtil.isNullOrEmpty(uri)) {
                throw new IllegalStateException("The uri cannot be null in a Subscribe annotation");
            }

            final Converter<EventData, ?> converter = CONVERTERS.get(subscribe.converter());

            if (converter == null) {
                if (method.getParameterTypes().length == 2) {
                    // do the subscription.
                    broker.subscribe(uri, new Callback() {
                        @Override
                        public void notify(String uri, EventData eventData) throws Exception {
                            method.invoke(object, uri, eventData);
                        }
                    });
                } else if (method.getParameterTypes().length == 1) {
                    if (method.getParameterTypes()[0] == String.class) {
                        // they just want the URI
                        broker.subscribe(uri, new Callback() {
                            @Override
                            public void notify(String uri, EventData eventData) throws Exception {
                                method.invoke(object, uri);
                            }
                        });
                    } else if (method.getParameterTypes()[0] == EventData.class) {
                        // they just want the eventData
                        broker.subscribe(uri, new Callback() {
                            @Override
                            public void notify(String uri, EventData eventData) throws Exception {
                                method.invoke(object, eventData);
                            }
                        });
                    } else if (method.getParameterTypes()[0] == EventStatus.class){
                        final UriAgent agent = new UriAgent(uri);
                        broker.subscribe(uri, new Callback() {
                            @Override
                            public void notify(String uri, EventData eventData) throws Exception {
                                EventStatus status = new EventStatus();

                                status.setBroker(broker);
                                status.setEventData(eventData);
                                status.setUri(uri);
                                status.setAgent(agent);

                                method.invoke(object, eventData);
                            }
                        });
                    } else {
                        throw new IllegalStateException("Cannot register a method of this signature. " + uri);
                    }
                }
            } else {
                if (method.getParameterTypes().length == 3) {
                    // do the subscription.
                    broker.subscribe(uri, new Callback() {
                        @Override
                        public void notify(String uri, EventData eventData) throws Exception {
                            method.invoke(object, uri, eventData, converter.convert(eventData));
                        }
                    });
                } else if (method.getParameterTypes().length == 2) {
                    // do the subscription.
                    broker.subscribe(uri, new Callback() {
                        @Override
                        public void notify(String uri, EventData eventData) throws Exception {
                            method.invoke(object, uri, converter.convert(eventData));
                        }
                    });
                } else if (method.getParameterTypes().length == 1) {
                    // they just want the eventData
                    broker.subscribe(uri, new Callback() {
                        @Override
                        public void notify(String uri, EventData eventData) throws Exception {
                            method.invoke(object, converter.convert(eventData));
                        }
                    });
                } else {
                    throw new IllegalStateException("Cannot register a method of this signature");
                }
            }
        }
    }
}
