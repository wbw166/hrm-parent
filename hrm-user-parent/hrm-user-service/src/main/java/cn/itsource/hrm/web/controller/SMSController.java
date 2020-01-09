package cn.itsource.hrm.web.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.hrm.service.ICodeService;
import cn.itsource.hrm.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sms")
@RestController
public class SMSController {
    @Autowired
    private ICodeService codeService;

    @Autowired
    private ISmsService smsService;

    @RequestMapping("/send")
    public AjaxResult sendSMS(String key,String code,String phone){
        //验证图片验证码
        boolean flag = codeService.validateCode(key, code);
        if(!flag){
            return AjaxResult.me().setSuccess(false).setMessage("验证码错误!");
        }
        //发送短信验证码
        if(StringUtils.isEmpty(phone)){
            return AjaxResult.me().setSuccess(false).setMessage("请输入正确的手机号码!");
        }
        try {
            smsService.sendSMS(phone);
            return AjaxResult.me().setSuccess(true).setMessage("发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("发送失败 请稍后重试!");
        }
    }
}
