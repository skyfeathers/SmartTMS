package net.lab1024.tms.fixedasset.common.constants;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.util.*;

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

    /**
     * 空MaP
     */
    public static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>(0));

    /**
     * 空 list
     */
    public static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>(0));


    public static final Interner<String> STRING_POOL = Interners.newWeakInterner();

}
