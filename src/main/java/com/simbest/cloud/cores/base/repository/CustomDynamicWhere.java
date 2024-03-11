package com.simbest.cloud.cores.base.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CustomDynamicWhere implements Serializable {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 根据自定义进行原生的sql查询，无参数
     * @param sql                       执行的sql语句
     * @return List
     */
    public List<Map<String, Object>> queryForList(String sql){
        log.warn("自定义查询SQL输出为：【{}】",sql );
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 根据自定义的动态参数进行原生的sql查询
     * @param sql                       执行的sql语句
     * @param params                    注入的参数  为占位符 ？
     * @return List
     */
    public List<Map<String, Object>> queryForList(String sql,Object[] params){
        log.warn("自定义查询SQL输出为：【{}】",sql );
        return jdbcTemplate.queryForList(sql,params);
    }

    /**
     * 根据自定义的动态参数进行原生的sql查询
     * @param sql                       执行的sql语句
     * @param paramMap                    注入的参数  为命名参数 :value
     * @return List
     */
    public List<Map<String, Object>> queryNamedParameterForList(String sql,Map<String, ?> paramMap){
        log.warn("自定义查询SQL输出为：【{}】",sql );
        return namedParameterJdbcTemplate.queryForList(sql, paramMap);
    }
    
    /**
     * 根据自定义进行原生的sql更新，无参数
     * @param sql                       执行的sql语句
     */
    public int updateData(String sql) {
    	log.warn("自定义查询SQL输出为：【{}】",sql );
    	return jdbcTemplate.update(sql);
    }
    
    /**
     * 根据自定义的动态参数进行原生的sql更新
     * @param sql                       执行的sql语句
     * @param params                    注入的参数  为占位符 ？
     */
    public int updateDataWithParam(String sql,Object[] params) {
    	log.warn("自定义查询SQL输出为：【{}】",sql );
        return jdbcTemplate.update(sql,params);
    }
}