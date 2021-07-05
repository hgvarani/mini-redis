package com.hugovarani.miniredis;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MiniRedisController {

    private final MiniRedisService miniRedis;

    public MiniRedisController(MiniRedisService miniRedisService) {
        miniRedis = miniRedisService;
    }

    @PostMapping("/set")
    public String set(@RequestParam(value = "key") String key,
                      @RequestParam(value = "value") String value,
                      @RequestParam(value = "time", required = false) Integer time){
        return miniRedis.set(key, value, time);
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

    @PostMapping("/zadd/{key}")
    public Integer zadd(@PathVariable String key,
                        @RequestParam(value = "score") Integer score,
                        @RequestParam(value = "member") String member){
        return miniRedis.zadd(key, score, member);
    }

    @GetMapping("/zcard")
    public Integer zcard(@RequestParam(value = "key") String key){
        return miniRedis.zcard(key);
    }

    @GetMapping("zrange/{key}")
    public List<String> zrange(@PathVariable String key,
                               @RequestParam(value = "min") Integer min,
                               @RequestParam(value = "max") Integer max){
        return miniRedis.zrange(key, min, max);
    }
}
