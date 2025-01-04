/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrJoiner;
import com.simbest.cloud.cores.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用途：读取Boot项目Classpath路径下的文件
 * 作者: lishuyi
 * 时间: 2018/8/2  21:13
 */
@Slf4j
public class BootAppFileReader {

    /**
     * 读取文件并转换为BufferedReader
     *
     * @param filepath
     * @return BufferedReader
     */
    public static BufferedReader getClasspathFile(String filepath) {
        filepath = ResourceUtils.CLASSPATH_URL_PREFIX + filepath;
        BufferedReader bufferedReader = null;
        try {
            ClassPathResource resource = new ClassPathResource(filepath);
            InputStream inputStream = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            try {
                bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile(filepath)));
            } catch (FileNotFoundException e1) {
            }
        }
        Assert.notNull(bufferedReader, String.format("严重错误：请注意读取配置文件%s失败！", filepath));
        return bufferedReader;
    }

    /**
     * 读取文件
     *
     * @param filepath
     * @return File
     */
    public static File getClasspathFileToFile(String filepath) {
        filepath = ResourceUtils.CLASSPATH_URL_PREFIX + filepath;
        try {
            File targetFile = ResourceUtils.getFile(filepath);
            return targetFile;
        } catch (IOException e) {
            ClassPathResource resource = new ClassPathResource(filepath);
            try {
                InputStream inputStream = resource.getInputStream();
                File dir = new File(System.getProperty("user.home"));
                File tmpFile = FileUtil.createTempFile(dir);
                FileUtil.writeFromStream(inputStream, tmpFile);
                return tmpFile;
            } catch (IOException e1) {
                Exceptions.printException(e1);
            }
        }
        return null;
    }

    /**
     * 读取文件并转换为字符串
     *
     * @param filepath
     * @return String
     */
    public static String getClasspathFileToString(String filepath) {
        BufferedReader bufferedReader = getClasspathFile(filepath);
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        return StrJoiner.of("\n").append(lines).toString();
    }


    /**
     * 读取jar包中的文件并转换为BufferedReader
     *
     * @param filepath
     * @return BufferedReader
     */
    public static BufferedReader getClasspathFileJar(String filepath) {
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            try {
                bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile(filepath)));
            } catch (FileNotFoundException e1) {
            }
        }
        Assert.notNull(bufferedReader, String.format("严重错误：请注意读取配置文件%s失败！", filepath));
        return bufferedReader;
    }
}
