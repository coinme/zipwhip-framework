package com.zipwhip.framework.pubsub;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 2/10/11
 * Time: 4:46 PM
 * <p/>
 * works on uri parts.
 */
public class UriUtil {

    private static final Map<String, Collection<String>> SCOPE_CACHE = Collections.synchronizedMap(new HashMap<String, Collection<String>>());
    private static final Map<String, Collection<String>> PARTS_CACHE = Collections.synchronizedMap(new HashMap<String, Collection<String>>());

    /**
     * Employs static caching. This has a limit on the number of distinct URI's you can pass in.
     *
     * Results from this method will never be modified. They are safe to iterate on without synchronizing the collection.
     *
     * @param uri
     * @return
     */
    public static Collection<String> cachedParseIntoScopes(String uri){
        Collection<String> result = SCOPE_CACHE.get(uri);

        if (result == null){
            result = parseIntoScopes(uri);

            SCOPE_CACHE.put(uri, result);
        }

        return result;
    }

    /**
     * @deprecated Check out cachedParseIntoScopes instead
     * @param uri
     * @return
     */
    @Deprecated
    public static Collection<String> parseIntoScopes(String uri) {
        if (uri == null) {
            return null;
        }

        boolean startsWith = uri.startsWith("/");
        StringBuilder sb = new StringBuilder();
        StringTokenizer parts = new StringTokenizer(uri, "/");
        List<String> result = null;

        boolean first = true;
        while (parts.hasMoreElements()) {
            if (startsWith || !first) {
                sb.append("/");
            }

            first = false;
            sb.append(parts.nextElement());

            uri = sb.toString();

            if (result == null) {
                result = new ArrayList<String>(3);
            }

            result.add(uri);
        }

        return result;
    }

    public static Collection<String> cachedParseIntoParts(String uri) {
        Collection<String> result = PARTS_CACHE.get(uri);

        if (result == null){
            result = parseIntoParts(uri);

            PARTS_CACHE.put(uri, result);
        }

        return result;
    }

    @Deprecated
    public static Collection<String> parseIntoParts(String uri) {
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        return Arrays.asList(uri.split("\\/"));
    }
}
