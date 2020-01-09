package cn.itsource.hrm.service.impl;

import cn.itsource.hrm.client.RedisClient;
import cn.itsource.hrm.service.ICodeService;
import cn.itsource.hrm.util.Base64Utils;
import cn.itsource.hrm.util.ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CodeServiceImpl implements ICodeService {

    @Autowired
    private RedisClient redisClient;

    @Override
    public String genCode(String key) {
        //生成随机字符串
        int width  = 200;
        int height = 50;
        int codeCount = 4;
        int lineCount = 150;
        ImageUtil imageUtil = new ImageUtil(width, height, codeCount, lineCount);
        String code = imageUtil.getCode();
        //将图片验证码包存到redis中 过期时间三分钟
        redisClient.setex(key, code, 3*60);

        ByteArrayOutputStream outputStream = null;
        try {
            //生成图片
            outputStream = new ByteArrayOutputStream();
            imageUtil.write(outputStream);
            //用工具类将图片编码 base64
            byte[] bytes = outputStream.toByteArray();
            String base64Str = Base64Utils.encodeToString(bytes);
            return base64Str;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 图片验证码的验证
     * @param key
     * @param code
     * @return
     */
    @Override
    public boolean validateCode(String key, String code) {
        String value = redisClient.get(key);
        if(StringUtils.isNotEmpty(code)){
            return code.equals(value);
        }
        return false;
    }
}
