package net.lab1024.tms.common.common.constant;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

/**
 * [ 通用常量 ]
 *
 * @author 卓大
 */
public class StringConst {

    /**
     * 全局通用分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 全局通用分隔符 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 全局通用 横杠
     */
    public static final String HORIZONTAL = "-";

    /**
     * 全局通用分隔符
     */
    public static final Character SEPARATOR_CHAR = ',';

    /**
     * 全局通用分隔符 斜杠
     */
    public static final String SEPARATOR_SLASH = "/";

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 换行
     */
    public static final String LINE = "\n";

    public static final Interner<String> STRING_POOL = Interners.newWeakInterner();

}
