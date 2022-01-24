import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LRUCacheTest {
    private final int CAPACITY = 10;
    private Cache<Integer, Integer> cache;

    @Before
    public void setUp() {
        cache = new LRUCache<>(CAPACITY);
    }

    @Test
    public void testPutAndGet() {
        cache.put(1, 2);
        Assert.assertEquals((Integer) 2, cache.get(1));
    }

    @Test
    public void testPutAFewElements() {
        for (int i = 0; i < CAPACITY; i++) {
            cache.put(i, -i);
        }

        for (int i = 0; i < CAPACITY; i++) {
            Assert.assertEquals((Integer) (-i), cache.get(i));
        }
    }

    @Test
    public void testPutSameElement() {
        for (int i = 0; i < CAPACITY; i++) {
            cache.put(i, -i);
        }

        Assert.assertEquals((Integer) (-1), cache.get(1));

        cache.put(1, 100);

        Assert.assertEquals((Integer) (100), cache.get(1));
    }

    @Test
    public void testCapacityOverflow() {
        for (int i = 0; i <= CAPACITY; i++) {
            cache.put(i, -i);
        }


        Assert.assertNull(cache.get(0));

        for (int i = 1; i <= CAPACITY; i++) {
            Assert.assertEquals((Integer) (-i), cache.get(i));
        }
    }

    @Test
    public void testCapacityOverflowWithActiveElement() {
        Integer activeKey = -1;
        Integer activeValue = -100;
        cache.put(activeKey, activeValue);

        for (int i = 0; i < CAPACITY * 3; i++) {
            cache.put(i, -i);

            Assert.assertEquals(activeValue, cache.get(activeKey));

            if (i + 1 >= CAPACITY) {
                Assert.assertNull(cache.get(i + 1 - CAPACITY));
            }
        }
    }

    @Test
    public void testCapacityOverflowWithActiveElements() {
        int activeCount = 3;
        List<Integer> activeKeys = Arrays.asList(-1, -2, -3);
        List<Integer> activeValues = Arrays.asList(-100, -200, -300);
//        int[] activeValues = new int[]{-100, -200, -300};

        cache.put(activeKeys.get(0), activeValues.get(0));
        for (int i = 0; i < CAPACITY - activeCount; i++){
            cache.put(i, -i);

            if (i == (CAPACITY - activeCount) / 2) {
                cache.put(activeKeys.get(1), activeValues.get(1));
            }
        }
        cache.put(activeKeys.get(2), activeValues.get(2));

        for (int j = 0; j < activeKeys.size(); j++) {
            Assert.assertEquals(activeValues.get(j), cache.get(activeKeys.get(j)));
        }

        for (int i = CAPACITY - activeCount; i < CAPACITY * 3; i++) {
            cache.put(i, -i);

            for (int j = 0; j < activeKeys.size(); j++) {
                Assert.assertEquals(activeValues.get(j), cache.get(activeKeys.get(j)));
            }

            if (i + activeCount >= CAPACITY) {
                Assert.assertNull(cache.get(i + activeCount - CAPACITY));
            }
        }
    }

    @Test
    public void testIllegalCapacity() {
        try {
            cache = new LRUCache<>(0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Illegal Capacity: 0", e.getMessage());
        }

        try {
            cache = new LRUCache<>(-10);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Illegal Capacity: -10", e.getMessage());
        }
    }
}