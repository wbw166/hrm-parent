package cn.itsource.hrm.client;

import cn.itsource.hrm.client.impl.RedisClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(path = "/redis",value = "REDIS-SERVICE",fallback = RedisClientFallback.class)
public interface RedisClient {
    @PostMapping("/set")
    public void set(@RequestParam("key") String key, @RequestParam("value") String value);

    @GetMapping("/get")
    public String get(@RequestParam("key") String key);

    @PostMapping("/setex")
    public void setex(@RequestParam("key") String key,@RequestParam("value") String value,@RequestParam("time") int time);
}
