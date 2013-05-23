package com.zipwhip.framework.pubsub;

import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 10/27/11
 * Time: 11:47 PM
 *
 */
public class UriUtilTest {

    @Test
    public void testName() throws Exception {
        Collection<String> collection = UriUtil.parseIntoParts("/adf/a/b");
        assertTrue(collection.size() == 3);
        collection = UriUtil.parseIntoParts("/adf/a/b");
        assertTrue(collection.size() == 3);
        collection = UriUtil.cachedParseIntoScopes("/adf/a/b");
        assertTrue(collection.size() == 3);
        assertTrue(collection.contains("/adf"));
        assertTrue(collection.contains("/adf/a"));
        assertTrue(collection.contains("/adf/a/b"));
        collection = UriUtil.cachedParseIntoParts("/adf/a/b");
        assertTrue(collection.size() == 3);
        assertTrue(collection.contains("adf"));
        assertTrue(collection.contains("a"));
        assertTrue(collection.contains("b"));

    }
}
