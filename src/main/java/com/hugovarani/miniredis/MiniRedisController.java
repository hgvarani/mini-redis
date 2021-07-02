package com.hugovarani.miniredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
public class MiniRedisController {

    @Autowired
    private final MiniRedisService miniRedis = new MiniRedisService();

    @GetMapping("/GET/{key}")
    public String get(@PathVariable String key){
        return miniRedis.get(key);
    }

    @PostMapping("/SET")
    public String set(@RequestParam(value = "key") String key,
                      @RequestParam(value = "value") String value){
        return miniRedis.set(key, value);
    }
}
