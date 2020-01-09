package cn.itsource.hrm.client;

import cn.itsource.hrm.domain.Systemdictionaryitem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(path = "/systemdictionaryitem",value = "SYSTEM-SERVICE")
public interface SystemdictionaryitemClient {

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    Systemdictionaryitem get(@PathVariable("id")Long id);
}
