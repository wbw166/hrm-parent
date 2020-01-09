package cn.itsource.hrm.client.impl;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.hrm.client.PageClient;
import org.springframework.stereotype.Component;

@Component
public class PageClientFallback implements PageClient {
    @Override
    public AjaxResult staticPage(String dataKey, Long pageId) {
        return AjaxResult.me().setSuccess(false).setMessage("系统繁忙!");
    }
}
