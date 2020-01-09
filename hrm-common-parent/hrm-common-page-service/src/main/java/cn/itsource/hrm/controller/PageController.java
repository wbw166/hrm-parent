package cn.itsource.hrm.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.FileUtil;
import cn.itsource.hrm.client.FileClient;
import cn.itsource.hrm.client.PageInfoClient;
import cn.itsource.hrm.client.RedisClient;
import cn.itsource.hrm.domain.PageInfo;
import cn.itsource.hrm.util.VelocityUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import feign.Response;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PageController {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private PageInfoClient pageInfoClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 页面静态化接口
     * @param dataKey
     * @param pageId
     * @return
     */
    @PostMapping("/staticPage")
    public AjaxResult staticPage(@RequestParam("dataKey") String dataKey,@RequestParam("PageId") Long pageId){

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            //从redis中获取数据
            String dataStr = redisClient.get(dataKey);
            JSONArray data = JSONObject.parseArray(dataStr);
            //下载模板
            PageInfo pageInfo = pageInfoClient.get(pageId);
            String templateUrl = pageInfo.getTemplateUrl();
            String templateFile = pageInfo.getTemplateFile();

            //通过feign下载文件
            Response response = fileClient.download(templateUrl);
            Response.Body body = response.body();

            String templatePath = "f:/temp1/template.zip";//文件下载到该路径下
            String templateDir = "f:/temp1/";//解压到该路径下
            //获取到输入流
            inputStream = body.asInputStream();
            //文件下载到哪儿 创建文件输出流
            outputStream = new FileOutputStream(new File(templatePath));
            //apache提供的IO操作工具  commons-io包中
            IOUtils.copy(inputStream, outputStream);
            //解压
            FileUtil.unZipFiles(new File(templatePath), templateDir);

            //静态化页面
            //模板+数据 ->文件
            //staticRoot - 模板的根目录
            //courseTypes - 数据【集合】
            HashMap<String, Object> model = new HashMap<>();
            model.put("staticRoot", templateDir);
            model.put("courseTypes", data);

            String templateFilePathAndName = templateDir + templateFile;
            String targetFilePathAndName = "f:/temp1/home.html";
            VelocityUtils.staticByTemplate(model, templateFilePathAndName, targetFilePathAndName);
            System.out.println("页面静态化成功!");

            //上传文件到fastdfs
            FileItem fileItem = createFileItem(new File(targetFilePathAndName));
            CommonsMultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            AjaxResult ajaxResult = fileClient.feignUpload(multipartFile);
            String fileId = ajaxResult.getResultObj().toString();
            //发送消息到mq
            //msg  {pageId:1,fileId:"xxxxx",physicalPath:"xxxxx"}
            Map<String,Object> msg = new HashMap<>();
            msg.put("pageId", pageId);
            msg.put("fileId", fileId);
            msg.put("physicalPath", pageInfo.getPhysicalPath());
            String msgStr = JSONObject.toJSONString(msg);
            rabbitTemplate.convertAndSend("hrm-course", msgStr);
            return AjaxResult.me();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AjaxResult.me().setSuccess(false).setMessage("失败!");
    }

    private FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        FileItem item = factory.createItem("file", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }
}
