package com.zipwhip.framework.pubsub;

import com.zipwhip.executors.ThreadPoolManager;
import com.zipwhip.lifecycle.CascadingDestroyableBase;
import com.zipwhip.pools.PoolUtil;
import com.zipwhip.pools.PoolableObjectFactoryBase;
import com.zipwhip.util.Directory;
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
public class MemoryBroker extends CascadingDestroyableBase implements Broker {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryBroker.class);

    private static final ObjectPool EVENT_DATA_POOL = PoolUtil.getPool(EventDataPoolableObjectFactory.getInstance());
    private final ObjectPool RUN_POOL = PoolUtil.getPool(new PoolableObjectFactoryBase() {
        @Override
        public Object makeObject() throws Exception {
            return new PubSubWorker();
        }

        @Override
        public void passivateObject(Object obj) throws Exception {
            PubSubWorker worker = (PubSubWorker)obj;

            // We don't need to clear these because they are not memory leaks.
//            worker.setUri(null);
//            worker.setPooled(false);

            // These never change, so no need to null them.
//            worker.setPubSubWorkerPool(null);
//            worker.setDirectory(null);

            // We should clear this reference so EventArgs can be garbage collected if needed.
            worker.setArgs(null);
        }
    });

    private LocalDirectory<String, Callback> directory = new ListDirectory<String, Callback>();
	private Executor executor;

	public MemoryBroker() {
		this(ThreadPoolManager.getInstance().getFixedThreadPool());

        // Normally when we create Executors we need to attach a destroy listener.
        // We do not need to shut down the thread pool once we are destroyed because it is shared.
	}

	public MemoryBroker(Executor executor) {
		this.executor = executor;
	}

	public void publish(String uri) {
        EventData e = PoolUtil.borrow(EVENT_DATA_POOL);

        // The EventData will be released by the PubSubWorker

        execute(uri, e, true);
	}

    @Override
    public void publish(String uri, Object... args) {
        EventData e = PoolUtil.borrow(EVENT_DATA_POOL);
        // copy over the data
        e.setExtras(args);

        execute(uri, e, true);
    }

	public void publish(final String uri, final EventData args) {
        execute(uri, args, false);
    }

    public void publish(String uri, Callback callback) {
        MemoryEventData e = PoolUtil.borrow(EVENT_DATA_POOL);

        e.success = callback;

        execute(uri, e, true);
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

    protected void execute(final String uri, final EventData args, final boolean pooled) {
        // The worker will self release when completed.
        execute(borrowWorker(uri, args, pooled));
    }

    protected void execute(final PubSubWorker worker) {
        executor.execute(worker);
    }

    private PubSubWorker borrowWorker(String uri, EventData args, boolean pooled) {
        PubSubWorker pubSubWorker = PoolUtil.borrowSafely(LOGGER, PubSubWorker.class, RUN_POOL);

        pubSubWorker.setArgs(args);
        pubSubWorker.setUri(uri);
        pubSubWorker.setPooled(pooled);
        pubSubWorker.setDirectory(directory);
        pubSubWorker.setPubSubWorkerPool(RUN_POOL);

        return pubSubWorker;
    }

    private static class PubSubWorker implements Runnable {

        private boolean pooled;
        private String uri;
        private EventData args;
        private Directory<String, Callback> directory;
        private ObjectPool pubSubWorkerPool;

        @Override
        public void run() {
            try {
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
                    PoolUtil.release(EVENT_DATA_POOL, args);
                }

                PoolUtil.releaseSafely(LOGGER, pubSubWorkerPool, this);
            }
        }

        public boolean isPooled() {
            return pooled;
        }

        public void setPooled(boolean pooled) {
            this.pooled = pooled;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public EventData getArgs() {
            return args;
        }

        public void setArgs(EventData args) {
            this.args = args;
        }

        public Directory<String, Callback> getDirectory() {
            return directory;
        }

        public void setDirectory(Directory<String, Callback> directory) {
            this.directory = directory;
        }

        public ObjectPool getPubSubWorkerPool() {
            return pubSubWorkerPool;
        }

        public void setPubSubWorkerPool(ObjectPool pubSubWorkerPool) {
            this.pubSubWorkerPool = pubSubWorkerPool;
        }
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
