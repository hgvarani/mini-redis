package com.hugovarani.miniredis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MiniRedisServiceTest {

    private final MiniRedisService miniRedis = new MiniRedisService();

    @Test
    void shouldSetACorrectKey(){
        Assertions.assertEquals(miniRedis.set("myKey", "myValue", null), "OK");
    }

    @Test
    void setShouldThrowNPEWhenKeyIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> miniRedis.set(null, "myValue", null));
    }

    @Test
    void setShouldThrowAnErrorWhenTimeIsInvalid(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> miniRedis.set("a", "a", -2));
    }

    @Test
    void setShouldThrowNPEWhenValueIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> miniRedis.set("myKey", null, null));
    }

    @Test
    void shouldGetExistingValueByKey(){
        miniRedis.set("myKey", "myValue", null);
        Assertions.assertEquals(miniRedis.get("myKey"), "myValue");
    }

    @Test
    void shouldGetNullValueByNonExistingKey(){
        miniRedis.set("myKey", "myValue", null);
        Assertions.assertEquals(miniRedis.get("notAKey"), "(nil)");
    }

    @Test
    void delShouldRemoveAllExistingElements(){
        miniRedis.set("a","a", null);
        miniRedis.set("b","b", null);
        miniRedis.set("c","c", null);
        miniRedis.set("d","d", null);

        Assertions.assertEquals(miniRedis.del("a", "b", "c", "d"), 4);
    }

    @Test
    void delShouldRemoveOnlyExistingElements(){
        miniRedis.set("b","b", null);
        miniRedis.set("c","c", null);
        miniRedis.set("d","d", null);

        Assertions.assertEquals(miniRedis.del("a", "b", "c", "d"), 3);
    }

    @Test
    void dbSizeShouldCountCorrectlyAfterRemovingValues(){
        miniRedis.set("b","b", null);
        miniRedis.set("c","c", null);
        miniRedis.set("d","d", null);

        Assertions.assertEquals(miniRedis.dbSize(), 3);

        miniRedis.del("b");
        Assertions.assertEquals(miniRedis.dbSize(), 2);
    }

    @Test
    void incrShouldIncrementExistingValue(){
        miniRedis.set("a", "5", null);

        Assertions.assertEquals(miniRedis.incr("a"), 6);
    }

    @Test
    void incrShouldReturnOneIfValueDOesNotExist(){
        Assertions.assertEquals(miniRedis.incr("a"), 1);
    }

    @Test
    void shouldReturnNullIfValueIsInvalid(){
        miniRedis.set("a", "a", null);

        Assertions.assertNull(miniRedis.incr("a"));
    }

    @Test
    void zaddShouldSaveNormally(){
        Assertions.assertEquals(miniRedis.zadd("myZSet", 1, "example"), 1);
    }

    @Test
    void zcardShouldPrintCorrectElementNumbers(){
        miniRedis.zadd("mySet", 1, "one");
        Assertions.assertEquals(miniRedis.zcard("mySet"), 1);

        miniRedis.zadd("mySet", 2, "two");
        Assertions.assertEquals(miniRedis.zcard("mySet"), 2);

        Assertions.assertEquals(miniRedis.zcard("invalidSet"), 0);
    }

    @Test
    void zaddShouldReplaceExistingScores(){
        miniRedis.zadd("aSet", 1, "one");
        miniRedis.zadd("aSet", 1, "notOne");

        List<String> expectedResult = Arrays.asList("1) notOne");

        Assertions.assertEquals(miniRedis.zrange("aSet", 1, 1),  expectedResult);
    }

    @Test
    void zrangeShouldGetEmptyListWithWrongZSet(){
        Assertions.assertEquals(miniRedis.zrange("unexistingZSet", 1, 1), Collections.emptyList());
    }

    @Test
    void zrangeShouldGetElementsInRange(){
        miniRedis.zadd("zset", 1, "insideRange");
        miniRedis.zadd("zset", 9, "outsideRange");

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("1) insideRange");

        Assertions.assertEquals(miniRedis.zrange("zset", 1, 5), expectedResult);

        miniRedis.zadd("zset", 5, "insideToo");
        expectedResult.add("5) insideToo");

        Assertions.assertEquals(miniRedis.zrange("zset", 1, 5), expectedResult);
    }

    @Test
    void zrangeShouldPrintSortedElements(){
        miniRedis.zadd("set", 6, "firstAdded");
        miniRedis.zadd("set", 2, "secondAdded");

        List<String> expectedResult = Arrays.asList("2) secondAdded", "6) firstAdded");

        Assertions.assertEquals(miniRedis.zrange("set", 1, 6), expectedResult);
    }

}