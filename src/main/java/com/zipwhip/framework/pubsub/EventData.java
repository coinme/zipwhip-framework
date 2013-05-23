package com.zipwhip.framework.pubsub;

import com.zipwhip.lifecycle.DestroyableBase;
import com.zipwhip.util.CollectionUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/16/10
 * Time: 9:18 PM
 * <p/>
 * A placeholder for the JMS event payload
 */
public class EventData extends DestroyableBase {

    private static final String NO_DATA_IN_THE_EVENT = "No data in the event.";

    /**
     * The arguments that were passed into the broker
     */
    private Object[] extras;

    public EventData() {

    }

    public EventData(Object[] extras) {
        this();
        this.extras = extras;
    }

    public EventData(Object extras) {
        this(new Object[]{ extras });
    }

    @Override
    public String toString() {
        if (CollectionUtil.isNullOrEmpty(extras)) {
            return NO_DATA_IN_THE_EVENT;
        } else {
            return EventDataUtil.getString(this);
        }
    }

    public Object[] getExtras() {
        return extras;
    }

    public void setExtras(Object[] extras) {
        this.extras = extras;
    }

    @Override
    protected void onDestroy() {
        extras = null;
    }

}
