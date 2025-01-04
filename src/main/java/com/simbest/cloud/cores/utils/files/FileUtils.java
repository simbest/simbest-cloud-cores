package com.simbest.cloud.cores.utils.files;

import cn.hutool.core.io.FileUtil;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * <strong>Title : FileUtils</strong><br>
 * <strong>Description : 文件操作工具</strong><br>
 * <strong>Create on : 2020/7/8</strong><br>
 * <strong>Modify on : 2020/7/8</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
@Slf4j
@Component
public class FileUtils {

    public boolean writeContentToFile(String filePath,String fileContent){
        try {
            FileUtil.writeString(fileContent,filePath, ApplicationConstants.UTF_8);
            return Boolean.TRUE;
        }catch (Exception e){
            Exceptions.printException( e );
        }
        return Boolean.FALSE;
    }

    /**
     * 拷贝文件
     *
     * @param sourceFilePath 源文件路径
     * @param targetFilePath 目标文件路径
     * @throws IOException 如果发生I/O错误
     */

    public static void copyFile(String sourceFilePath, String targetFilePath) throws IOException {
        Path source = Paths.get(sourceFilePath);
        Path target = Paths.get(targetFilePath);
        // 确保源文件存在
        if (!Files.exists(source)) {
            throw new IOException("源文件不存在: " + sourceFilePath);
        }
        // 拷贝文件
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 移动文件
     *
     * @param sourceFilePath 源文件路径
     * @param targetFilePath 目标文件路径
     * @throws IOException 如果发生I/O错误
     */

    public static void moveFile(String sourceFilePath, String targetFilePath) throws IOException {
        Path source = Paths.get(sourceFilePath);
        Path target = Paths.get(targetFilePath);
        // 确保源文件存在
        if (!Files.exists(source)) {
            throw new IOException("源文件不存在: " + sourceFilePath);
        }
        // 注意：如果目标文件已存在，此方法将抛出FileAlreadyExistsException
        // 你可以根据需要添加额外的逻辑来处理这种情况
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 从给定的文件路径字符串中提取文件路径和文件名。
     * @param filePath 完整的文件路径
     * @return 包含文件路径和文件名的字符串数组，其中索引0是文件路径，索引1是文件名
     */

    public static String[] extractFilePathAndName(String filePath) {
        // 使用lastIndexOf找到最后一个'/'的位置
        int lastIndex = filePath.lastIndexOf('/');
        // 检查是否找到了'/'
        if (lastIndex == -1) {
            // 如果没有找到，则认为整个字符串是文件名，路径为空
            return new String[]{"", filePath};
        }
        // 提取文件路径和文件名
        String path = filePath.substring(0, lastIndex);
        String fileName = filePath.substring(lastIndex + 1);
        // 返回结果
        return new String[]{path, fileName};
    }

    public static void main(String[] args) {
        try {
            String sourceFile = "D:\\demoTidbTest.zip";
            String targetFile = "D:\\bak_20240516_demoTidbTest.zip";
            copyFile(sourceFile, targetFile);
            System.out.println("文件拷贝成功！");
        } catch (IOException e) {
            Exceptions.printException(e);
        }
    }
}
