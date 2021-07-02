package com.hugovarani.miniredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
public class MiniRedisController {

    @Autowired
    private final MiniRedisService miniRedis = new MiniRedisService();

    @PostMapping("/set")
    public String set(@RequestParam(value = "key") String key,
                      @RequestParam(value = "value") String value){
        return miniRedis.set(key, value);
    }

    @GetMapping("/get/{key}")
    public String get(@PathVariable String key){
        return miniRedis.get(key);
    }

    @DeleteMapping("/del")
    public Integer del(@RequestParam(value = "keys") String... keys){
        return miniRedis.del(keys);
    }

    @GetMapping("/dbsize")
    public Integer dbSize(){
        return miniRedis.dbSize();
    }

    @PostMapping("/incr/{key}")
    public Integer incr(@PathVariable String key){
        return miniRedis.incr(key);
    }
}
