package cn.itsource.hrm.client;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.hrm.client.impl.FileClientFallback;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(path = "/fastdfs",value = "FILE-SERVICE",fallback = FileClientFallback.class)
public interface FileClient {
    @RequestMapping(value = "/downloadFile",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Response download(@RequestParam("fileId")String file_id);

    @RequestMapping(value = "/feignUpload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    AjaxResult feignUpload(@RequestPart(value = "file")MultipartFile file);
}
