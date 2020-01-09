package cn.itsource.hrm.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.Charset;

public class Base64Utils {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static final Base64Delegate default_delegate = new CommonsCodecBase64Delegate();

    public Base64Utils() {
    }
    /**
     * 内部定义接口
     */
    private static interface Base64Delegate {

        public abstract byte[] encode(byte src[]);

        public abstract byte[] decode(byte src[]);
    }

    /**
     * 内部接口实现
     * @author lz
     *
     */
    static class CommonsCodecBase64Delegate implements Base64Delegate {

        private final Base64 base64;

        public byte[] encode(byte src[]) {
            return base64.encode(src);
        }

        public byte[] decode(byte src[]) {
            return base64.decode(src);
        }


        private CommonsCodecBase64Delegate() {
            base64 = new Base64();
        }

    }
    /**
     * base64 编码
     * @param src
     * @return
     */
    public static String encodeToString(byte src[]) {
        if (src == null){
            return null;
        }
        if (src.length == 0)
            return "";
        else
            return new String(default_delegate.encode(src), DEFAULT_CHARSET);
    }
    /**
     * Base64 解码,返回String
     * @param base64Str
     * @return
     */
    public static String decodeToString(String base64Str) {
        if (base64Str == null)
            return null;
        if (base64Str.length() == 0)
            return "";
        else
            return new String(default_delegate.decode(base64Str.getBytes()),
                    DEFAULT_CHARSET);
    }
    /**
     * Base64 解码,返回byte[]
     * @param base64Str
     * @return
     */
    public static byte[] decodeToByte(String base64Str) {
        if (base64Str == null)
            return null;
        if (base64Str.length() == 0)
            return null;
        else
            return default_delegate.decode(base64Str.getBytes());
    }
}
