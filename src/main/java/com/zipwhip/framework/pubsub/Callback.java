package com.zipwhip.framework.pubsub;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 12/16/10
 * Time: 8:16 PM
 * <p/>
 * When someone publishes, this is what is called.
 */
public interface Callback {

    /**
     * @param uri       The URI that occurred.
     * @param eventData The eventData that was published.
     * @throws Exception if you need to throw, you can. it'll get logged.
     */
    void notify(String uri, EventData eventData) throws Exception;

}
