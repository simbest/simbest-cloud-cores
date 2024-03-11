/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.util;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.simbest.cloud.cores.base.enums.StoreLocation;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.exception.Exceptions;
import com.simbest.cloud.cores.sys.model.SysFile;
import com.simbest.cloud.cores.util.encrypt.UrlEncryptor;
import com.simbest.cloud.cores.util.json.JacksonUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.simbest.cloud.cores.constants.ApplicationConstants.DOT;
import static com.simbest.cloud.cores.constants.ApplicationConstants.SLASH;
import static com.simbest.cloud.cores.sys.web.SysFileController.*;
import static com.simbest.cloud.cores.util.encrypt.AbstractEncryptor.DEFAULT_URL_ENCODING;



/**
 * 用途：文件工具类
 * 作者: lishuyi
 * 时间: 2018/3/12  20:45
 */
@Slf4j
@Component
@SuppressWarnings("ALL")
public class AppFileUtil {

//    private static final String UPLOAD_FILE_PATTERN =
//            "(jpg|jpeg|png|gif|bmp|doc|docx|xls|xlsx|pdf|ppt|pptx|txt|rar|zip|7z|ogg|swf|webm" +
//                    "|html|htm|mov|flv|mp4|mp3|amr|csv|bpmn|apk|asf|wps|gw|xlsb|xlsm|tif|mht|ai|et|ett|xml|dif|xla|uof|ofd|dbf|rtf)$";
//    private static Pattern pattern = Pattern.compile(UPLOAD_FILE_PATTERN);

    /**
     * <app.file.util.upload.pattern>(jpg|jpeg|png|gif|bmp|doc|docx|xls|xlsx|pdf|ppt|pptx|txt|rar|zip|7z|ogg|swf|webm|html|htm|mov|flv|mp4|mp3|amr|csv|bpmn|apk|asf|wps|gw|xlsb|xlsm|tif|mht|ai|et|ett|xml|dif|xla|uof|ofd|dbf|rtf|enc|dwg)$</app.file.util.upload.pattern>
     */
    @Value("${app.file.util.upload.pattern:(jpg|jpeg|png|gif|bmp|doc|docx|xls|xlsx|pdf|ppt|pptx|txt|rar|zip|7z|ogg|swf|webm|html|htm|mov|flv|mp4|mp3|amr|csv|bpmn|apk|asf|wps|gw|xlsb|xlsm|tif|mht|ai|et|ett|xml|dif|xla|uof|ofd|dbf|rtf)$}")
    private String UPLOAD_FILE_PATTERN;

    private static Pattern pattern = null;

    public static final String NGINX_STATIC_FILE_LOCATION = "/uploadFiles";

    @Autowired
    private AppConfig config;

    @Autowired
    private SpringContextUtil springContextUtil;

    @Autowired
    private UrlEncryptor urlEncryptor;

    private AppFileSftpUtil sftpUtil;

    @Getter
    public StoreLocation serverUploadLocation;

    private String DEFAULT_COMPRESS_IMAGE_WIDTH = "2000";    //默认分辨率图片像素宽

    private String DEFAULT_COMPRESS_IMAGE_SUFFIX = "jpg";    //默认分辨率压缩图片后缀

    @PostConstruct
    public void init() {
        this.pattern = Pattern.compile(UPLOAD_FILE_PATTERN);
        serverUploadLocation = Enum.valueOf(StoreLocation.class, config.getUploadLocation());
        try {
            sftpUtil = springContextUtil.getBean(AppFileSftpUtil.class);
        }
        catch (NoSuchBeanDefinitionException e) {
        }
        finally {
            if(null == sftpUtil){
                log.info("请注意应用没有配置SFTP，请检查配置是否需要，如不需要，则忽略该条警告信息！");
            }
        }
    }

    /**
     * 判断是否允许上传
     *
     * @param fileName
     * @return boolean
     */
    public static boolean validateUploadFileType(String fileName) {
        String fileSuffix = getFileSuffix(fileName).toLowerCase();
        if(StringUtils.isNotEmpty(fileSuffix)) {
            Matcher matcher = pattern.matcher(fileSuffix);
            boolean ret = matcher.matches();
            log.debug("判断文件【{}】是否合法结果为【{}】", fileName, ret);
            return ret;
        }
        //没有后缀的文件，也允许上传
        else{
            return true;
        }
    }

    /**
     * //将文件名称进行MD5编码，解决文件保存服务器时的乱码问题
     * @param fileName
     * @return
     */
    public String getFileNameMd5(String fileName) {
        //文件名称通过URL解码
        fileName = urlEncryptor.decrypt(fileName);
        //特殊字符过滤，防止XSS漏洞
        fileName = JacksonUtils.escapeString(fileName);
        String fileBaseName = getFileBaseName(fileName);
        String fileSuffix = getFileSuffix(fileName);
        fileName = DigestUtils.md5Hex(fileBaseName) + DOT + fileSuffix;
        return fileName;
    }

    /**
     * 根据路径返回文件名，如：http://aaa/bbb.jpg C:/aaa/abc.jpg 返回abc.jpg
     *
     * @param fileUrl
     * @return String
     */
    public static String getFileName(String fileUrl) {
        Assert.notNull(fileUrl, fileUrl+"文件URL路径不能为空");
        String fileName = null;
        String suffix = null;
        try {
            suffix = FilenameUtils.getExtension(new URL(fileUrl).getPath());
            if(StringUtils.isEmpty(suffix)){
                fileName = getFileNameByContentDisposition(fileUrl);
            }
            else{
                fileName = FilenameUtils.getName(new URL(fileUrl).getPath());
            }
        } catch (MalformedURLException e) {
            log.warn("文件【{}】无法通过URL获取路径，直接通过工具类提取文件全称为【{}】", fileUrl, fileName);
            fileName = FilenameUtils.getName(fileUrl);
        }
        try {
            fileName = URLDecoder.decode(fileName, DEFAULT_URL_ENCODING);
        }catch (UnsupportedEncodingException e1){
            Exceptions.printException(e1);
        }
        return fileName;
    }

    /**
     * 根据路径返回文件名，如：http://aaa/bbb.jpg C:/aaa/abc.jpg 返回abc
     *
     * @param fileUrl
     * @return String
     */
    public static String getFileBaseName(String fileUrl) {
        Assert.notNull(fileUrl, fileUrl+"文件URL路径不能为空");
        String fileBaseName = null;
        String suffix = null;
        try {
            suffix = FilenameUtils.getExtension(new URL(fileUrl).getPath());
            if(StringUtils.isEmpty(suffix)){
                String fileName = getFileNameByContentDisposition(fileUrl);
                fileBaseName = FilenameUtils.getBaseName(fileName);
            }
            else{
                fileBaseName = FilenameUtils.getBaseName(new URL(fileUrl).getPath());
            }
        } catch (MalformedURLException e) {
            fileBaseName = FilenameUtils.getBaseName(fileUrl);
            log.warn("文件【{}】无法通过URL获取路径，直接通过工具类提取文件名称为【{}】", fileUrl, fileBaseName);
        }
        return fileBaseName;
    }

    /**
     * 根据路径返回文件后缀，如：http://aaa/bbb.jpg C:/aaa/abc.jpg 返回jpg
     *
     * @param fileUrl
     * @return String
     */
    public static String getFileSuffix(String fileUrl) {
        Assert.notNull(fileUrl, fileUrl+"文件URL路径不能为空");
        String suffix = null;
        try {
            suffix = FilenameUtils.getExtension(new URL(fileUrl).getPath());
            if(StringUtils.isEmpty(suffix)) {
                String fileName = getFileNameByContentDisposition(fileUrl);
                suffix = FilenameUtils.getExtension(fileName);
            }
        } catch (MalformedURLException e) {
            suffix = FilenameUtils.getExtension(fileUrl);
            log.warn("文件【{}】无法通过URL获取路径，直接通过工具类提取文件后缀为【{}】", fileUrl, suffix);
        }
        return suffix;
    }

    /**
     * 通过URL Connetion 在Content-Disposition头信息获取文件名称
     * @param fileUrl
     * @return
     */
    public static String getFileNameByContentDisposition(String fileUrl) {
        String fileName = null;
        HttpURLConnection urlConnection = null;
        try {
//            URL connUrl = new URL(fileUrl);
            //引入galimatias,解决不规范文件名导致文件下载异常
            URL connUrl = io.mola.galimatias.URL.parse(fileUrl).toJavaURL();
            urlConnection = (HttpURLConnection) connUrl.openConnection();
            String fieldValue = urlConnection.getHeaderField("Content-Disposition");
            fieldValue = StringUtils.replaceIgnoreCase(fieldValue, "filename=", "filename=");
            fileName = StringUtils.substringAfter(fieldValue, "filename=");
            fileName = StringUtils.remove(fileName, "\"");
            fileName = StringUtils.remove(fileName, "\'");
            fileName = new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
            Exceptions.printException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
        }
        return fileName;
    }

    /**
     * 返回 text/plain image/jpeg image/png video/mp4  application/vnd.openxmlformats-officedocument.wordprocessingml.document 等文件类型
     * @param file
     * @return
     */
    public static String getFileType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean isImage(File file) {
        Assert.notNull(file, "图片文件不能为空");
        boolean ret = false;
        try {
            Image image = ImageIO.read(file);
            if (image != null) {
                ret = true;
            }
        } catch(Exception ex) {
        }
        log.debug("判断文件【{}】是否为图片结果为【{}】", file.getName(), ret);
        return ret;
    }

    public static String replaceSlash(String directory) {
        directory = StringUtils.replace(directory, "\\", SLASH);
        directory = StringUtils.replace(directory, "//", SLASH);
        return directory;
    }

    private String diskUpload(byte[] uploadFileBytes, File targetFileDirectory, String fileName) throws IOException {
        if (!targetFileDirectory.exists()) {
            FileUtils.forceMkdir(targetFileDirectory);
            log.debug("目录不存在，即将强制创建路径【{}】", targetFileDirectory.getPath());
        }
        String pathTmp = targetFileDirectory.getPath() + SLASH + getFileNameMd5(fileName);
        pathTmp = replaceSlash(pathTmp);
        Path path = Paths.get(pathTmp);
        Files.write(path, uploadFileBytes);
        String filePath = path.toString();
        log.debug("通过Disk方式上传文件，返回路径为：【{}】", filePath);
        return filePath;
    }

/*    private String fastDfsUpload(byte[] uploadFileBytes, String fileName) throws Exception {
        String filePath = FastDfsClient.uploadFile(uploadFileBytes, fileName, getFileSuffix(fileName));
        log.debug("通过FastDFS方式上传文件，返回路径为：【{}】", filePath);
        return filePath;
    }*/

    private String ftpSftpUpload(byte[] uploadFileBytes, String directory, String fileName) {
        directory = replaceSlash(directory);
        String filePath = sftpUtil.upload(directory, fileName, uploadFileBytes);
        log.debug("通过ftpSftp方式上传文件，返回路径为：【{}】", filePath);
        return filePath;
    }

    private SysFile buildSysFile(String filePath, String fileName, Long fileSize){
        SysFile sysFile = SysFile.builder().fileName(fileName).fileType(getFileSuffix(fileName)).storeLocation(serverUploadLocation)
                .filePath(StringUtils.replace(filePath, ApplicationConstants.SEPARATOR, SLASH)).fileSize(fileSize)
                .downLoadUrl(DOWNLOAD_FULL_URL).apiFilePath(DOWNLOAD_FULL_URL_API).anonymousFilePath(DOWNLOAD_FULL_URL_ANONYMOUS).build();
        return sysFile;
    }

    /**
     * 将文件在远程URL读取后，再上传
     * @param fileUrl   远程文件URL
     * @param directory 相对路径
     * @return SysFile
     */
    public SysFile uploadFromUrl(String fileUrl, String directory) throws Exception {
        String filePath = null;
        String fileName = null;
        Long fileSize = null;
        HttpURLConnection urlConnection = null;
        UrlFile urlFile = null;
        try {
            urlFile = openAndConnectUrl(fileUrl);
            urlConnection = urlFile.getConn();
            if(StringUtils.isEmpty(urlFile.getFileName())){
                //URL连接上面没有文件名
                fileName = CodeGenerator.systemUUID();
            }
            fileName = fileName + DOT + urlFile.getFileSuffix();
            switch (serverUploadLocation) {
                case disk:
                    File targetFileDirectory = createAutoUploadDirFile(directory);
                    File storeFile = new File(targetFileDirectory.getAbsolutePath() + SLASH + getFileNameMd5(fileName));
                    FileUtils.touch(storeFile); //覆盖文件
                    FileUtils.copyURLToFile(urlFile.getConnUrl(), storeFile);
                    filePath = storeFile.getAbsolutePath();
                    fileSize = storeFile.length();
                    break;
                case fastdfs:
/*                    File tmpFile = createTempFile();
                    FileUtils.copyURLToFile(urlFile.getConnUrl(), tmpFile);
                    filePath = FastDfsClient.uploadFile(IOUtils.toByteArray(new FileInputStream(tmpFile)),
                            fileName, getFileSuffix(fileName));
                    fileSize = tmpFile.length();*/
                    break;
                case ftp:
                    log.debug("基于ftp，方式同sftp");
                case sftp:
                    File tmpFile1 = createTempFile();
                    FileUtils.copyURLToFile(urlFile.getConnUrl(), tmpFile1);
                    sftpUtil.upload(createAutoUploadDirPath(directory), fileName, tmpFile1);
                    filePath = config.getUploadPath() + createAutoUploadDirPath(directory) + SLASH + getFileNameMd5(fileName);
                    break;
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
            Exceptions.printException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
        }
        SysFile sysFile = SysFile.builder().fileName(fileName).fileType(urlFile.getFileSuffix()).storeLocation(serverUploadLocation)
                .filePath(filePath).fileSize(fileSize).downLoadUrl(DOWNLOAD_FULL_URL).apiFilePath(DOWNLOAD_FULL_URL_API)
                .anonymousFilePath(DOWNLOAD_FULL_URL_ANONYMOUS).build();
        log.debug("上传文件成功，具体信息如下： {}", sysFile.toString());
        return sysFile;
    }

    /**
     * 上传单个文件
     * @param directory 相对路径
     * @param multipartFile
     * @return SysFile
     * @throws Exception
     */
    public SysFile uploadFile(String directory, MultipartFile multipartFile) throws Exception {
        return uploadFile(directory, multipartFile, null);
    }

    /**
     * 上传单个文件  自定义文件名称
     * @param directory
     * @param multipartFile
     * @param fileName
     * @return
     * @throws Exception
     */
    public SysFile uploadFile(String directory, MultipartFile multipartFile, String fileName) throws Exception {
        Assert.notNull(multipartFile, "上传文件不能为空");
        List<SysFile> sysFileList = uploadFiles(directory, Arrays.asList(multipartFile), fileName);
        if(sysFileList.size() > 0){
            return sysFileList.get(0);
        }
        else{
            return null;
        }
    }


    /**
     * 上传多个文件   重载上面的方法
     * @param directory 相对路径
     * @param multipartFiles
     * @return List<SysFile>
     * @throws Exception
     */
    public List<SysFile> uploadFiles(String directory, Collection<MultipartFile> multipartFiles) throws Exception {
        return uploadFiles(directory, multipartFiles,null);
    }

    /**
     * 上传多个文件
     * @param directory 相对路径
     * @param multipartFiles
     * @return List<SysFile>
     * @throws Exception
     */
    public List<SysFile> uploadFiles(String directory, Collection<MultipartFile> multipartFiles, String fileName) throws Exception {
        Assert.notEmpty(multipartFiles, "上传文件不能为空");
        List<SysFile> fileModels = Lists.newArrayList();
        for (MultipartFile multipartFile : multipartFiles) {
            long fileSize = multipartFile.getSize();
            if (multipartFile.isEmpty()) {
                log.warn("上传文件流为空，继续循环处理上传文件");
                continue;
            } else if (fileSize == 0L){
                log.warn("上传文件流大小为空，继续循环处理上传文件");
                continue;
            }
            String uploadFilePath = null;
            String uploadFileName = null;
            if(StrUtil.isNotEmpty(fileName)){
                uploadFileName = urlEncryptor.decrypt(fileName);
            }
            else{
                uploadFileName = getFileName(multipartFile.getOriginalFilename());
            }
            //特殊字符过滤，防止XSS漏洞
            uploadFileName = JacksonUtils.escapeString(uploadFileName);
            if(validateUploadFileType(uploadFileName)) {
                log.debug("即将以【{}】方式上传【{}】文件", serverUploadLocation, uploadFileName);
                //File tempFile = File.createTempFile("tmp", null);
                //multipartFile.transferTo(tempFile);
                //置为null是为了告诉gc此块内存可以回收
                //multipartFile = null;
                switch (serverUploadLocation) {
                    case disk:
//                        uploadFilePath = diskUpload(multipartFile.getBytes(), createAutoUploadDirFile(directory), uploadFileName);
//                        uploadFilePath = diskUpload(IOUtils.toByteArray(multipartFile.getInputStream()), createAutoUploadDirFile(directory), uploadFileName);
//                        解决java.lang.OutOfMemoryError: Java heap space
                        File saveFile = new File(createAutoUploadDirFile(directory).getPath().concat(ApplicationConstants.SEPARATOR).concat(getFileNameMd5(uploadFileName)));
                        InputStream sourceInput = multipartFile.getInputStream();
                        FileOutputStream outTarget = new FileOutputStream(saveFile);
                        IOUtils.copy(sourceInput, outTarget);
                        outTarget.close();
                        IOUtils.closeQuietly(sourceInput);
                        uploadFilePath = saveFile.getPath();
                        log.debug("文件保存路径"+uploadFilePath);
                        break;
                    case fastdfs:
                       // uploadFilePath = fastDfsUpload(multipartFile.getBytes(), uploadFileName);
                        //uploadFilePath = fastDfsUpload(FileUtil.readBytes(tempFile), uploadFileName);
                        break;
                    case ftp:
                        log.debug("基于ftp，方式同sftp");
                    case sftp:
                        uploadFilePath = ftpSftpUpload(multipartFile.getBytes(), createAutoUploadDirPath(directory), uploadFileName);
                        //uploadFilePath = ftpSftpUpload(FileUtil.readBytes(tempFile), createAutoUploadDirPath(directory), uploadFileName);
                        break;
                }
                Assert.notNull(uploadFilePath, String.format("文件以【%s】方式上传失败", serverUploadLocation));
                SysFile sysFile = buildSysFile(uploadFilePath, uploadFileName, fileSize);
                log.debug("【{}】上传成功，具体信息如下: 【{}】", uploadFileName, sysFile.toString());
                fileModels.add(sysFile);
            } else {
                log.warn("【{}】文件类型受限，不允许上传！", uploadFileName);
                throw new RuntimeException(uploadFileName+"文件类型受限，不允许上传！");
            }
        }
        return fileModels;
    }

    /**
     * 上传MultipartFile文件，自定义目录
     * @param customDirectory
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    public List<SysFile> customUploadFiles(String customDirectory, Collection<MultipartFile> multipartFiles) throws Exception {
        return customUploadFiles(customDirectory, multipartFiles,null);
    }

    /**
     * 上传MultipartFile文件，自定义目录，自定义文件名
     * @param customDirectory
     * @param multipartFiles
     * @param fileName
     * @return
     * @throws Exception
     */
    public List<SysFile> customUploadFiles(String customDirectory, Collection<MultipartFile> multipartFiles, String fileName) throws Exception {
        Assert.notEmpty(multipartFiles, "上传文件不能为空");
        List<SysFile> fileModels = Lists.newArrayList();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                log.warn("上传文件流为空，继续循环处理上传文件");
                continue;
            } else if (multipartFile.getSize() == 0L){
                log.warn("上传文件流大小为空，继续循环处理上传文件");
                continue;
            }
            String uploadFilePath = null;
            String uploadFileName = StrUtil.isEmpty(fileName) ? getFileName(multipartFile.getOriginalFilename()) : fileName;
            if(validateUploadFileType(uploadFileName)) {
                log.debug("即将以【{}】方式上传【{}】文件", serverUploadLocation, uploadFileName);
                switch (serverUploadLocation) {
                    case disk:
                        uploadFilePath = diskUpload(multipartFile.getBytes(), forceCreateCustomUploadDirectory(customDirectory), uploadFileName);
                        break;
                    case fastdfs:
//                        String filePath = FastDfsClient.uploadFile(IOUtils.toByteArray(multipartFile.getInputStream()), fileName, getFileSuffix(fileName));
//                        filePath = FastDfsClient.uploadFile(FileUtil.readBytes(uploadFile), filename, getFileSuffix(filename));
                       // uploadFilePath = fastDfsUpload(multipartFile.getBytes(), uploadFileName);
                        break;
                    case ftp:
                        log.debug("基于ftp，方式同sftp");
                    case sftp:
                        uploadFilePath = ftpSftpUpload(multipartFile.getBytes(), customDirectory, uploadFileName);
                        break;
                }
                Assert.notNull(uploadFilePath, String.format("文件以【%s】方式上传失败", serverUploadLocation));
                SysFile sysFile = buildSysFile(uploadFilePath, uploadFileName, multipartFile.getSize());
                log.debug("【{}】上传成功，具体信息如下: 【{}】", uploadFileName, sysFile.toString());
                fileModels.add(sysFile);
            } else {
                log.warn("【{}】文件类型受限，不允许上传！", uploadFileName);
                throw new RuntimeException(uploadFileName+"文件类型受限，不允许上传！");
            }
        }
        return fileModels;
    }

    /**
     * 上传本地文件，并保存在系统默认的年月日目录下
     * @param localFile
     * @param directory
     * @return
     */
    public SysFile uploadFromLocalAutoDirectory(String directory, File localFile, String fileName) throws Exception {
        return uploadFromLocal(createAutoUploadDirPath(directory), localFile, fileName);
    }

    /**
     * 上传本地文件，并保存在指定远程服务器路径，不做任何处理
     * @param localFile
     * @param targetDir
     * @return
     */
    public SysFile uploadFromLocalCustomDirectory(String customDirectory, File localFile, String fileName) throws Exception {
        return uploadFromLocal(customDirectory, localFile, fileName);
    }

    /**
     * 上传普通File文件
     * @param directory
     * @param uploadFile
     * @param fileName
     * @return
     * @throws Exception
     */
    private SysFile uploadFromLocal(String directory, File uploadFile, String fileName) throws Exception {
        SysFile sysFile = null;
        String uploadFilePath = null;
        String uploadFileName = StrUtil.isEmpty(fileName) ? getFileName(uploadFile.getName()) : fileName;
        //特殊字符过滤，防止XSS漏洞
        uploadFileName = JacksonUtils.escapeString(uploadFileName);
        if(validateUploadFileType(uploadFileName)) {
            log.debug("即将以【{}】方式上传【{}】文件", serverUploadLocation, uploadFileName);
            switch (serverUploadLocation) {
                case disk:
                    uploadFilePath = diskUpload(FileUtil.readBytes(uploadFile), forceCreateCustomUploadDirectory(directory), uploadFileName);
                    break;
                case fastdfs:
                 ///   uploadFilePath = fastDfsUpload(FileUtil.readBytes(uploadFile), uploadFileName);
                    break;
                case ftp:
                    log.debug("基于ftp，方式同sftp");
                case sftp:
                    uploadFilePath = ftpSftpUpload(FileUtil.readBytes(uploadFile), directory, uploadFileName);
                    break;
            }
            Assert.notNull(uploadFilePath, String.format("文件以【%s】方式上传失败", serverUploadLocation));
            sysFile = buildSysFile(uploadFilePath, uploadFileName, uploadFile.length());
            log.debug("【{}】上传成功，具体信息如下: 【{}】", uploadFileName, sysFile.toString());

        } else {
            log.warn("【{}】文件类型受限，不允许上传！", uploadFileName);
            throw new RuntimeException(uploadFileName+"文件类型受限，不允许上传！");
        }
        return sysFile;
    }


    private BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     * 将应用系统中的图片压缩后，再上传（按质量压缩）
     * <p>
     * 图片高质量压缩参考：http://www.lac.inpe.br/JIPCookbook/6040-howto-compressimages.jsp
     *
     * @param imageFile 应用系统Context路径下的图片
     * @param quality   压缩质量
     * @param directory 相对路径
     * @return SysFile
     */
    public SysFile uploadCompressImage(File imageFile, float quality, String directory) {
        String filePath = null;
        String fileName = imageFile.getName();
        ByteArrayOutputStream baos = null;
        ImageOutputStream ios = null;
        ByteArrayInputStream bais = null;
        ImageWriter writer;
        try {
            BufferedImage image = null;
            if(getFileSuffix(imageFile.getName()).equalsIgnoreCase("bmp")){
                image = ImageIO.read(imageFile);
            } else {
//            java上传图片，压缩、更改尺寸等导致变色（表层蒙上一层红色）
//            https://blog.csdn.net/qq_25446311/article/details/79140008?tdsourcetag=s_pctim_aiomsg
                Image bufferedImage = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                image = this.toBufferedImage(bufferedImage);// Image to BufferedImage
            }
            Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpg");
            if (!writers.hasNext())
                throw new IllegalStateException("No writers found");
            writer = (ImageWriter) writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            baos = new ByteArrayOutputStream(32768);
            switch (serverUploadLocation) {
                case disk:
                    File targetFileDirectory = createAutoUploadDirFile(directory);
                    File compressedFile = new File(targetFileDirectory.getAbsolutePath() + SLASH + getFileNameMd5(fileName));
                    log.debug("压缩图片保存路径为 :" + compressedFile.getPath());
                    ios = ImageIO.createImageOutputStream(baos);
                    FileImageOutputStream output = new FileImageOutputStream(compressedFile);
                    writer.setOutput(output);
                    writer.write(null, new IIOImage(image, null, null), param);
                    output.flush();
                    writer.dispose();
                    ios.flush();
                    ios.close();
                    baos.close();
                    output = null;
                    writer = null;
                    ios = null;
                    baos = null;
                    filePath = compressedFile.getAbsolutePath();
                    break;
                case fastdfs:
                    ios = ImageIO.createImageOutputStream(baos);
                    File tmpFile = createTempFile(getFileSuffix(imageFile.getName()));
                    output = new FileImageOutputStream(tmpFile);
                    writer.setOutput(output);
                    writer.write(null, new IIOImage(image, null, null), param);
                    output.flush();
                    writer.dispose();
                    ios.flush();
                    ios.close();
                    baos.close();
                    output = null;
                    writer = null;
                    ios = null;
                    baos = null;
                   // filePath = FastDfsClient.uploadLocalFile(tmpFile.getAbsolutePath());
                    tmpFile.deleteOnExit();
                    break;
                case ftp:
                    log.debug("基于ftp，方式同sftp");
                case sftp:
                    ios = ImageIO.createImageOutputStream(baos);
                    tmpFile = createTempFile(getFileSuffix(imageFile.getName()));
                    output = new FileImageOutputStream(tmpFile);
                    writer.setOutput(output);
                    writer.write(null, new IIOImage(image, null, null), param);
                    output.flush();
                    writer.dispose();
                    ios.flush();
                    ios.close();
                    baos.close();
                    output = null;
                    writer = null;
                    ios = null;
                    baos = null;
                    sftpUtil.upload(createAutoUploadDirPath(directory), fileName, tmpFile);
                    filePath = createAutoUploadDirPath(directory) + SLASH + fileName;
                    tmpFile.deleteOnExit();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Exceptions.printException(e);
        } finally {
            if (baos != null)
                try {
                    baos.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
        }
        SysFile sysFile = SysFile.builder().fileName(fileName).fileType(getFileSuffix(imageFile.getAbsolutePath())).storeLocation(serverUploadLocation)
                .filePath(filePath).fileSize(imageFile.length()).downLoadUrl(DOWNLOAD_FULL_URL).apiFilePath(DOWNLOAD_FULL_URL_API)
                .anonymousFilePath(DOWNLOAD_FULL_URL_ANONYMOUS).build();
        log.debug("上传文件成功，具体信息为【{}】", sysFile.toString());
        return sysFile;
    }

    /**
     * 将应用系统中的图片压缩后，再上传（按分辨率压缩，默认框2000像素）， fileSuffix 为JPG的时候，压缩的大小比较小，所以推荐
     * @param imageFile         压缩图片
     * @param compressWidth     压缩分辨率宽
     * @param directory         存放目录
     * @param fileSuffix        文件后缀
     * @return
     */
    public SysFile uploadCompressImage2(File imageFile, String compressWidth, String directory, String fileSuffix) {
        Integer realCompressWidth = StringUtils.isEmpty(compressWidth) ? Integer.parseInt(DEFAULT_COMPRESS_IMAGE_WIDTH) : Integer.parseInt(compressWidth);
        String realFileSuffix = StringUtils.isEmpty(fileSuffix) ? DEFAULT_COMPRESS_IMAGE_SUFFIX : fileSuffix;
        String filePath = null;
        long  compressImageLength=0;
        String fileName = imageFile.getName();
        ImageWriter writer;
        try {
            BufferedImage image = null;
            if(getFileSuffix(fileName).equalsIgnoreCase("bmp")){
                image = ImageIO.read(imageFile);
            } else {
//            java上传图片，压缩、更改尺寸等导致变色（表层蒙上一层红色）
                Image bufferedImage = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                image = this.toBufferedImage(bufferedImage);// Image to BufferedImage
            }
            int width = image.getWidth(); //原始分辨率宽
            int height= image.getHeight(); //原始分辨率高
            long size = imageFile.length();
            log.debug("文件【{}】后缀【{}】大小【{}】分辨率宽【{}】分辨率高【{}】", fileName, realFileSuffix, size, width, height);
            float scale = width < realCompressWidth ? 1 : (float) realCompressWidth/width;  //计算压缩比例
            int dowithWidth = (int) (scale * width);    //计算分辨率宽
            int doWithHeight = (int) (scale * height);  //计算分辨率高
            BufferedImage finalImage = null;
            finalImage = new BufferedImage(dowithWidth, doWithHeight, BufferedImage.TYPE_INT_RGB);
            //重新绘制图片
            finalImage.getGraphics().drawImage(image.getScaledInstance(dowithWidth, doWithHeight, Image.SCALE_SMOOTH), 0, 0, null);
            switch (serverUploadLocation) {
                case disk:
                    File targetFileDirectory = createAutoUploadDirFile(directory);
                    File compressedFile = new File(targetFileDirectory.getAbsolutePath() + SLASH + getFileNameMd5(fileName));
                    log.debug("压缩图片保存路径为 :" + compressedFile.getPath());
                    FileOutputStream output = new FileOutputStream(compressedFile);
                    ImageIO.createImageOutputStream(output);
                    ImageIO.write(finalImage, realFileSuffix, output);
                    output.flush();
                    long fileSize = compressedFile.length();
                    compressImageLength = fileSize;
                    output = null;
                    writer = null;
                    filePath = compressedFile.getAbsolutePath();
                    break;
                case fastdfs:
                    File tmpFile = createTempFile(getFileSuffix(fileName));
                    output = null;
                    writer = null;
                 //   filePath = FastDfsClient.uploadLocalFile(tmpFile.getAbsolutePath());
                    tmpFile.deleteOnExit();
                    break;
                case ftp:
                    log.debug("基于ftp，方式同sftp");
                case sftp:
                    tmpFile = createTempFile(getFileSuffix(fileName));
                    output = null;
                    writer = null;
                    sftpUtil.upload(createAutoUploadDirPath(directory), fileName, tmpFile);
                    filePath = createAutoUploadDirPath(directory) + SLASH + fileName;
                    tmpFile.deleteOnExit();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Exceptions.printException(e);
        } finally {
        }

        SysFile sysFile = SysFile.builder().fileName(fileName).fileType(getFileSuffix(imageFile.getAbsolutePath())).storeLocation(serverUploadLocation)
                .filePath(filePath).fileSize(compressImageLength).downLoadUrl(DOWNLOAD_FULL_URL).apiFilePath(DOWNLOAD_FULL_URL_API)
                .anonymousFilePath(DOWNLOAD_FULL_URL_ANONYMOUS).build();
        log.debug("上传文件成功，具体信息为【{}】", sysFile.toString());
        return sysFile;
    }

    /**
     * 从远程URL上传压缩图片
     * @param imageUrl
     * @param quality
     * @param directory 相对路径
     * @return SysFile
     */
    public SysFile uploadCompressImageFromUrl(String imageUrl, float quality, String directory) {
        File imageFile = downloadFileFromUrl(imageUrl);
        return uploadCompressImage(imageFile, quality, directory);
    }

    /**
     * 设置文件上传路径
     */
    public String createAutoUploadDirPath(String directory) {
        StringBuffer dir = new StringBuffer();
        if (Boolean.valueOf(config.getFileDirYmdFlag())){
            dir.append(config.getUploadPath()).append(SLASH).append(DateUtil.getDateStr("yyyy"));
            dir.append(SLASH).append(DateUtil.getDateStr("MM"));
            dir.append(SLASH).append(config.getAppcode());
            dir.append(SLASH).append(directory);
        }else{
            dir.append(directory);
        }
        return replaceSlash(dir.toString());
    }

    /**
     * 设置本地文件上传目录（适用于disk和共享存储方式）
     */
    public File createAutoUploadDirFile(String directory) throws IOException {
        String storePath = createAutoUploadDirPath(directory);
        File targetFileDirectory = new File(storePath);
        if (!targetFileDirectory.exists()) {
            FileUtils.forceMkdir(targetFileDirectory);
            log.debug("目录不存在，即将强制创建路径【{}】", storePath);
        }
        return targetFileDirectory;
    }

    /**
     * 设置本地文件上传目录（适用于disk和共享存储方式）
     */
    public File forceCreateCustomUploadDirectory(String directory) throws IOException {
        String storePath = directory;
        File targetFileDirectory = new File(storePath);
        if (!targetFileDirectory.exists()) {
            FileUtils.forceMkdir(targetFileDirectory);
            log.debug("目录不存在，即将强制创建路径【{}】", storePath);
        }
        return targetFileDirectory;
    }

    /**
     * 创建临时目录
     * @param dir
     * @return File
     */
    public File createTempDirectory(String dir){
        File tempDirectory = new File(config.getUploadTmpFileLocation().concat(ApplicationConstants.SEPARATOR)
                .concat(CodeGenerator.systemUUID()).concat(ApplicationConstants.SEPARATOR).concat(dir));
        try {
            FileUtils.forceMkdir(tempDirectory);
        } catch (IOException e) {
            Exceptions.printException(e);
            return null;
        }
        log.debug("创建的临时目录为【{}】", tempDirectory.getAbsolutePath());
        return tempDirectory;
    }


    /**
     * 创建临时后缀临时文件
     * @return File
     */
    public File createTempFile(){
        return createTempFile(CodeGenerator.randomChar(4));
    }

    /**
     * 创建固定名称的临时文件
     * @param filename
     * @return
     */
    public File createTempFileWithName(String filename){
        File tempFile = null;
        try {
//            tempFile = File.createTempFile("tmp", DOT+getFileSuffix(filename));
            tempFile = new File(config.getUploadTmpFileLocation().concat(ApplicationConstants.SEPARATOR)
                    .concat(CodeGenerator.systemUUID()).concat(ApplicationConstants.SEPARATOR)
                    .concat(filename));
            FileUtils.forceDeleteOnExit(tempFile);
            FileUtils.touch(tempFile);
        } catch (IOException e) {
            Exceptions.printException(e);
        }
        log.debug("创建的临时文件路径为【{}】", tempFile.getAbsolutePath());
        return tempFile;
    }

    /**
     * 创建指定文件名称的临时文件
     * @param dir
     * @param filename
     * @return File
     */
    public File createTempFile(String dir, String filename){
        File tempFile = null;
        try {
            tempFile = new File(config.getUploadTmpFileLocation().concat(ApplicationConstants.SEPARATOR)
                    .concat(CodeGenerator.systemUUID()).concat(ApplicationConstants.SEPARATOR)
                    .concat(dir).concat(ApplicationConstants.SEPARATOR).concat(filename));
            FileUtils.forceDeleteOnExit(tempFile);
            FileUtils.touch(tempFile);
        } catch (IOException e) {
            Exceptions.printException(e);
        }
        log.debug("创建的临时文件路径为【{}】", tempFile.getAbsolutePath());
        return tempFile;
    }


    /**
     * 创建带后缀临时文件
     * @param suffix
     * @return File
     */
    public File createTempFile(String suffix){
        File tempFile = null;
        try {
            tempFile = new File(config.getUploadTmpFileLocation().concat(ApplicationConstants.SEPARATOR)
                    .concat(CodeGenerator.systemUUID() + DOT + suffix));
            FileUtils.forceDeleteOnExit(tempFile);
            FileUtils.touch(tempFile);
        } catch (IOException e) {
            Exceptions.printException(e);
        }
        log.debug("创建的临时文件路径为【{}】", tempFile.getAbsolutePath());
        return tempFile;
    }

    /**
     * 从系统中下载文件
     * @param sysFile
     * @return File
     */
    public File getFileFromSystem(SysFile sysFile){
        if(null == sysFile.getStoreLocation()){
            log.debug("文件记录的存储位置为空，将通过【{}】方式下载文件【{}】", serverUploadLocation, sysFile.getFilePath());
            return getFileFromSystem(serverUploadLocation, sysFile);
        }
        else{
            log.debug("将通过【{}】方式下载文件【{}】", sysFile.getStoreLocation(), sysFile.getFilePath());
            return getFileFromSystem(sysFile.getStoreLocation(), sysFile);
        }
    }

    public File getFileFromSystem(StoreLocation serverUploadLocation, SysFile sysFile){
        Assert.notNull(serverUploadLocation, "文件保存位置不能为空!");
        Assert.notNull(serverUploadLocation, "文件路径不能为空!");
        File realFile = null;
        log.debug("即将以【{}】方式读取文件【{}】",serverUploadLocation, sysFile.getFilePath());
        switch (serverUploadLocation) {
            case disk:
                realFile = new File(sysFile.getFilePath());
                if(!realFile.exists()){
                    realFile = null;
                }
                break;
            case fastdfs:
                realFile = downloadFileFromUrl(config.getAppHostPort() + SLASH + sysFile.getFilePath());
                break;
            case ftp:
                log.debug("基于ftp，方式同sftp");
            case sftp:
                //下载文件时每次打开-关闭FTP太费资源。因此，隐藏下面代码，改为配置Nginx的虚拟目录，直接提供URL进行下载
                //Nginx的虚拟目录参考
//                location  /uploadFiles/ {
//                        alias ${app.file.upload.path};
//                        autoindex on;
//                }
                if(config.isNgEnable()){
                    String nginxFileUrl;
                    if (BooleanUtil.toBoolean( config.getNgCustomUploadFlag()) ){
                        //自定义了Nginx映射目录
                        nginxFileUrl = StringUtils.replace(sysFile.getFilePath(), config.getNgCustomUploadPath(), config.getAppHostPort().concat(NGINX_STATIC_FILE_LOCATION));
                    }
                    else{
                        nginxFileUrl = StringUtils.replace(sysFile.getFilePath(), config.getUploadPath(), config.getAppHostPort().concat(NGINX_STATIC_FILE_LOCATION));
                    }
                    nginxFileUrl = StringUtils.replace(nginxFileUrl, "\\", SLASH);
                    log.debug("即将通过Nginx以【{}】方式通过URL【{}】下载文件【{}】", serverUploadLocation, nginxFileUrl, sysFile.getFileName());
                    realFile = downloadFileFromUrl(nginxFileUrl);
                }
                else {
                    log.warn("即将以【{}】方式通过路径【{}】下载文件【{}】", serverUploadLocation, sysFile.getFilePath(), sysFile.getFileName());
                    String directory = StringUtils.removeEnd(sysFile.getFilePath(), sysFile.getFileName());
                    realFile = sftpUtil.download2File(directory, sysFile.getFileName(), createTempFileWithName(sysFile.getFileName()));
                }
                break;
        }
        return realFile;
    }

    /**
     * 不通过Nginx, 从FTP服务器下载文件
     * @param directory
     * @param filename
     * @return
     */
    public File downloadFileFromDir(String directory, String fileName){
        Assert.notNull(directory, "文件路径不能为空!");
        Assert.notNull(fileName, "文件名称不能为空!");
        File realFile = null;
        switch (serverUploadLocation) {
            case ftp:
                log.debug("基于ftp，方式同sftp");
            case sftp:
                realFile = createTempFile(getFileSuffix(fileName));
                sftpUtil.download2File(directory, fileName, realFile);
                break;
        }
        return realFile;
    }

    /**
     * 不通过Nginx, 从FTP服务器下载文件
     * @param directory
     * @param filename
     * @return
     */
    public byte[] downloadByteFromDir(String directory, String fileName){
        Assert.notNull(directory, "文件路径不能为空!");
        Assert.notNull(fileName, "文件名称不能为空!");
        byte[] realFileByte = null;
        switch (serverUploadLocation) {
            case ftp:
                log.debug("基于ftp，方式同sftp");
            case sftp:
                realFileByte = sftpUtil.download2Byte(directory, fileName);
                break;
        }
        return realFileByte;
    }

    /**
     * 根据远程文件的url下载文件
     * @param fileUrl
     * @return File
     */
    public File downloadFileFromUrl(String fileUrl) {
        Assert.notNull(fileUrl, "下载链接地址不可为空");
        File targetFile = null;
        HttpURLConnection urlConnection = null;
        try {
            UrlFile urlFile = openAndConnectUrl(fileUrl);
            urlConnection = urlFile.getConn();
            if (null != urlConnection && urlConnection.getContentLengthLong() != 0) {
                try {
                    targetFile = createTempFileWithName(urlFile.getFileName());
                    FileUtils.copyURLToFile(urlFile.getConnUrl(), targetFile);
                    log.debug("即将从路径【{}】 下载文件保存至【{}】", fileUrl, targetFile.getAbsolutePath());
                }catch (FileNotFoundException e){
                    log.warn("URL目标文件不存在【{}】，错误信息为【{}】", urlFile.getConnUrl(), e.getMessage());
                    targetFile = null;
                }
            } else {
                log.error("从路径【{}】下载文件发生异常", fileUrl);
            }
        } catch (Exception e) {
            if (urlConnection!= null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
            Exceptions.printException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return targetFile;
    }

    private UrlFile openAndConnectUrl(String remoteFileUrl) throws Exception {
        //URL路径中有/分割的文件名，进行URL编码处理中文，否则不做处理
        String fileName = getFileName(remoteFileUrl);
        String suffix = getFileSuffix(remoteFileUrl);
        URL connUrl = null;
        HttpURLConnection urlConnection = null;
        try {
            //connUrl = new URL(remoteFileUrl);
            //引入galimatias,解决不规范文件名导致文件下载异常
            connUrl = io.mola.galimatias.URL.parse(remoteFileUrl).toJavaURL();
            urlConnection = (HttpURLConnection) connUrl.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod(ApplicationConstants.HTTPGET);
            urlConnection.connect();

        } catch (MalformedURLException e){
            log.warn("URL地址错误【{}】，错误信息为【{}】", remoteFileUrl, e.getMessage());
        }
        return UrlFile.builder().remoteFileUrl(remoteFileUrl).conn(urlConnection).connUrl(connUrl).fileName(fileName).fileSuffix(suffix).build();
    }

    /**
     * 物理删除文件
     * @param sysFile
     * @return boolean
     */
    public boolean deleteFile(SysFile sysFile) {
        if(null == sysFile.getStoreLocation()){
            return deleteFile(serverUploadLocation, sysFile.getFilePath());
        }
        else{
            return deleteFile(sysFile.getStoreLocation(), sysFile.getFilePath());
        }
    }

    public boolean deleteFile(StoreLocation serverUploadLocation, String filePath) {
        Assert.notNull(serverUploadLocation, "文件保存位置不能为空!");
        Assert.notNull(serverUploadLocation, "文件路径不能为空!");
        boolean result = ApplicationConstants.TRUE;
        log.debug("即将以【{}】方式删除文件【{}】",serverUploadLocation, filePath);
        switch (serverUploadLocation) {
            case disk:
                try {
                    FileUtils.forceDelete(new File(filePath));
                } catch (Exception e) {
                    result = ApplicationConstants.FALSE;
                    Exceptions.printException(e);
                }
                break;
            case fastdfs:
                try {
                   // FastDfsClient.deleteFile(filePath);
                } catch (Exception e) {
                    result = ApplicationConstants.FALSE;
                    Exceptions.printException(e);
                }
                break;
            case ftp:
                log.debug("基于ftp，方式同sftp");
            case sftp:
                try {
                    sftpUtil.deleteFile(filePath);
                } catch (Exception e) {
                    result = ApplicationConstants.FALSE;
                    Exceptions.printException(e);
                }
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 读取放置在项目resource目录下的文件，以二进制流的方式方法，
     *  因为项目打包后在读取项目里面的文件必须用流的方式获取，否则用其他方式获取会提示找不到文件
     * @param filePath
     * @return byte[]
     */
    public static byte[] getFileByte(String filePath){
        byte[] fileByte = null;
        try {
            if ( StringUtils.isEmpty( filePath ) ){
                return null;
            }
            ClassPathResource classPathResource = new ClassPathResource( filePath );
            BufferedInputStream bufferedInputStream = new BufferedInputStream( classPathResource.getInputStream() );
            fileByte = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(fileByte);
            bufferedInputStream.close();
        } catch ( IOException e ) {
            Exceptions.printException(e);
        }
        return fileByte;
    }


}
