package com.simbest.cloud.cores.utils;


import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.simbest.cloud.cores.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.exception.Exceptions;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
/**
 * 用途： 对象工具类
 * 作者: lishuyi
 * 时间: 2018/6/7  0:38
 */
@Slf4j
public class ObjectUtil extends org.apache.commons.lang3.ObjectUtils {
    /**
     * 判断对象的所有属性是否均为空
     *
     * @param obj
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(obj)) {
            try{
                if (!pd.getName().equals("class")&& pd.getReadMethod()!=null && !pd.getReadMethod().getName().equals("isEmpty")) {
                    try {
                        Field f = getIndicateField(obj, pd.getName());
                        if (!f.isAnnotationPresent(Transient.class)) {
                            Object value = pd.getReadMethod().invoke(obj);
                            if (value instanceof Collection) {
                                if (((Collection<?>) value).size() != 0)
                                    return false;
                            } else if (value instanceof Map) {
                                if (((Map<?, ?>) value).size() != 0)
                                    return false;
                            } else if (value != null)
                                return false;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return false;
                    }
                }
            }catch(Exception e){
                Exceptions.printException(e);
            }
        }
        return true;
    }

    /**
     * 获取持久化对象的主键Id字段
     *
     * @param obj
     * @return Field
     */
    public static Field getEntityIdField(Object obj) {
        Field id = null;
        for (Field field : getAllFields(obj.getClass())) {
            if (field.isAnnotationPresent(Id.class)) {
                id = field;
                break;
            }
        }
        return id;
    }

    public static Field getEntityIdField(String objName) {
        Field id = null;
        try {
            for (Field field : getAllFields(Class.forName(objName))) {
                if (field.isAnnotationPresent(Id.class)) {
                    id = field;
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
           log.error("【{}】ClassNotFoundException", objName);
        }
        return id;
    }

    /**
     * 获取持久化对象的主键Id字段值
     * @param obj
     * @return Object
     */
    public static Object getEntityIdVaue(Object obj) {
        try {
            Field id = getEntityIdField(obj);
            id.setAccessible(true);
            Object idValue = id.get(obj);
            if(null != idValue && idValue instanceof String){
                if(StringUtils.isEmpty(idValue.toString())){
                    idValue = null;
                }
            }
            return idValue;
        } catch (IllegalAccessException e) {
            Exceptions.printException(e);
        }
        return null;
    }

    /**
     * 设置持久化对象的主键值
     * @param obj
     * @param value
     */
    public static void setEntityIdVaue(Object obj, Object value) {
        try {
            Field id = getEntityIdField(obj);
            id.setAccessible(true);
            id.set(obj, value);
        } catch (IllegalAccessException e) {
            Exceptions.printException(e);
        }
    }

    /**
     * 获取数据库主键EntityIdPrefix的定义前缀值
     * @param obj
     * @return String
     */
    public static String getEntityIdPrefixVaue(Object obj) {
        Field id = getEntityIdField(obj);
        if (id.isAnnotationPresent(EntityIdPrefix.class)){
            EntityIdPrefix prefix = id.getAnnotation(EntityIdPrefix.class);
            return prefix.prefix();
        }
        return null;
    }

    /**
     * 获取被Column标注的持久化字段
     * @param obj
     * @return Set<Field>
     */
    public static Set<Field> getEntityPersistentFieldExceptId(Object obj) {
        Set<Field> fields = Sets.newHashSet();
        for (Field field : getAllFields(obj.getClass())) {
            if (field.isAnnotationPresent(Column.class)) {
                fields.add(field);
            }
            if (field.isAnnotationPresent(JoinColumn.class)) {
                fields.add(field);
            }
        }
        //获取框架基类中的字段
        for (Field field : getAllFields(obj.getClass().getSuperclass())) {
            if (field.isAnnotationPresent(Column.class)) {
                fields.add(field);
            }
            if (field.isAnnotationPresent(JoinColumn.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * 获取被Column标注的持久化字段值
     * @param obj
     * @return Map<String, Object>
     */
    public static Map<String, Object> getEntityPersistentFieldValueExceptId(Object obj) {
        Map<String, Object> persistentFieldValues = Maps.newHashMap();
        Set<Field> persistentFields = getEntityPersistentFieldExceptId(obj);
        final BeanWrapper src = new BeanWrapperImpl(obj);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> propertyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Field f = getIndicateField(obj, pd.getName());
            if(persistentFields.contains(f)) {
                if(src.isReadableProperty(pd.getName())){
                    try {
                        Object value = src.getPropertyValue(pd.getName());
                        if (value != null) {
                            //字符串属性只读取非空的值
                            if (value instanceof String) {
//                        String str = (String)value;
                                if (StringUtils.isNotEmpty(value.toString())) {
                                    persistentFieldValues.put(pd.getName(), value);
                                }
                            } else {
                                persistentFieldValues.put(pd.getName(), value);
                            }
                        }
                    }catch (Exception e){
                       log.warn("读取属性【{}】失败", pd.getName());
                    }
                }
            }
        }
        return persistentFieldValues;
    }

    /**
     * 获取指定字段的Field
     * @param obj
     * @param fieldName
     * @return Field
     */
    public static Field getIndicateField(Object obj, String fieldName) {
        Field indicateField = null;
        for (Field field : getAllFields(obj.getClass())) {
            if (field.getName().equals(fieldName)) {
                indicateField = field;
                break;
            }
        }
        return indicateField;
    }

    /**
     * 获取被Transient标注的非持久化字段
     * @param obj
     * @return Set<Field>
     */
    public static Set<Field> getEntityTransientField(Object obj) {
        Set<Field> fields = Sets.newHashSet();
        for (Field field : getAllFields(obj.getClass())) {
            if (field.isAnnotationPresent(Transient.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * 返回所有字段
     */
    public static Field[] getAllFields(Class<?> clazz) {
        Collection<Class<?>> classes = getAllSuperClasses(clazz);
        classes.add(clazz);
        return getAllFields(classes);
    }

    /**
     * 返回所有字段
     */
    private static Field[] getAllFields(Collection<Class<?>> classes) {
        Set<Field> fields = Sets.newHashSet();
        for (Class<?> clazz : classes) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * 返回所有超类
     */
    public static Collection<Class<?>> getAllSuperClasses(Class<?> clazz) {
        Set<Class<?>> classes = Sets.newHashSet();
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }

        return classes;
    }
}