package net.lab1024.tms.admin.common.constants;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/9/14 15:48
 */
public class CommonConst {

    /**
     * 全局通用分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 全局通用分隔符
     */
    public static final Character SEPARATOR_CHAR = ',';


    public static final String IGNORE_H5_URL_MAPPING = "/h5/api";

    public static final class FileServiceConst {

        /**
         * 公用读取文件夹 public
         */
        public static final String FOLDER_PUBLIC = "pu";

        /**
         * 私有读取文件夹 private
         */
        public static final String FOLDER_PRIVATE = "pr";

    }

    public static final class System {

        public static final String SA_DEFAULT_TOKEN = "employeeId";
        /**
         * oss url redis 过期时间
         */
        public static final int FILE_URL_EXPIRE_SECOND = 3600;

        public static final int FILE_VO_EXPIRE_SECOND = 86400;
    }

    /**
     * 长度类常量
     */
    public static final class NumberLimit {
        /**
         * 文件名称长度
         */
        public static final int FILE_NAME = 100;

    }

    public static final class Password {

        public static final String DEFAULT = "123456";

        public static final String DEFAULT_LETTER = "abcdefghijklmnopqrstuvwsxyz";

        public static final String DEFAULT_UPPERCASE_LETTER = "ABCDEFGHIJKLMNOPQRSTUVWSXYZ";

    }

    public static final class CommonCollection {

        public static final Set<String> IGNORE_URL = ImmutableSet.of("/swagger", "Excel");

        public static final Set<String> IGNORE_URL_MAPPING = ImmutableSet.of(IGNORE_H5_URL_MAPPING);

        public static Boolean contain(Set<String> ignores, String uri) {
            if (CollectionUtils.isEmpty(ignores)) {
                return false;
            }
            for (String ignoreUrl : ignores) {
                if (uri.startsWith(ignoreUrl)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static final Interner<String> STRING_POOL = Interners.newWeakInterner();

}
