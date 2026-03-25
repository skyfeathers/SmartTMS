package net.lab1024.tms.common.constant;

/**
 * redis key 常量类
 *
 * @author zhuoda
 */
public class RedisKeyConst {

    public static final String SEPARATOR = ":";

    public static class Support {

        public static final String FILE_URL = "file:";

        public static final String FILE_VO = "file-vo:";

        public static final String LOCK = "lock:";

        public static final String SERIAL_NUMBER = LOCK + "serial-number:";

        public static final String SERIAL_NUMBER_LAST_INFO = LOCK + "serial-number:last-info";

        public static final String CAPTCHA = "captcha:";

        public static final String TOKEN = "token:";

        public static final String ID_GENERATOR = LOCK + "id:";

        public static final String SMS_VERIFICATION = "sms:verify:";
    }

    public static class BaiduOcr {

        private static final String BASE = "BAIDU:";

        public static final String TOKEN = BASE + "token";

    }

    public static class DING_DING {
        private static final String BASE = "DINGDING:";
        public static final String TOKEN = BASE + "token-%s:";
    }
}
