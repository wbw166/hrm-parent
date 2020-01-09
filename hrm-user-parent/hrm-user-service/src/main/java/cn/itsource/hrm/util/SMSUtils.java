package cn.itsource.hrm.util;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

import java.util.HashMap;
import java.util.Set;

public class SMSUtils {

    private static final String SERVER_IP = "sandboxapp.cloopen.com";
    private static final String PORT = "8883";
    private static final String ACCOUNT_ID = "8a216da86f696570016f8a890c0711ea";
    private static final String TOKEN = "a4a0c4f1d26d4520b0063be8241f5a13";
    private static final String APP_ID = "8a216da86f696570016f8a890c6811f0";
    private static final String TEMPLATE_ID = "1";


    /**
     * 发送手机验证码
     * @param telephone
     * @param code
     * @return
     */
    public static boolean send(String telephone,String code){
        HashMap<String, Object> result = null;
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init(SERVER_IP, PORT);
        // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
        restAPI.setAccount(ACCOUNT_ID, TOKEN);
        // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
        restAPI.setAppId(APP_ID);
        // 请使用管理控制台中已创建应用的APPID。
        //【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入。
        result = restAPI.sendTemplateSMS(telephone,TEMPLATE_ID ,new String[]{code,"3"});
        System.out.println("SDKTestGetSubAccounts result=" + result);//响应
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
            return true;
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
            return false;
        }
    }

}
