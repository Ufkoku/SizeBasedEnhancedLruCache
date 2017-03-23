package com.ufkoku.cache;

import com.vladium.utils.ObjectProfiler;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SizeBasedEnhancedLruCacheTest {

    @Test
    public void fillTest() {
        HashSet<String> strs = new HashSet<>();
        strs.add("Str1");
        strs.add("Str2");
        strs.add("Str3");

        List<HashSet<String>> sets = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            sets.add(new HashSet<>(strs));
        }

        SizeBasedEnhancedLruCache<String, Object> cache = new SizeBasedEnhancedLruCache<>(ObjectProfiler.sizeof(strs) * 5);
        for (int i = 0; i < 10; i++) {
            cache.put("" + i, sets.get(i));
        }
        Assert.assertNotEquals(10, cache.getEntryCount());
    }

}
