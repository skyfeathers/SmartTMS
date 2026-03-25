package net.lab1024.tms.common.module.support.datatracer.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.support.datatracer.annoation.*;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerContentBO;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [ 对象对比 ]
 *
 * @author 罗伊
 */
@Slf4j
@Service
public class DataTracerFieldService {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DictCacheService dictCacheService;
    /**
     * 字段描述缓存
     */
    private ConcurrentHashMap<String, String> fieldDescCacheMap = new ConcurrentHashMap<>();

    /**
     * 类 加注解字段缓存
     */
    private ConcurrentHashMap<Class, List<Field>> fieldMap = new ConcurrentHashMap<>();

    /**
     * 数据批量对比
     * @param oldObjectList
     * @param newObjectList
     * @param <T>
     * @return
     */
    public <T> String beanListParse(List<T> oldObjectList, List<T> newObjectList) {
        boolean valid = this.valid(oldObjectList, newObjectList);
        if (!valid) {
            return "";
        }
        DataTracerOperateTypeEnum.Common operateType = this.getOperateType(oldObjectList, newObjectList);
        String operateContent = "";
        if (DataTracerOperateTypeEnum.Common.SAVE.equals(operateType) || DataTracerOperateTypeEnum.Common.DELETE.equals(operateType)) {
            operateContent = this.getObjectListContent(newObjectList);
            if (StringUtils.isEmpty(operateContent)) {
                return "";
            }
            return operateType.getDesc() + ":" + operateContent;
        }
        if (DataTracerOperateTypeEnum.Common.UPDATE.equals(operateType)) {
            return this.getUpdateContentList(oldObjectList, newObjectList);
        }
        return operateContent;
    }

    /**
     * 单个对象变动内容
     * @param oldObjectList
     * @param newObjectList
     * @param <T>
     * @return
     */
    private <T> String getUpdateContentList(List<T> oldObjectList, List<T> newObjectList) {
        String oldContent = this.getObjectListContent(oldObjectList);
        String newContent = this.getObjectListContent(newObjectList);
        if (oldContent.equals(newContent)) {
            return "";
        }
        if (StringUtils.isEmpty(oldContent) && StringUtils.isEmpty(newContent)) {
            return "";
        }
        return "【原数据】:<br/>" + oldContent + "<br/>" + "【新数据】:<br/>" + newContent;
    }

    /**
     * 获取一个对象的内容信息
     * @param objectList
     * @param <T>
     * @return
     */
    private <T> String getObjectListContent(List<T> objectList) {
        if (CollectionUtils.isEmpty(objectList)) {
            return "";
        }
        List<Field> fields = this.getField(objectList.get(0));
        List<String> contentList = Lists.newArrayList();
        for (Object objItem : objectList) {
            Map<String, DataTracerContentBO> beanParseMap = this.fieldParse(objItem, fields);
            contentList.add(this.getAddDeleteContent(beanParseMap));
        }
        return StringUtils.join(contentList, "<br/>");
    }


    /**
     * 解析多个对象的变更，删除，新增
     * oldObject 为空 ，newObject 不为空 为新增
     * oldObject 不为空 ，newObject 不空 为删除
     * 都不为空为编辑
     *
     * @param oldObject
     * @param newObject
     * @return
     */
    public String beanParse(Object oldObject, Object newObject) {
        boolean valid = this.valid(oldObject, newObject);
        if (!valid) {
            return null;
        }
        DataTracerOperateTypeEnum.Common operateType = this.getOperateType(oldObject, newObject);
        String operateContent = "";
        if (DataTracerOperateTypeEnum.Common.SAVE.equals(operateType) || DataTracerOperateTypeEnum.Common.DELETE.equals(operateType)) {
            operateContent = this.getAddDeleteContent(newObject);
        }
        if (DataTracerOperateTypeEnum.Common.UPDATE.equals(operateType)) {
            operateContent = this.getUpdateContent(oldObject, newObject);
        }
        if (StringUtils.isEmpty(operateContent)) {
            return "";
        }
        return operateType.getDesc() + ":" + operateContent;
    }

    /**
     * 解析单个bean的内容
     *
     * @param operateDesc
     * @param object
     * @return
     */
    public String beanObjectParse(String operateDesc, Object object) {
        String content = this.getAddDeleteContent(object);
        if (StringUtils.isEmpty(operateDesc)) {
            return content;
        }
        return operateDesc + ":" + content;
    }

    /**
     * 获取新增或删除操作内容
     *
     * @param object 新增或删除的对象
     * @return
     */
    private String getAddDeleteContent(Object object) {
        List<Field> fields = this.getField(object);
        Map<String, DataTracerContentBO> beanParseMap = this.fieldParse(object, fields);
        return this.getAddDeleteContent(beanParseMap);
    }

    private String getAddDeleteContent(Map<String, DataTracerContentBO> beanParseMap) {
        List<String> contentList = new ArrayList<>();
        for (Entry<String, DataTracerContentBO> entry : beanParseMap.entrySet()) {
            DataTracerContentBO dataTracerContentBO = entry.getValue();
            Boolean jsonFlag = JSONUtil.isTypeJSON(dataTracerContentBO.getFieldContent());
            String filedDesc = dataTracerContentBO.getFieldDesc();
            if(jsonFlag){
                contentList.add(filedDesc + "(请进入详情查看)");
            }else {
                contentList.add(dataTracerContentBO.getFieldDesc() + ":" + dataTracerContentBO.getFieldContent());
            }
        }
        String operateContent = StringUtils.join(contentList, "<br/>");
        if (StringUtils.isEmpty(operateContent)) {
            return "";
        }
        return operateContent;
    }


    /**
     * 获取更新操作内容
     *
     * @param oldObject
     * @param newObject
     * @return
     */
    private <T> String getUpdateContent(T oldObject, T newObject) {
        List<Field> fields = this.getField(oldObject);
        List<String> contentList = new ArrayList<>();
        Map<String, DataTracerContentBO> oldBeanParseMap = this.fieldParse(oldObject, fields);
        Map<String, DataTracerContentBO> newBeanParseMap = this.fieldParse(newObject, fields);
        //oldBeanParseMap与newBeanParseMap size一定相同
        for (Entry<String, DataTracerContentBO> entry : oldBeanParseMap.entrySet()) {
            String fieldName = entry.getKey();
            // 新旧对象内容
            DataTracerContentBO oldContentBO = entry.getValue();
            DataTracerContentBO newContentBO = newBeanParseMap.get(fieldName);
            // fieldContent
            String oldContent = oldContentBO.getFieldContent() == null ? "" : oldContentBO.getFieldContent();
            String newContent = newContentBO.getFieldContent() == null ? "" : newContentBO.getFieldContent();

            if (oldContent.equals(newContent)) {
                continue;
            }
            String fieldDesc = oldContentBO.getFieldDesc();
            Boolean jsonFlag = JSONUtil.isTypeJSON(oldContent) || JSONUtil.isTypeJSON(newContent);
            if (jsonFlag ) {
                String content = fieldDesc + "【进入详情查看】";
                contentList.add(content);
                continue;
            }
            String content = fieldDesc + ":" + "由【" + oldContent + "】变更为【" + newContent + "】";
            contentList.add(content);
        }
        if (CollectionUtils.isEmpty(contentList)) {
            return "";
        }
        String operateContent = StringUtils.join(contentList, "<br/>");
        if (StringUtils.isEmpty(operateContent)) {
            return "";
        }
        return operateContent;
    }


    /**
     * 接bean对象
     *
     * @param object
     * @param fields
     * @return <desc,value></>
     */
    private Map<String, DataTracerContentBO> fieldParse(Object object, List<Field> fields) {
        Map<String, DataTracerContentBO> objectParse = new HashMap<>(16);
        if (fields == null || fields.size() == 0) {
            return objectParse;
        }
        //对象解析结果
        for (Field field : fields) {
            field.setAccessible(true);
            String desc = this.getFieldDesc(field);
            if (StringUtils.isEmpty(desc)) {
                continue;
            }
            DataTracerContentBO dataTracerContentBO = this.getFieldValue(field, object);
            if(dataTracerContentBO != null){
                dataTracerContentBO.setFieldDesc(desc);
                objectParse.put(field.getName(), dataTracerContentBO);
            }
        }
        return objectParse;
    }

    /**
     * 获取字段值
     * @param field
     * @param object
     * @return
     */
    private DataTracerContentBO getFieldValue(Field field, Object object) {
        Object fieldValue = "";
        Class clazz = object.getClass();
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method get = pd.getReadMethod();
            fieldValue = get.invoke(object);
        } catch (Exception e) {
            log.error("bean operate log: reflect field value error " + field.getName());
            return null;
        }
        if (fieldValue == null) {
            return null;
        }

        String fieldContent = "";
        DataTracerFieldEnum dataTracerFieldEnum = field.getAnnotation(DataTracerFieldEnum.class);
        DataTracerFieldSql dataTracerFieldSql = field.getAnnotation(DataTracerFieldSql.class);
        DataTracerFieldDict dataTracerFieldDict = field.getAnnotation(DataTracerFieldDict.class);
        DataTracerFieldBoolean dataTracerFieldBoolean = field.getAnnotation(DataTracerFieldBoolean.class);
        if (dataTracerFieldEnum != null) {
            if (fieldValue instanceof Collection) {
                fieldContent = SmartBaseEnumUtil.getEnumDescByValueList((Collection) fieldValue, dataTracerFieldEnum.enumClass());
            } else {
                fieldContent = SmartBaseEnumUtil.getEnumDescByValue(fieldValue, dataTracerFieldEnum.enumClass());
            }
        } else if (dataTracerFieldDict != null) {
            fieldContent = dictCacheService.selectValueNameByValueCodeSplit(fieldValue.toString());
        } else if (dataTracerFieldSql != null) {
            fieldContent = this.getRelateDisplayValue(fieldValue, dataTracerFieldSql);
        } else if (dataTracerFieldBoolean != null) {
            fieldContent = this.getBooleanDesc(fieldValue, dataTracerFieldBoolean);

        } else if (fieldValue instanceof Date) {
            fieldContent = DateUtil.formatDateTime((Date) fieldValue);
        } else if (fieldValue instanceof LocalDateTime) {
            fieldContent = LocalDateTimeUtil.formatNormal((LocalDateTime) fieldValue);
        } else if (fieldValue instanceof LocalDate) {
            fieldContent = LocalDateTimeUtil.formatNormal((LocalDate) fieldValue);
        } else if (fieldValue instanceof BigDecimal) {
            DataTracerFieldBigDecimal dataTracerFieldBigDecimal = field.getAnnotation(DataTracerFieldBigDecimal.class);
            if (dataTracerFieldBigDecimal != null) {
                BigDecimal value = SmartBigDecimalUtil.setScale((BigDecimal) fieldValue, dataTracerFieldBigDecimal.scale());
                fieldContent = value.toString();
            } else {
                fieldContent = fieldValue.toString();
            }
        } else {
            fieldContent = JSON.toJSONString(fieldValue);
        }
        DataTracerContentBO dataTracerContentBO = new DataTracerContentBO();
        dataTracerContentBO.setField(field);
        dataTracerContentBO.setFieldValue(fieldValue);
        dataTracerContentBO.setFieldContent(fieldContent);
        return dataTracerContentBO;
    }

    /**
     * 获取关联字段的显示值
     *
     * @param fieldValue
     * @return
     */
    private String getRelateDisplayValue(Object fieldValue, DataTracerFieldSql dataTracerFieldSql) {
        Class<? extends BaseMapper> relateMapper = dataTracerFieldSql.relateMapper();
        BaseMapper mapper = applicationContext.getBean(relateMapper);
        if (mapper == null) {
            return "";
        }
        String relateFieldValue = fieldValue.toString();
        QueryWrapper qw = new QueryWrapper();
        qw.select(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, dataTracerFieldSql.relateDisplayColumn()));
        qw.in(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, dataTracerFieldSql.relateColumn()), relateFieldValue);
        List<Object> displayValue = mapper.selectObjs(qw);
        if (CollectionUtils.isEmpty(displayValue)) {
            return "";
        }
        return SmartStringUtil.join(",", displayValue);
    }

    /**
     * 获取Boolean状态的描述
     *
     * @param fieldValue
     * @param dataTracerFieldBoolean
     * @return
     */
    private String getBooleanDesc(Object fieldValue, DataTracerFieldBoolean dataTracerFieldBoolean) {
        String[] replace = dataTracerFieldBoolean.value();
        String[] temp;
        String value = fieldValue.toString();
        for (String str : replace) {
            temp = str.split("_");
            if (value.equals(temp[1])) {
                value = temp[0];
                break;
            }
        }
        return value;
    }


    /**
     * 获取字段描述信息 优先 OperateField 没得话swagger判断
     *
     * @param field
     * @return
     */
    private String getFieldDesc(Field field) {
        // 根据字段名称 从缓存中查询
        String fieldName = field.toGenericString();
        String desc = fieldDescCacheMap.get(fieldName);
        if (null != desc) {
            return desc;
        }
        DataTracerFieldDoc operateField = field.getAnnotation(DataTracerFieldDoc.class);
        if (operateField != null) {
            return operateField.value();
        }
        fieldDescCacheMap.put(fieldName, desc);
        return desc;
    }

    /**
     * 获取操作类型
     *
     * @param oldObject
     * @param newObject
     * @return
     */
    private  DataTracerOperateTypeEnum.Common getOperateType(Object oldObject, Object newObject) {
        if (oldObject == null && newObject != null) {
            return DataTracerOperateTypeEnum.Common.SAVE;
        }
        if (oldObject != null && newObject == null) {
            return  DataTracerOperateTypeEnum.Common.DELETE;
        }
        return DataTracerOperateTypeEnum.Common.UPDATE;
    }

    /**
     * 校验是否进行比对
     *
     * @param oldObject
     * @param newObject
     * @return
     */
    private boolean valid(Object oldObject, Object newObject) {
        if (oldObject == null && newObject == null) {
            return false;
        }
        if (oldObject == null && newObject != null) {
            return true;
        }
        if (oldObject != null && newObject == null) {
            return true;
        }
        if (oldObject != null && newObject != null) {
            String oldClass = oldObject.getClass().getName();
            String newClass = newObject.getClass().getName();
            return oldClass.equals(newClass);
        }
        return true;
    }


    /**
     * 校验
     * @param oldObjectList
     * @param newObjectList
     * @param <T>
     * @return
     */
    private <T> boolean valid(List<T> oldObjectList, List<T> newObjectList) {
        if (CollectionUtils.isEmpty(oldObjectList) && CollectionUtils.isEmpty(newObjectList)) {
            return false;
        }
        if (CollectionUtils.isEmpty(oldObjectList) && CollectionUtils.isNotEmpty(newObjectList)) {
            return true;
        }
        if (CollectionUtils.isNotEmpty(oldObjectList) && CollectionUtils.isEmpty(newObjectList)) {
            return true;
        }
        if (CollectionUtils.isNotEmpty(oldObjectList) && CollectionUtils.isNotEmpty(newObjectList)) {
            T oldObject = oldObjectList.get(0);
            T newObject = newObjectList.get(0);
            String oldClass = oldObject.getClass().getName();
            String newClass = newObject.getClass().getName();
            return oldClass.equals(newClass);
        }
        return true;
    }

    /**
     * 查询 包含 file key 注解的字段
     * 使用缓存
     *
     * @param obj
     * @return
     */
    private List<Field> getField(Object obj) {
        // 从缓存中查询
        Class tClass = obj.getClass();
        List<Field> fieldList = fieldMap.get(tClass);
        if (null != fieldList) {
            return fieldList;
        }

        // 这一段递归代码 是为了 从父类中获取属性
        Class tempClass = tClass;
        fieldList = new ArrayList<>();
        while (tempClass != null) {
            Field[] declaredFields = tempClass.getDeclaredFields();
            for (Field field : declaredFields) {
                // 过虑出有注解字段
                if (!field.isAnnotationPresent(DataTracerFieldDoc.class)) {
                    continue;
                }
                field.setAccessible(true);
                fieldList.add(field);
            }
            tempClass = tempClass.getSuperclass();
        }
        fieldMap.put(tClass, fieldList);
        return fieldList;
    }


}
