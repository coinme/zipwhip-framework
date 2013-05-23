package com.zipwhip.framework.pubsub;

import com.zipwhip.executors.ThreadPoolManager;
import com.zipwhip.lifecycle.DestroyableBase;
import com.zipwhip.pools.PoolUtil;
import com.zipwhip.util.ListDirectory;
import com.zipwhip.util.LocalDirectory;
import com.zipwhip.util.StringUtil;
import org.apache.commons.pool.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.Executor;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 5/17/11
 * Time: 6:45 PM
 * <p/>
 * A simple in-memory broker that executes async. It will execute all groups in parallel, but items sync.
 */
public class MemoryBroker extends DestroyableBase implements Broker {

    private static final ObjectPool POOL = PoolUtil.getPool(EventDataPoolableObjectFactory.getInstance());
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryBroker.class);

    private LocalDirectory<String, Callback> directory = new ListDirectory<String, Callback>();
	private Executor executor;

	public MemoryBroker() {
		this(ThreadPoolManager.getInstance().getFixedThreadPool());
        // we do not need to shut down the thread pool once we are destroyed because it is shared.
	}

	public MemoryBroker(Executor executor) {
		this.executor = executor;
	}

	public void publish(String uri) {
		publish(uri, (EventData) null, false);
	}

    @Override
    public void publish(String uri, Object... args) {
        EventData e = PoolUtil.borrow(POOL);
        // copy over the data
        e.setExtras(args);

        publish(uri, e, true);
    }

	public void publish(final String uri, final EventData args) {
        publish(uri, args, false);
    }

	private void publish(final String uri, final EventData args, final boolean pooled) {
		executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Collection<Callback> globals = directory.get("*");

                    if (globals != null) {
                        for (Callback global : globals) {
                            try {
                                global.notify(uri, args);
                            } catch (Exception e) {
                                LOGGER.error("Problem with global callback", e);
                            }
                        }
                    }

                    Collection<String> parts = UriUtil.cachedParseIntoScopes(uri);

                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace(StringUtil.join("publish: ", uri, "   (", args, ")"));
                    }

                    if (parts == null) {
                        return;
                    }

                    for (String _uri : parts) {
                        Collection<Callback> items = directory.get(_uri);
                        if (items == null) {
                            continue;
                        }

                        for (Callback item : items) {
                            try {
                                item.notify(uri, args);
                            } catch (Exception e) {
                                LOGGER.error(String.format("Problem with observer to uri %s", _uri), e);
                            }
                        }
                    }
                } finally {
                    if (pooled && args != null) {
                        PoolUtil.release(POOL, args);
                    }
                }
            }
        });
	}

	public void publish(String uri, Callback callback) {
		publish(uri, new MemoryEventData(callback));
	}

	@Override
	public void subscribe(String uri, Callback callback) {
		if (directory != null) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(StringUtil.join("subscribe: ", uri, "   (", uri, ")"));
            }

            directory.add(uri, callback);
		} else {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(StringUtil.join("subscribe: ", uri, "   (", uri, ") (warning: directory null!)"));
            }
        }
	}

	@Override
	public void unsubscribe(String uri, Callback callback) {
		directory.remove(uri, callback);
	}

	@Override
	protected void onDestroy() {
		directory.clear();
		executor = null;
	}

	protected LocalDirectory<String, Callback> getDirectory() {
		return directory;
	}

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
