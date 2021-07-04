package com.hugovarani.miniredis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

}