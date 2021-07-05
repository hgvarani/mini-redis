package com.hugovarani.miniredis;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Scope("singleton")
public class MiniRedisService {

    private final ConcurrentHashMap<String, String> map;
    private final ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> zSet;

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
            map.putIfAbsent(key, "0");
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

    public Integer zadd(String key, Integer score, String member){
        if (zSet.containsKey(key)){
            zSet.get(key).put(score, member);
            return 1;
        } else {
            ConcurrentHashMap<Integer, String> newZSet = new ConcurrentHashMap<>();
            newZSet.put(score,member);
            zSet.put(key, newZSet);
        }
        return 1;
    }

    public Integer zcard(String key){
        return zSet.containsKey(key) ? zSet.get(key).size() : 0;
    }

    public List<String> zrange(String key, Integer min, Integer max){
        if(!zSet.containsKey(key)){
            return Collections.emptyList();
        }

        return zSet.get(key).entrySet()
                .stream()
                .filter(entry -> entry.getKey() >= min && entry.getKey() <= max)
                .sorted(Map.Entry.comparingByKey())
                .map(elements -> String.format("%s) %s", elements.getKey(), elements.getValue()))
                .collect(Collectors.toList());

    }
}
