package com.hugovarani.miniredis;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Data
@Service
@Scope("singleton")
public class MiniRedisService {

    private ConcurrentHashMap<String, String> map;

    public MiniRedisService(){
        map = new ConcurrentHashMap<>();
    }

    public String set(String key, String value) {
        map.put(key, value);
        return "OK";
    }

    public String get(String key) {
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
}
