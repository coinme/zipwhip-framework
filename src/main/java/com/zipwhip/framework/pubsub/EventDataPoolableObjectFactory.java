package com.zipwhip.framework.pubsub;

import com.zipwhip.pools.PoolUtil;
import com.zipwhip.pools.PoolableObjectFactoryBase;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 10/28/11
 * Time: 1:03 AM
 *
 * So we can pool these guys
 */
public class EventDataPoolableObjectFactory extends PoolableObjectFactoryBase {

    private static final PoolableObjectFactory instance = new EventDataPoolableObjectFactory();
    private static final ObjectPool POOL = PoolUtil.getPool(getInstance());

    @Override
    public Object makeObject() throws Exception {
        return new MemoryEventData();
    }

    @Override
    public void destroyObject(Object obj) throws Exception {
        ((EventData) obj).destroy();
    }

    @Override
    public boolean validateObject(Object obj) {
        return true;
    }

    @Override
    public void activateObject(Object obj) throws Exception {

    }

    @Override
    public void passivateObject(Object obj) throws Exception {
        if (obj instanceof MemoryEventData) {
            ((MemoryEventData) obj).success = null;
            ((MemoryEventData) obj).failure = null;
            ((MemoryEventData) obj).completion = null;
        }

        ((EventData) obj).setExtras(null);
    }

    public static EventData borrow(Object... args) {
        EventData e = PoolUtil.borrow(POOL);

        e.setExtras(args);

        return e;
    }

    public static void release(EventData e){
        PoolUtil.release(POOL, e);
    }

    public static PoolableObjectFactory getInstance() {
        return instance;
    }
}
