package com.zipwhip.framework.pubsub;

import com.zipwhip.util.CollectionUtil;
import com.zipwhip.util.HashCodeComparator;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/17/10
 * Time: 1:08 AM
 * <p/>
 * Utilities for working with PubSub to make dealing with Extras easier.
 */
public class EventDataUtil {

    /**
     * Defaults to the first item.
     *
     * @param request
     * @return
     */
    public static String getString(EventData request) {
        return getString(request, 0);
    }

    /**
     * Get an extra and cast it to a string, if it's not a string just return null.
     *
     * @param request
     * @param index
     * @return
     */
    public static String getString(EventData request, int index) {
        Object result = getExtra(request, index);
        if (result instanceof String) {
            return (String) result;
        }
        if (result == null) {
            return null;
        }

        return String.valueOf(result);
    }

    public static Long getLong(EventData request) {
        return getLong(request, 0);
    }

    public static Long getLong(EventData request, int index) {
        Object result = getExtra(request, index);
        if (result instanceof Long) {
            return (Long) result;
        } else if (result instanceof Integer) {
            return Long.valueOf(result.toString());
        } else if (result instanceof String) {
            return Long.valueOf((String) result);
        } else {
            return null;
        }
    }

    public static <T> T getExtra(Class<T> clazz, EventData request) {
        return getExtra(clazz, request, 0);
    }

    public static <T> T getExtra(Class<T> clazz, EventData request, int index) {
        Object result = getExtra(request, index);
        if (result == null) {
            return null;
        }

        if (clazz.isInstance(result)) {
            return (T) result;
        }

        return null;
    }

    public static Object getExtra(EventData request) {
        return getExtra(request, 0);
    }

    public static Object getExtra(EventData request, int index) {
        if (request == null) {
            return null;
        }
        return CollectionUtil.get(request.getExtras(), index);
    }

    public static <T> List<T> getList(Class<T> clazz, EventData request, int index) {
        if (request == null) {
            return null;
        }
        Object object = getExtra(request, index);
        if (object instanceof List) {
            return (List<T>) object;
        } else if (object instanceof Collection){
            return new ArrayList<T>((Collection)object);
        } else if (object != null) {
            if (clazz.isInstance(object)) {
                return Arrays.asList((T) object);
            }
        }
        return null;
    }

    public static <T> Collection<T> getCollection(Class<T> clazz, EventData request, int index) {
        if (request == null) {
            return null;
        }

        Object object = getExtra(request, index);
        if (object instanceof Collection) {
            return (Collection<T>) object;
        } else if (object != null) {
            return Arrays.asList((T) object);
        }
        return null;
    }

    public static <T> Set<T> getSet(Class<T> clazz, EventData request) {
        return getSet(clazz, request, 0);
    }

    public static <T> Set<T> getSet(Class<T> clazz, EventData request, int index) {
        if (request == null) {
            return null;
        }
        Object object = getExtra(request, index);
        if (object instanceof Set) {
            return (Set<T>)object;
        } else if (clazz.isInstance(object)) {
            TreeSet<T> result = new TreeSet<T>(HashCodeComparator.getInstance());
            result.add((T)object);
            return result;
        } else if (object instanceof Collection) {
            TreeSet<T> result = new TreeSet<T>(HashCodeComparator.getInstance());
            result.addAll((Collection<T>) object);
            return result;
        }

        return null;
    }

    public static <T> boolean getBoolean(EventData request, int index) {
        Object object = getExtra(request, index);
        if (object == null) {
            return false;
        }
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        return false;
    }

    public static <T> List<T> getList(Class<T> clazz, EventData request) {
        return getList(clazz, request, 0);
    }

    public static <T> Collection<T> getCollection(Class<T> clazz, EventData request) {
        return getCollection(clazz, request, 0);
    }

    public static Integer getInteger(EventData eventData, int index) {
        Object object = getExtra(eventData, index);
        if (object instanceof Integer) {
            return (Integer) object;
        }
        return null;
    }

    public static Integer getInteger(EventData eventData) {
        return getInteger(eventData, 0);
    }


    public static void fail(Broker broker, String uri, EventData eventData, UriAgent uriAgent, Throwable throwable) {
        if (eventData != null) {
            if (eventData instanceof MemoryEventData) {
                ((MemoryEventData) eventData).fail(uri, throwable);
            }
        }

        if (uriAgent != null) {
            broker.publish(uriAgent.getFailureUri(), throwable);
        }
    }

    public static void succeed(Broker broker, String uri, EventData eventData, UriAgent uriAgent, Object... extras) {
        if (eventData != null) {
            if (eventData instanceof MemoryEventData) {
                ((MemoryEventData) eventData).succeed(uri, extras);
            }
        }
        if (uriAgent != null) {
            broker.publish(uriAgent.getSuccessUri(), extras);
        }
    }
}
