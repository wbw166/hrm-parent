package cn.itsource.hrm.service.impl;

import cn.itsource.basic.util.StrUtils;
import cn.itsource.hrm.client.RedisClient;
import cn.itsource.hrm.service.ISmsService;
import cn.itsource.hrm.util.SMSUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImp implements ISmsService {

    @Autowired
    private RedisClient redisClient;

    /**
     * 发送短信验证码并做相应的判断
     * @param phone
     */
    @Override
    public void sendSMS(String phone) {
        //通过固定的key 判断是否能从redis中取出验证码
        String key = "SMSCODE:"+phone+":REG";
        String value = redisClient.get(key);
        String code = null;
        if(StringUtils.isNotEmpty(value)){
            //如果redis中存在 就以逗号分隔取到对应的值做判断
            String[] split = value.split(",");
            code = split[0];//取出验证码
            Long time = Long.valueOf(split[1]);//取出重发时间
            if(System.currentTimeMillis()-time>0){//因为在保存重发时间是当前时间加上30秒
                //已过重发时间 可以重新发送 重新设置重发时间
                time = System.currentTimeMillis() + 30*1000;
                value = code + "," + time;
            }else {
                //如果没有过重发时间 就直接抛出异常
                throw new RuntimeException("请稍后重试!");
            }

        }else {
            //redis中没有 则通过工具类生成验证码
            code = StrUtils.getRandomString(6);
            value = code + "," + System.currentTimeMillis() + 30*1000;
        }
        //保存到redis中设置过期时间
        redisClient.setex(key, value, 3*60);
        //调用短信接口发送
        SMSUtils.send(phone, code);
    }

}
