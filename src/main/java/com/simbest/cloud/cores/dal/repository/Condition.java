/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.repository;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用途：查询条件
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:51
 */
public class Condition {

    // 等于
    @Getter
    private Map<String, Object> eq = new HashMap<>();

    // 不等于
    @Getter
    private Map<String, Object> neq = new HashMap<>();

    // 大于
    @Getter
    private Map<String, Number> gt = new HashMap<>();

    // 小于
    @Getter
    private Map<String, Number> lt = new HashMap<>();

    // 相似
    @Getter
    private Map<String, String> like = new HashMap<>();

    // 在其中
    @Getter
    private Map<String, List<Object>> in = new HashMap<>();

    // 不在其中
    @Getter
    private Map<String, List<Object>> notIn = new HashMap<>();

    public void eq(String key, Object value) {
        if (value != null) {
            eq.put(key, value);
        }
    }

    public void neq(String key, Object value) {
        if (value != null) {
            neq.put(key, value);
        }
    }

    public void gt(String key, Number value) {
        if (value != null) {
            gt.put(key, value);
        }
    }

    public void lt(String key, Number value) {
        if (value != null) {
            lt.put(key, value);
        }
    }

    public void like(String key, String value) {
        if (value != null) {
            like.put(key, value);
        }
    }

    public void in(String key, Object value) {
        if (key != null && value != null) {
            List<Object> list = in.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(value);
        }
    }

    public void notIn(String key, Object value) {
        if (key != null && value != null) {
            List<Object> list = notIn.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(value);
        }
    }
}
