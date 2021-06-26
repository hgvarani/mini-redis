package test;

import main.MiniRedis;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MiniRedisTest {

    private static MiniRedis miniRedis;

    @BeforeAll
    static void before(){
        miniRedis = new MiniRedis();
    }

    @Test
    void shouldSetACorrectKey(){
        Assertions.assertEquals(miniRedis.set("myKey", "myValue"), "OK");
    }

    @Test
    void setShouldThrowNPEWhenKeyIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> miniRedis.set(null, "myValue"));
    }

    @Test
    void setShouldThrowNPEWhenValueIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> miniRedis.set("myKey", null));
    }

    @Test
    void shouldGetExistingValueByKey(){
        miniRedis.set("myKey", "myValue");
        Assertions.assertEquals(miniRedis.get("myKey"), "myValue");
    }

    @Test
    void shouldGetNullValueByNonExistingKey(){
        miniRedis.set("myKey", "myValue");
        Assertions.assertNull(miniRedis.get("anotherKey"));
    }

    @Test
    void delShouldRemoveAllExistingElements(){
        miniRedis.set("a","a");
        miniRedis.set("b","b");
        miniRedis.set("c","c");
        miniRedis.set("d","d");

        Assertions.assertEquals(miniRedis.del("a", "b", "c", "d"), 4);
    }

    @Test
    void delShouldRemoveOnlyExistingElements(){
        miniRedis.set("b","b");
        miniRedis.set("c","c");
        miniRedis.set("d","d");

        Assertions.assertEquals(miniRedis.del("a", "b", "c", "d"), 3);
    }

    @Test
    void dbSizeShouldCountCorrectlyAfterRemovingValues(){
        miniRedis.set("b","b");
        miniRedis.set("c","c");
        miniRedis.set("d","d");

        Assertions.assertEquals(miniRedis.dbSize(), 3);

        miniRedis.del("b");
        Assertions.assertEquals(miniRedis.dbSize(), 2);
    }

    @Test
    void incrShouldIncrementExistingValue(){
        miniRedis.set("a", "5");

        Assertions.assertEquals(miniRedis.incr("a"), 6);
    }

    @Test
    void incrShouldReturnOneIfValueDOesNotExist(){
        Assertions.assertEquals(miniRedis.incr("a"), 1);
    }

    @Test
    void shouldReturnNullIfValueIsInvalid(){
        miniRedis.set("a", "a");

        Assertions.assertNull(miniRedis.incr("a"));
    }


}