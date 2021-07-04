package com.hugovarani.miniredis;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Service
@Scope("singleton")
public class MiniRedisService {

    private ConcurrentHashMap<String, String> map;
    private ConcurrentHashMap<String, LinkedHashMap<Double, String>> zSet;

    public MiniRedisService(){
        map = new ConcurrentHashMap<>();
        zSet = new ConcurrentHashMap<>();
    }

    public String set(String key, String value, Integer time) {
        map.put(key, value);
        if (time != null){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    map.remove(key);
                }
            }, TimeUnit.SECONDS.toMillis(time));
        }
        return "OK";
    }

    public String get(String key) {
        if(!map.containsKey(key)){
            return "(nil)";
        }

        return map.get(key);
    }

    public Integer del(String... keys) {
        List<String> keysToRemove = Arrays.asList(keys);

        return keysToRemove.stream().map(key -> {
            if(map.containsKey(key)) {
                map.remove(key);
                return 1;
            } else {
                return 0;
            }
        }).reduce(0, Integer::sum);

    }

    public Integer dbSize(){
        return map.size();
    }

    public Integer incr(String key) {
        if(!map.containsKey(key)){
            map.put(key, "0");
        }

        try {
            int value = Integer.parseInt(map.get(key));
            value += 1;

            map.put(key, Integer.toString(value));
            return value;
        } catch (Exception e){
            return null;
        }
    }

    public Integer zadd(String key, Double score, String member){
        if (zSet.containsKey(key)){
            zSet.get(key).put(score, member);
            return 1;
        }

        LinkedHashMap<Double, String> newZSet = new LinkedHashMap<>();
        newZSet.put(score,member);
        zSet.put(key, newZSet);
        return 1;
    }

    public Integer zcard(String key){
        return zSet != null && zSet.containsKey(key) ? zSet.get(key).size() : 0;
    }
}
