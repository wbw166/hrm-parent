package cn.itsource.hrm.client;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.hrm.client.impl.PageClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "STATIC-SERVICE",fallback = PageClientFallback.class)
public interface PageClient {

    @PostMapping("/staticPage")
    AjaxResult staticPage(@RequestParam("dataKey") String dataKey, @RequestParam("PageId") Long pageId);
}
