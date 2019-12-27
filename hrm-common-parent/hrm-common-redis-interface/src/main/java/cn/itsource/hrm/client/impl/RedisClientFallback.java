package cn.itsource.hrm.client.impl;

import cn.itsource.hrm.client.RedisClient;
import org.springframework.stereotype.Component;

@Component
public class RedisClientFallback implements RedisClient {
    @Override
    public void set(String key, String value) {

    }

    @Override
    public String get(String key) {
        return "系统繁忙！";
    }
}
