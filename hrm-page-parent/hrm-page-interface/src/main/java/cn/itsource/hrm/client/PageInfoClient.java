package cn.itsource.hrm.client;

import cn.itsource.hrm.client.impl.PageInfoFallback;
import cn.itsource.hrm.domain.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "PAGE-SERVICE",fallback = PageInfoFallback.class)
public interface PageInfoClient {

    @RequestMapping(value = "/pageInfo/{id}",method = RequestMethod.GET)
    PageInfo get(@PathVariable("id")Long id);
}
