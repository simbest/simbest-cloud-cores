/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.utils.http.client;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.exception.Exceptions;
import com.simbest.cloud.cores.exceptions.BusinessForbiddenException;
import com.simbest.cloud.cores.utils.files.AppFileUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.simbest.cloud.cores.constants.ApplicationConstants.HTTPGET;
import static com.simbest.cloud.cores.constants.ApplicationConstants.HTTPPOST;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;


/**
 * 用途：POST请求提交表单数据
 * 作者: lishuyi
 * 时间: 2020/1/9  16:41
 */
@Slf4j
@SuppressWarnings("ALL")
public class PostRequest {

    protected String url;

    private static HttpHeaders headers;

    private Map<String, List<String>> formParams;

    private Map<String, List<Object>> formParamsObject;

    @Setter
    protected RestTemplate restTemplate;

    @Setter
    protected AppFileUtil appFileUtil;

    public PostRequest(String url) {
        this.url = url;
        this.formParams = new LinkedHashMap<>();
        this.headers  = new HttpHeaders();
        this.formParamsObject = Maps.newLinkedHashMap();
    }

    public PostRequest header(String name, String value) {
        if (StringUtils.hasLength(name) && null != value)
            this.headers.add(name, value);

        return this;
    }

    /**
     * 设置提交的请求参数及其值
     *
     * @param parameters 键值对列表
     * @return
     */
    public PostRequest param(Map<String, String> parameters) {
        //Assert.notEmpty(parameters, "Parameters may not be null.");
        if (CollectionUtils.isEmpty(parameters)) {
            return this;
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            this.param(entry.getKey(), entry.getValue(), false);
        }
        return this;
    }

    /**
     * 设置提交的请求参数及其值
     *
     * @param name  参数名
     * @param value 参数值
     * @return PostRequest
     */
    public PostRequest param(String name, String value) {
        return this.param(name, value, false);
    }

    /**
     * 设置提交的请求参数及其值,支持参数为文件
     *
     * @param name  参数名
     * @param value 参数值
     * @return PostRequest
     */
    public PostRequest param(String name, Object value) {
        return this.param(name, value, false);
    }

    /**
     * 设置提交的请求参数及其值,支持参数为文件
     *
     * @param name  参数名
     * @param value 参数值
     * @return PostRequest
     */
    public PostRequest queryString(String name, String value) {
        return this.param(name, value, false);
    }

    /**
     * 设置提交的请求参数及其值,支持参数为文件
     *
     * @param name    参数名
     * @param value   参数值
     * @param replace 值为[@code true}则替换处理
     * @return PostRequest
     */
    public PostRequest param(String name, Object value, boolean replace) {
        //Assert.hasLength(name, "Name may not be null or empty.");
        if (StringUtils.isEmpty(name)) {
            log.debug("参数名称name为空，忽略追加参数");
            return this;
        }
        if (!replace && value == null) {
            log.debug("参数名称【{}】的参数值value为空，且不进行替换", name);
            return this;
        }
        List<Object> valueList = this.formParamsObject.get(name);
        if (valueList == null) {
            valueList = new LinkedList<>();
            /*if(StrUtil.equals(ApplicationConstants.REST_TEMPLATE_PARM_FILE,name)){
                FileSystemResource resource = new FileSystemResource(new File(name));
                this.formParamsObject.put(name, resource);
            }else{
                this.formParamsObject.put(name, valueList);
            }*/
            this.formParamsObject.put(name, valueList);
        }

        if (replace) valueList.clear();

        if(StrUtil.equals(ApplicationConstants.REST_TEMPLATE_PARM_FILE,name)){
            FileSystemResource resource = new FileSystemResource(new File(name));
            valueList.add(resource);
        }else{
            valueList.add(value);
        }
        return this;
    }

    /**
     * 设置提交的请求参数及其值
     *
     * @param name    参数名
     * @param value   参数值
     * @param replace 值为[@code true}则替换处理
     * @return PostRequest
     */
    public PostRequest param(String name, String value, boolean replace) {
        //Assert.hasLength(name, "Name may not be null or empty.");
        if (StringUtils.isEmpty(name)) {
            log.debug("参数名称name为空，忽略追加参数");
            return this;
        }
        if (!replace && value == null) {
            log.debug("参数名称【{}】的参数值value为空，且不进行替换", name);
            return this;
        }
        List<String> valueList = this.formParams.get(name);
        if (valueList == null) {
            valueList = new LinkedList<>();
            this.formParams.put(name, valueList);
        }

        if (replace) valueList.clear();

        valueList.add(value);
        return this;
    }

    private void printLog(Exception e){
        log.error("HTTP请求发生错误，url地址【{}】,参数如下：", url);
        for(Map.Entry<String, List<String>> entry : formParams.entrySet()){
            log.error("键【{}】，值【{}】",entry.getKey(), entry.getValue().get(ApplicationConstants.ZERO));
        }
        Exceptions.printException(e);
    }

    /**
     * 将响应结果转为JavaBean对象
     * @param targetClass 目标类型
     * @param httpMethod  请求方式
     * @param <E>         泛型类型
     * @return
     */
    public <E> E asBean(Class<E> targetClass, HttpMethod httpMethod) {
        E response = null;
        try {
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(toValueMap(formParams), headers);
            switch (httpMethod.name()){
                case HTTPGET:
                    throw new BusinessForbiddenException("getForObject方式拼接参数较为麻烦，建议应用使用asBean2的exchange方式进行调用！！");
                case HTTPPOST:
                    response = restTemplate.postForObject(url, httpEntity, targetClass);
                    break;
            }
        } catch (Exception e){
            printLog(e);
        }
        return response;
    }

    /**
     * 将响应结果转为JavaBean对象
     *
     * @param targetClass 目标类型
     * @param <E>         泛型类型
     * @return JavaBean对象
     * @throws HttpClientException 如果服务器返回非200则抛出此异常
     */
    public <E> E asBean(Class<E> targetClass) {
        return asBean(targetClass, POST);
    }

    /**
     * 将响应结果转为JavaBean对象
     *
     * @param targetClass 目标类型
     * @param httpMethod  请求方式
     * @param <E>         泛型类型
     * @return JavaBean对象
     * @throws HttpClientException 如果服务器返回非200则抛出此异常
     */
    public <E> E asBean2(Class<E> targetClass, HttpMethod httpMethod) {
        ResponseEntity<E> response = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(url)
                    .queryParams(toValueMap(formParams));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
            switch (httpMethod.name()){
                case HTTPGET:
                    response = restTemplate.exchange(builder.build().encode().toUri(), GET, httpEntity, targetClass);
                    break;
                case HTTPPOST:
                    response = restTemplate.exchange(builder.build().encode().toUri(), POST, httpEntity, targetClass);
                    break;
            }
        } catch (Exception e){
            printLog(e);
        }
        return response == null ? null : response.getBody();
    }

    /**
     * 将响应结果转为JavaBean对象
     *
     * @param targetClass 目标类型
     * @param <E>         泛型类型
     * @return JavaBean对象
     * @throws HttpClientException 如果服务器返回非200则抛出此异常
     */
    public <E> E asBean2(Class<E> targetClass) {
        return asBean2(targetClass, POST);
    }

    /**
     * 将响应结果转为String对象
     * @param httpMethod
     * @return
     */
    public String asString(HttpMethod httpMethod) {
        String response = null;
        try {
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(toValueMap(formParams), headers);
            switch (httpMethod.name()){
                case HTTPGET:
                    response = restTemplate.getForObject(url, String.class, httpEntity);
                    break;
                case HTTPPOST:
                    response = restTemplate.postForObject(url, httpEntity, String.class);
                    break;
            }
        } catch (Exception e){
            printLog(e);
        }
        return response;
    }

    /**
     * 将响应结果转为String对象
     * @return
     */
    public String asString() {
        return asString(POST);
    }

    public String asString2(String json, HttpMethod httpMethod) {
        String response = null;
        try {
            switch (httpMethod.name()){
                case HTTPGET:
                    response = restTemplate.getForObject(url, String.class, json);
                    break;
                case HTTPPOST:
                    response = restTemplate.postForObject(url, json, String.class);
                    break;
            }
        } catch (Exception e){
            printLog(e);
        }
        return response;
    }

    public String asString2(String json) {
        return asString2(json, GET);
    }

    private static MultiValueMap toValueMap(Map<String, List<String>> parameters){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for(Map.Entry<String, List<String>> entry : parameters.entrySet()){
            params.set(entry.getKey(), entry.getValue().get(ApplicationConstants.ZERO));
        }
        return params;
    }

    /**
     * 下载文件
     * @param httpMethod 请求方法
     * @param fileSuffix 文件后缀
     * @return
     */
    public File asFile(HttpMethod httpMethod, String fileSuffix) {
        File downloadFile = null;
        byte[] downloadData = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(url)
                    .queryParams(toValueMap(formParams));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
            switch (httpMethod.name()){
                case HTTPGET:
                    downloadData = restTemplate.exchange(builder.build().encode().toUri(), GET, httpEntity, byte[].class).getBody();
                    break;
                case HTTPPOST:
                    downloadData = restTemplate.exchange(builder.build().encode().toUri(), POST, httpEntity, byte[].class).getBody();
                    break;
            }
            if(downloadData != null){
                downloadFile = appFileUtil.createTempFile(fileSuffix);
                FileUtil.writeBytes(downloadData, downloadFile);
            }
        } catch (Exception e){
            printLog(e);
        }
        return downloadFile;
    }
}
