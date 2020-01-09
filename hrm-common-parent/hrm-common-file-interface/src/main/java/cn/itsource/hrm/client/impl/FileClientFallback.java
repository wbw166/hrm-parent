package cn.itsource.hrm.client.impl;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.hrm.client.FileClient;
import feign.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileClientFallback implements FileClient {
    @Override
    public Response download(String file_id) {
        return null;
    }

    @Override
    public AjaxResult feignUpload(MultipartFile file) {
        return AjaxResult.me().setSuccess(false).setMessage("失败!");
    }
}
