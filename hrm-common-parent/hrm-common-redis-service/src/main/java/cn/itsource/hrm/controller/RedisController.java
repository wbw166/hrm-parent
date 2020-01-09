package cn.itsource.hrm.controller;

import cn.itsource.hrm.util.RedisUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @PostMapping("/set")
    public void set(String key,String value){
        RedisUtils.INSTANCE.set(key, value);
    }

    @GetMapping("/get")
    public String get(String key){
        return RedisUtils.INSTANCE.get(key);
    }

    @PostMapping("/setex")
    public void setex(String key,String value,int time){
        Jedis source = RedisUtils.INSTANCE.getSource();
        source.setex(key, time, value);
        source.close();
    }
}
