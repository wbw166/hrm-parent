package cn.itsource.hrm.handler;

import cn.itsource.hrm.client.FileClient;
import cn.itsource.hrm.config.RabbitmqConfig;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class ReceiveHandler {

    @Autowired
    private FileClient fileClient;

    //监听队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SITE})
    public void receive_email(String msg, Message message, Channel channel) {

        //下载文件
        //解析msg  {pageId:1,fileId:"xxxxx",physicalPath:"xxxxx"}
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String fileId = jsonObject.getString("fileId");
        String physicalPath = jsonObject.getString("physicalPath");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            Response response = fileClient.download(fileId);
            inputStream = response.body().asInputStream();

            outputStream = new FileOutputStream(physicalPath);
            IOUtils.copy(inputStream, outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if(outputStream!=null)
                    outputStream.close();
                if(inputStream!=null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


}
