package com.zipwhip.framework.pubsub;

import com.zipwhip.util.CollectionUtil;
import com.zipwhip.util.StringUtil;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 2/10/11
 * Time: 4:33 PM
 * <p/>
 * Helps building pusub stuff
 */
public class UriAgent {

    Collection<String> uris;

    // caching
    private String successUri = null;
    private String failureUri = null;
    private String basicUri = null;

    String successPrefix = "success";
    String failurePrefix = "failure";

    public UriAgent(String... uris) {
        if (CollectionUtil.isNullOrEmpty(uris)) {
            return;
        }

        for (String uri : uris) {
            Collection<String> _uris = UriUtil.cachedParseIntoParts(uri);

            this.uris = CollectionUtil.addAllEfficient(this.uris, _uris);
        }
    }

    public String getUri() {
        if (StringUtil.isNullOrEmpty(basicUri)) {
            basicUri = getUri(null);
        }
        return basicUri;
    }

    public String getSuccessUri() {
        if (StringUtil.isNullOrEmpty(successUri)) {
            successUri = getUri(successPrefix);
        }
        return successUri;
    }

    public String getFailureUri() {
        if (StringUtil.isNullOrEmpty(failureUri)) {
            failureUri = getUri(failurePrefix);
        }
        return failureUri;
    }

    private String getUri(String prefix) {
        StringBuilder sb = new StringBuilder();
        int size = uris.size();
        int index = 0;

        if (!StringUtil.isNullOrEmpty(prefix)) {
            sb.append("/");
            sb.append(prefix);
        }

        for (String uri : uris) {
            index++;
            sb.append("/");
            sb.append(uri);
        }

        return sb.toString();
    }
}
