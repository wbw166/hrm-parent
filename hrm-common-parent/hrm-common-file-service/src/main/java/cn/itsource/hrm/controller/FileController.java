package cn.itsource.hrm.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.hrm.util.FastDfsApiOpr;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/fastdfs")
public class FileController {
    /**
     * 文件上传  MultipartFile 是SpringMVC封装好的API方便操作上传的文件的
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public AjaxResult file_upload(MultipartFile file){
        try {
            //获取文件的扩展名
            String file_id = upload(file);
            return AjaxResult.me().setResultObj(file_id);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上传失败!"+e.getMessage());
        }

    }
    @DeleteMapping("/delete")
    public AjaxResult delete(String file_id){
        /*try {
            //     /group1/M00/00/01/rBACgF1euQiAEdrcAACbjnUEnrE193.jpg

            file_id = file_id.substring(1);//   group1/M00/00/01/rBACgF1euQiAEdrcAACbjnUEnrE193.jpg

            String groupName = file_id.substring(0, file_id.indexOf("/"));
            //   group1

            String fileName = file_id.substring(file_id.indexOf("/")+1);//   M00/00/01/rBACgF1euQiAEdrcAACbjnUEnrE193.jpg

            FastDfsApiOpr.delete(groupName, fileName);

            return AjaxResult.me();

        } catch (Exception e) {
            e.printStackTrace();

            return AjaxResult.me().setSuccess(false).setMessage("删除失败!");

        }*/
        try {
            file_id = file_id.substring(1);
            int index = file_id.indexOf("/");
            String groupName = file_id.substring(0,index);
            String fileName = file_id.substring(index+1);
            FastDfsApiOpr.delete(groupName,fileName);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败!"+e.getMessage());
        }
    }

    /**
     * 文件下载
     * @param response
     */
    @RequestMapping(value = "/downloadFile",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void download(@RequestParam("fileId")String file_id, HttpServletResponse response){
        file_id = file_id.substring(1);//去掉第一个  /
        int index = file_id.indexOf("/");
        String groupName = file_id.substring(0,index);
        String fileName = file_id.substring(index+1);
        byte[] bytes = FastDfsApiOpr.download(groupName, fileName);

        try {
            OutputStream out = response.getOutputStream();
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件上传 - feign
     * feign 实现文件上传
     * @return
     */
    @RequestMapping(value = "/feignUpload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult feignUpload(@RequestPart(value = "file")MultipartFile file){
        try {
            String file_id = upload(file);
            return AjaxResult.me().setResultObj(file_id);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上传失败!"+e.getMessage());
        }
    }

    private String upload(MultipartFile file)throws IOException{
        String fileName = file.getName();
        String originalFilename = file.getOriginalFilename();

        //文件后缀名
        int index = originalFilename.lastIndexOf(".");
        String extName = originalFilename.substring(index + 1);


        return FastDfsApiOpr.upload(file.getBytes(), extName);

    }
}
