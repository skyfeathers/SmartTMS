package net.lab1024.tms.common.module.support.redis;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import net.lab1024.tms.common.common.domain.SystemEnvironment;
import net.lab1024.tms.common.common.enumeration.SystemEnvironmentEnum;
import net.lab1024.tms.common.constant.RedisKeyConst;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * [ redis 一顿操作 ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
@Component
public class RedisService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, String> redisValueOperations;

    @Autowired
    private HashOperations<String, String, Object> redisHashOperations;

    @Autowired
    private ListOperations<String, Object> redisListOperations;

    @Autowired
    private SetOperations<String, Object> redisSetOperations;

    @Autowired
    private SystemEnvironment systemEnvironment;


    /**
     * 生成redis key
     * @param prefix
     * @param key
     * @return
     */
    public String generateRedisKey(String prefix, String key) {
        SystemEnvironmentEnum currentEnvironment = systemEnvironment.getCurrentEnvironment();
        return systemEnvironment.getRedisProjectName() + RedisKeyConst.SEPARATOR + currentEnvironment.getValue() +  RedisKeyConst.SEPARATOR + prefix + key;
    }

    /**
     * redis key 解析成真实的内容
     * @param redisKey
     * @return
     */
    public static String redisKeyParse(String redisKey) {
        if(redisKey == null || redisKey.equals("")){
            return "";
        }
        int index = redisKey.lastIndexOf(RedisKeyConst.SEPARATOR);
        if(index < 1){
            return redisKey;
        }
        return redisKey.substring(index);
    }

    public boolean getLock(String key, long expire) {
        return redisValueOperations.setIfAbsent(key, String.valueOf(System.currentTimeMillis()), expire, TimeUnit.MILLISECONDS);
    }

    public void unLock(String key) {
        redisValueOperations.getOperations().delete(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取当天剩余的秒数
     *
     * @return
     */
    public static long currentDaySecond() {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param keyList
     */
    public void delete(List<String> keyList) {
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        redisTemplate.delete(keyList);
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : redisValueOperations.get(key);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object json = this.get(key);
        if (json == null) {
            return null;
        }
        T obj = JSON.parseObject(json.toString(), clazz);
        return obj;
    }


    /**
     * 普通缓存放入
     */
    public void set(String key, String value) {
        redisValueOperations.set(key, value);
    }
    public void set(Object key, Object value) {
        String jsonString = JSON.toJSONString(value);
        redisValueOperations.set(key.toString(), jsonString);
    }

    /**
     * 普通缓存放入
     */
    public void set(String key, String value, long second) {
        redisValueOperations.set(key, value, second, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入并设置时间
     */
    public void set(Object key, Object value, long time) {
        String jsonString = JSON.toJSONString(value);
        if (time > 0) {
            redisValueOperations.set(key.toString(), jsonString, time, TimeUnit.SECONDS);
        } else {
            set(key.toString(), jsonString);
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisListOperations.size(key);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisListOperations.index(key, index);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisListOperations.rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key
     * @return
     */
    public List<Object> lGetAll(String key) {
        return lGet(key, 0, -1);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisListOperations.range(key, start, end);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    //============================ map =============================
    public void mset(String key, String hashKey, Object value) {
        redisHashOperations.put(key, hashKey, value);
    }

    public Object mget(String key, String hashKey) {
        return redisHashOperations.get(key, hashKey);
    }

    /**
     * 根据前缀删除Key
     *
     * @param prefix
     */
    public void deleteByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (CollUtil.isEmpty(keys)) {
            return;
        }

        redisTemplate.delete(keys);
    }
}