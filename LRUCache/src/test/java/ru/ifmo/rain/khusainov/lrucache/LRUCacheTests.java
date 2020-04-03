package ru.ifmo.rain.khusainov.lrucache;

import main.java.ru.ifmo.rain.khusainov.lrucache.LRUCache;
import org.junit.Assert;
import org.junit.Test;

import static java.lang.Math.abs;

public class LRUCacheTests {

    private static final class LinkedHashMapCache<K, V> extends LinkedHashMap<K, V> {
        private static final int MAX_ENTRIES = 20;

        LinkedHashMapCache() {
            super(MAX_ENTRIES, 0.5f, true);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > MAX_ENTRIES;
        }
    }

    @Test
    public void test_single_store() {
        final LRUCache<Integer, Integer> single = new LRUCache<>(1);
        Assert.assertEquals(0, single.size());
        Assert.assertNull(single.get(0));
        single.put(0, 1);
        Assert.assertEquals(Integer.valueOf(1), single.get(0));
        Assert.assertEquals(1, single.size());
    }

    @Test
    public void test_single_hit() {
        final LRUCache<Integer, Integer> single = new LRUCache<>(1);
        single.put(0, 1);
        single.put(1, 2);
        Assert.assertNull(single.get(0));
        Assert.assertEquals(Integer.valueOf(2), single.get(1));
        Assert.assertEquals(1, single.size());
    }

    @Test
    public void test_single_reassign() {
        final LRUCache<Integer, Integer> single = new LRUCache<>(1);
        single.put(0, 1);
        single.put(0, 2);
        Assert.assertNull(single.get(1));
        Assert.assertEquals(Integer.valueOf(2), single.get(0));
        Assert.assertEquals(1, single.size());
    }

    @Test
    public void test_multiple_store() {
        final LRUCache<Integer, Integer> cache = new LRUCache<>(4);
        cache.put(0, 5);
        cache.put(20, 21);
        cache.put(1, 8);
        cache.put(15, 12);
        Assert.assertNull(cache.get(4));
        Assert.assertEquals(Integer.valueOf(5), cache.get(0));
        Assert.assertEquals(Integer.valueOf(8), cache.get(1));
        Assert.assertEquals(Integer.valueOf(21), cache.get(20));
        Assert.assertEquals(Integer.valueOf(12), cache.get(15));
        Assert.assertEquals(4, cache.size());
    }

    @Test
    public void test_multiple_hits() {
        final LRUCache<Integer, Integer> cache = new LRUCache<>(4);
        cache.put(0, 5);
        cache.put(20, 21);
        cache.put(1, 8);
        cache.put(15, 12);
        cache.put(2, 5);
        cache.put(3, 4);
        cache.put(0, 2);
        Assert.assertNull(cache.get(1));
        Assert.assertNull(cache.get(20));
        Assert.assertEquals(Integer.valueOf(12), cache.get(15));
        Assert.assertEquals(Integer.valueOf(5), cache.get(2));
        Assert.assertEquals(Integer.valueOf(4), cache.get(3));
        Assert.assertEquals(Integer.valueOf(2), cache.get(0));
        Assert.assertEquals(4, cache.size());
    }

    @Test
    public void test_multiple_reassign() {
        final LRUCache<Integer, Integer> cache = new LRUCache<>(4);
        cache.put(0, 5);
        cache.put(20, 21);
        cache.put(0, 2);
        cache.put(0, 2);
        cache.put(20, 21);
        Assert.assertEquals(Integer.valueOf(2), cache.get(0));
        Assert.assertEquals(Integer.valueOf(21), cache.get(20));
        Assert.assertEquals(2, cache.size());
    }

    @Test
    public void test_multiple_access() {
        final LRUCache<Integer, Integer> cache = new LRUCache<>(4);
        cache.put(0, 5);
        cache.put(20, 21);
        cache.put(1, 8);
        cache.put(15, 12);
        cache.get(0);
        cache.get(20);
        cache.put(2, 5);
        cache.put(3, 4);
        Assert.assertNull(cache.get(1));
        Assert.assertNull(cache.get(15));
        Assert.assertEquals(Integer.valueOf(5), cache.get(0));
        Assert.assertEquals(Integer.valueOf(21), cache.get(20));
        Assert.assertEquals(Integer.valueOf(5), cache.get(2));
        Assert.assertEquals(Integer.valueOf(4), cache.get(3));
        Assert.assertEquals(4, cache.size());
    }

    @Test
    public void test_random() {
        final LRUCache<Integer, Double> cache = new LRUCache<>(20);
        final Map<Integer, Double> expected = new LinkedHashMapCache<>();
        final Random rnd = new Random();
        final List<Integer> keyArray = new ArrayList<>();
        final int keyNumber = 100;
        for (int i = 0; i < keyNumber; i++) {
            keyArray.add(rnd.nextInt());
        }
        for (int i = 0; i < 1000000; i++) {
            int nextOperation = rnd.nextInt() % 2;
            int key = keyArray.get(abs(rnd.nextInt()) % keyNumber);
            if (nextOperation == 0) {
                double value = rnd.nextDouble();
                cache.put(key, value);
                expected.put(key, value);
            } else if (nextOperation == 1) {
                Assert.assertEquals(expected.get(key), cache.get(key));
            }
            Assert.assertEquals(expected.size(), cache.size());
        }
    }
}
