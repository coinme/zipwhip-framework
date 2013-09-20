package com.zipwhip.framework.pubsub;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/16/10
 * Time: 9:18 PM
 */
public class MemoryEventData extends EventData {

    public Callback success;
    public Callback failure;
    public Callback completion;

    /**
     * This will ensure you'll get the same callback of completion (but you wont know if it succeeded or failed.
     * <p/>
     * Both the sucess/failure callbacks will be used.
     *
     * @param callback
     */
    public MemoryEventData(Callback callback) {
        this.success = callback;
        this.failure = callback;
    }

    public MemoryEventData(Object... extras) {
        this.setExtras(extras);
    }

    public MemoryEventData(Callback success, Object... extras) {
        this.setExtras(extras);

        this.success = success;
    }

    public MemoryEventData(Callback success, Callback failure, Object... extras) {
        this.setExtras(extras);

        this.success = success;
        this.failure = failure;
    }

    public MemoryEventData() {

    }

    public void observeSuccess(Callback callback) {
        success = CallbackGroup.add(success, callback);
    }

    public void observeFailure(Callback callback) {
        failure = CallbackGroup.add(failure, callback);
    }

    public void observeCompletion(Callback callback) {
        completion = CallbackGroup.add(completion, callback);
    }

    /**
     * Notify the callback (if it exists) that we're done!
     *
     * @param uri
     */
    public void succeed(String uri, EventData request) {
        notify(success, uri, request);
        notify(completion, uri, request);
    }

    private void notify(final Callback callback, final String uri, final EventData request) {
        if (callback == null) {
            return;
        }

        try {
            callback.notify(uri, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void succeed(String uri, Object... args) {
        EventData e = new MemoryEventData();
        e.setExtras(args);
        succeed(uri, e);
    }

    public void succeed(String uri) {
        succeed(uri, this);
    }

    public void fail(String uri, EventData request) {
        // notify that we're done!
        notify(failure, uri, request);
        notify(completion, uri, request);
    }

    public void fail(String uri, Object... args) {
        EventData e = new MemoryEventData();
        e.setExtras(args);
        fail(uri, e);
    }

    public void fail(String uri) {
        fail(uri, this);
    }

    @Override
    protected void onDestroy() {
        failure = null;
        success = null;
    }

}
