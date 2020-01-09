package cn.itsource.hrm.web.controller;

import cn.itsource.hrm.service.ICodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class CodeController {

    @Autowired
    private ICodeService codeService;

    /**
     * 生成图片验证码
     * @param key
     * @return
     */
    @RequestMapping("/genCode")
    public String genCode(String key){
        return codeService.genCode(key);

    }
}
