package com.simbest.cloud.cores.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.simbest.cloud.cores.base.enums.StoreLocation;
import com.simbest.cloud.cores.base.service.impl.LogicService;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.exception.Exceptions;
import com.simbest.cloud.cores.exceptions.AppRuntimeException;
import com.simbest.cloud.cores.exceptions.BusinessForbiddenException;
import com.simbest.cloud.cores.sys.model.SysFile;
import com.simbest.cloud.cores.sys.model.UploadFileResponse;
import com.simbest.cloud.cores.sys.repository.SysFileRepository;
import com.simbest.cloud.cores.sys.service.ISysFileService;
import com.simbest.cloud.cores.util.AppFileUtil;
import com.simbest.cloud.cores.util.CodeGenerator;
import com.simbest.cloud.cores.util.SecurityUtils;
import com.simbest.cloud.cores.util.office.ExcelUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.simbest.cloud.cores.config.MultiThreadConfiguration.MULTI_THREAD_BEAN;
import static com.simbest.cloud.cores.sys.model.SysFile.*;
import static com.simbest.cloud.cores.util.AppFileUtil.getFileName;


/**
 * 用途：统一系统文件管理逻辑层
 * 作者: lishuyi
 * 时间: 2018/2/23  10:14
 */
@Slf4j
@Service
@DependsOn(value = {"appFileUtil"})
public class SysFileService extends LogicService<SysFile, String> implements ISysFileService {

    public static final String FILE_ERROR = "文件操作异常【%s】";

    private final SysFileRepository repository;

    @Resource
    private AppFileUtil appFileUtil;

    @Resource
    private AppConfig config;

    public StoreLocation serverUploadLocation;

    public SysFileService(SysFileRepository sysFileRepository) {
        super(sysFileRepository);
        this.repository = sysFileRepository;
    }

    @PostConstruct
    public void init() {
        serverUploadLocation = Enum.valueOf(StoreLocation.class, config.getUploadLocation());
    }

    /**
     * 浏览器上传单个文件
     * @param multipartFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    public SysFile uploadProcessFile(MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart) {
        List<SysFile> fileList = uploadProcessFiles(Arrays.asList(multipartFile), pmInsType, pmInsId, pmInsTypePart);
        return fileList.isEmpty() ? null : fileList.get(0);
    }

    /**
     * 浏览器上传多个文件
     * @param multipartFiles 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    @Transactional
    public List<SysFile> uploadProcessFiles(Collection<MultipartFile> multipartFiles, String pmInsType, String pmInsId, String pmInsTypePart) {
        List<SysFile> sysFileList;
        try {
            sysFileList = appFileUtil.uploadFiles(prepareDirectory(pmInsType, pmInsId, pmInsTypePart), multipartFiles);
            saveSysFileList(pmInsType, pmInsId, pmInsTypePart, sysFileList);
        } catch (Exception e) {
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFileList;
    }

    /**
     * 浏览器上传单个文件，并自定义文件名称、文件路径
     * @param multipartFile 上传文件
     * @param customFileName   自定义文件名称
     * @param customDirectory  自定义路径
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    public SysFile uploadProcessFile (MultipartFile multipartFile,String customFileName,String customDirectory,String pmInsType, String pmInsId, String pmInsTypePart ) {
        List<SysFile> fileList = uploadProcessFiles(Arrays.asList(multipartFile),customFileName, customDirectory, pmInsType, pmInsId, pmInsTypePart);
        return fileList.isEmpty() ? null : fileList.get(0);
    }

    /**
     * 浏览器上传多个文件，并自定义文件名称、文件路径
     * @param multipartFiles 上传文件
     * @param customFileName  自定义文件名称
     * @param customDirectory  自定义路径
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    public List<SysFile> uploadProcessFiles (Collection<MultipartFile> multipartFiles,String customFileName,String customDirectory,String pmInsType, String pmInsId, String pmInsTypePart ) {
        List<SysFile> sysFileList;
        try {
            sysFileList = appFileUtil.customUploadFiles(prepareCustomDirectory(customDirectory, pmInsType, pmInsId, pmInsTypePart), multipartFiles, customFileName);
            saveSysFileList(pmInsType, pmInsId, pmInsTypePart, sysFileList);
        }catch (Exception e) {
            Exceptions.printException(e);
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFileList;
    }

    /**
     * 本地上传单个文件
     * @param localFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    public SysFile uploadLocalProcessFile(File localFile, String pmInsType, String pmInsId, String pmInsTypePart) {
        SysFile sysFile = null;
        try {
            sysFile = appFileUtil.uploadFromLocalAutoDirectory(prepareDirectory(pmInsType, pmInsId, pmInsTypePart), localFile, null);
            saveSysFileList(pmInsType, pmInsId, pmInsTypePart, Arrays.asList(sysFile));
        } catch (Exception e) {
            Exceptions.printException(e);
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFile;
    }

    /**
     * 本地上传多个个文件
     * @param localFiles 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    public List<SysFile> uploadLocalProcessFiles(Collection<File> localFiles, String pmInsType, String pmInsId, String pmInsTypePart) {
        List<SysFile> sysFileList = Lists.newArrayList();
        for(File localFile : localFiles){
            SysFile sysFile = uploadLocalProcessFile(localFile, pmInsType, pmInsId, pmInsTypePart);
            if(null != sysFile){
                sysFileList.add(sysFile);
            }
        }
        return sysFileList;
    }

    /**
     * 本地上传单个文件，并自定义文件名称、文件路径
     * @param localFile 上传文件
     * @param customFileName  自定义文件名称
     * @param customDirectory  自定义路径
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    public SysFile uploadLocalProcessFile(File localFile, String customFileName, String customDirectory, String pmInsType, String pmInsId, String pmInsTypePart) {
        SysFile sysFile = null;
        try {
            sysFile = appFileUtil.uploadFromLocalCustomDirectory(prepareCustomDirectory(customDirectory, pmInsType, pmInsId, pmInsTypePart), localFile, customFileName);
            saveSysFileList(pmInsType, pmInsId, pmInsTypePart, Arrays.asList(sysFile));
        } catch (Exception e) {
            Exceptions.printException(e);
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFile;
    }

    /**
     * 本地上传多个文件，并自定义文件名称、文件路径
     * @param localFiles 上传文件
     * @param customFileName  自定义文件名称
     * @param customDirectory  自定义路径
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return
     */
    @Override
    public List<SysFile> uploadLocalProcessFiles(Collection<File> localFiles, String customFileName, String customDirectory, String pmInsType, String pmInsId, String pmInsTypePart) {
        List<SysFile> sysFileList = Lists.newArrayList();
        for(File localFile : localFiles){
            SysFile sysFile = uploadLocalProcessFile(localFile, customFileName, customDirectory, pmInsType, pmInsId, pmInsTypePart);
            if(null != sysFile){
                sysFileList.add(sysFile);
            }
        }
        return sysFileList;
    }

    /**
     * 按照质量压缩图片
     * @param imageFile
     * @param quality   压缩精度 0.5f
     * @param pmInsType
     * @param pmInsId
     * @param pmInsTypePart
     * @return
     */
    @Override
    public SysFile uploadCompressImage(File imageFile, float quality, String pmInsType, String pmInsId, String pmInsTypePart) {
        SysFile sysFile = null;
        try {
            sysFile = appFileUtil.uploadCompressImage(imageFile, quality, prepareDirectory(pmInsType, pmInsId, pmInsTypePart));
            saveSysFileList(pmInsType, pmInsId, pmInsTypePart, Arrays.asList(sysFile));
        } catch (Exception e) {
            Exceptions.printException(e);
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFile;
    }

    //按照质量压缩图片
    @Override
    public List<SysFile> uploadCompressImages(Collection<MultipartFile> multipartFiles, float quality, String pmInsType, String pmInsId, String pmInsTypePart) {
        List<SysFile> sysFileList = Lists.newArrayList();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                File tempImageFile = appFileUtil.createTempFileWithName(getFileName(multipartFile.getOriginalFilename()));
                SysFile sysFile = uploadCompressImage(tempImageFile, quality, pmInsType, pmInsId, pmInsTypePart);
                sysFileList.add(sysFile);
            }
        } catch (Exception e) {
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFileList;
    }

    /**
     * 按照分辨率压缩图片
     * @param imageFile
     * @param pmInsType
     * @param pmInsId
     * @param pmInsTypePart
     * @param compressWidth 可以为空，传空时，系统默认2000像素
     * @param fileSuffix    为JPG的时候，压缩的大小比较小，所以推荐
     * @return
     */
    @Override
    public SysFile uploadCompressImage2(File imageFile, String pmInsType, String pmInsId, String pmInsTypePart, String compressWidth, String fileSuffix) {
        SysFile sysFile = null;
        try {
            sysFile = appFileUtil.uploadCompressImage2(imageFile, compressWidth, prepareDirectory(pmInsType, pmInsId, pmInsTypePart), fileSuffix);
            saveSysFileList(pmInsType, pmInsId, pmInsTypePart, Arrays.asList(sysFile));
        } catch (Exception e) {
            Exceptions.printException(e);
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFile;
    }

    //按照分辨率压缩图片
    @Override
    public List<SysFile> uploadCompressImages2(Collection<MultipartFile> multipartFiles,String pmInsType, String pmInsId, String pmInsTypePart, String compressWidth, String fileSuffix) {
        List<SysFile> sysFileList = Lists.newArrayList();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                File tempImageFile = appFileUtil.createTempFileWithName(getFileName(multipartFile.getOriginalFilename()));
                multipartFile.transferTo(tempImageFile);
                SysFile sysFile = uploadCompressImage2(tempImageFile, pmInsType, pmInsId, pmInsTypePart, compressWidth, fileSuffix);
                sysFileList.add(sysFile);
            }
        } catch (Exception e) {
            throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
        }
        return sysFileList;
    }

    private String prepareDirectory(String pmInsType, String pmInsId, String pmInsTypePart){
        String pmInsTypePath = StrUtil.isEmpty(pmInsType) ? "" : pmInsType.concat(ApplicationConstants.SLASH);
        String pmInsIdPath = StrUtil.isEmpty(pmInsId) ? "" : pmInsId.concat(ApplicationConstants.SLASH);
        String pmInsTypePartPath = StrUtil.isEmpty(pmInsTypePart) ? "" : pmInsTypePart.concat(ApplicationConstants.SLASH);
        String username = SecurityUtils.getCurrentUserName();
        String directory = StringUtils.removeEnd(pmInsTypePath + username + ApplicationConstants.SLASH
                + CodeGenerator.systemUUID() + ApplicationConstants.SLASH
                + pmInsTypePartPath + pmInsIdPath , ApplicationConstants.SLASH);
        log.debug("上传路径地址为【{}】", directory);
        return directory;
    }

    private String prepareCustomDirectory(String customDirectory, String pmInsType, String pmInsId, String pmInsTypePart){
        customDirectory = customDirectory + ApplicationConstants.SLASH + prepareDirectory(pmInsType, pmInsId, pmInsTypePart);
        customDirectory = StringUtils.removeEnd(customDirectory, ApplicationConstants.SLASH);
        log.debug("自定义上传路径地址为【{}】", customDirectory);
        return customDirectory;
    }

    private void saveSysFileList(String pmInsType, String pmInsId, String pmInsTypePart, List<SysFile> sysFileList){
        for(SysFile sysFile : sysFileList){
            sysFile = super.insert(sysFile); //先保存文件获取ID
            sysFile.setDownLoadUrl(sysFile.getDownLoadUrl().concat("?id="+sysFile.getId())); //修改下载URL，追加ID
            sysFile.setApiFilePath(sysFile.getApiFilePath().concat("?id="+sysFile.getId()));
            sysFile.setAnonymousFilePath(sysFile.getAnonymousFilePath().concat("?id="+sysFile.getId()));
            sysFile.setPmInsType(pmInsType);
            sysFile.setPmInsId(pmInsId);
            sysFile.setPmInsTypePart(pmInsTypePart);
            String mobileFilePath = null;
            String apiFilePath = null;
            String anonymousFilePath = null;
            switch (serverUploadLocation) {
                case fastdfs:
                    mobileFilePath = config.getAppHostPort() + ApplicationConstants.SLASH + sysFile.getFilePath();
                    apiFilePath = mobileFilePath;
                    anonymousFilePath = mobileFilePath;
                    break;
                case disk:
                case ftp:
                case sftp:
//                    mobileFilePath = config.getAppHostPort() + ApplicationConstants.SLASH + config.getAppcode() + sysFile.getDownLoadUrl();
                    mobileFilePath = config.getAppHostPort() + ApplicationConstants.SLASH + config.getAppcode() + sysFile.getAnonymousFilePath();
                    apiFilePath = config.getAppHostPort() + ApplicationConstants.SLASH + config.getAppcode() + sysFile.getApiFilePath();
                    anonymousFilePath = config.getAppHostPort() + ApplicationConstants.SLASH + config.getAppcode() + sysFile.getAnonymousFilePath();
                    break;
                default:
            }
            sysFile.setMobileFilePath( mobileFilePath );
            sysFile.setApiFilePath(apiFilePath);
            sysFile.setAnonymousFilePath(anonymousFilePath);
            super.update(sysFile); //再保存一下更新的值
        }
    }

    @Override
    public <T> UploadFileResponse importExcel(MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart, Class<T> clazz, String sheetName) {
        SysFile sysFile = uploadProcessFile(multipartFile, pmInsType, pmInsId, pmInsTypePart);
        if (sysFile != null) {
            ExcelUtil<T> importUtil = new ExcelUtil<>(clazz);
            File tempFile = appFileUtil.createTempFile();
            try {
                multipartFile.transferTo(tempFile);
                List<T> listData = importUtil.importExcel(sheetName, new FileInputStream(tempFile));
                UploadFileResponse<T> uploadFileResponse = new UploadFileResponse<>();
                uploadFileResponse.setListData(listData);
                uploadFileResponse.setSysFiles(Arrays.asList(sysFile));
                return uploadFileResponse;
            } catch (IOException e) {
                Exceptions.printException(e);
                throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
            }
        }
        return null;
    }

    @Override
    public <T> UploadFileResponse importExcel ( MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart, Class<T> clazz, String sheetName, int inputRow ) {
        SysFile sysFile = uploadProcessFile(multipartFile, pmInsType, pmInsId, pmInsTypePart);
        if (sysFile != null) {
            ExcelUtil<T> importUtil = new ExcelUtil<>(clazz);
            File tempFile = appFileUtil.createTempFile();
            try {
                multipartFile.transferTo(tempFile);
                List<T> listData = importUtil.importExcel(sheetName, new FileInputStream(tempFile),inputRow);
                UploadFileResponse<T> uploadFileResponse = new UploadFileResponse<>();
                uploadFileResponse.setListData(listData);
                uploadFileResponse.setSysFiles(Arrays.asList(sysFile));
                return uploadFileResponse;
            } catch (IOException e) {
                Exceptions.printException(e);
                throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
            }
        }
        return null;
    }

    @Override
    public <T> UploadFileResponse importExcel(MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart, Class<T> clazz) {
        SysFile sysFile = uploadProcessFile(multipartFile, pmInsType, pmInsId, pmInsTypePart);
        if (sysFile != null) {
            ExcelUtil<T> importUtil = new ExcelUtil<>(clazz);
            File tempFile = appFileUtil.createTempFile();
            try {
                multipartFile.transferTo(tempFile);
                Map<String, List<T>> mapData = importUtil.importExcel(new FileInputStream(tempFile));
                UploadFileResponse<T> uploadFileResponse = new UploadFileResponse<>();
                uploadFileResponse.setMapData(mapData);
                uploadFileResponse.setSysFiles(Arrays.asList(sysFile));
                return uploadFileResponse;
            } catch (IOException e) {
                Exceptions.printException(e);
                throw new AppRuntimeException(String.format(FILE_ERROR, e.getMessage()));
            }
        }
        return null;
    }

    @Override
    public File getRealFileById(String id) {
        SysFile sysFile = this.findById(id);
        return appFileUtil.getFileFromSystem(sysFile);
    }

    @Override
    @Transactional
    public void deleteById ( String id ) {
        SysFile sysFile = this.findById(id);
        String filePath = sysFile.getFilePath();
        super.deleteById(id);
        boolean result = appFileUtil.deleteFile(sysFile);
        log.warn("物理删除文件结果为【{}】", result);
    }

    @Override
    public void deleteByIdNoFile(String id) {
        super.deleteById(id);
    }

    /**
     * 扫描不可读取的文件，可成功读取标识为1，因文件乱码等原因不可读取标识为-1，文件记录主键与文件路径名称一致为2
     * @param id
     * @param isLocal
     * @return
     */
    @Override
    @Async(MULTI_THREAD_BEAN)
    public Future<String> findFileByIsLocal(String id, Integer isLocal) {
        Date startDate = new Date();
        SysFile param = null;
        if(StringUtils.isNotEmpty(id)){
            param = new SysFile();
            param.setId(id);
        }
        else if (null != isLocal){
            param = new SysFile();
            param.setIsLocal(isLocal);
        }
        else{
            throw new BusinessForbiddenException("查询系统文件记录参数不能为空");
        }
        Specification<SysFile> condition = repository.getSpecification(param);
        int updateCount = 0;
        List<SysFile> sysFileList = this.findAllNoPage(condition);
        log.info("###############参数【{}】记录数【{}】", isLocal, sysFileList.size() );
        for(SysFile sysFile : sysFileList){
            //获取真实文件
            File realFile = getRealFileById(sysFile.getId());
            if(null == realFile || !realFile.exists()){
                //文件不可读取
                sysFile.setIsLocal(NOT_EXIST_FILE);
            }
            else{
                if(sysFile.getId().equals(FilenameUtils.getBaseName(sysFile.getFilePath()))){
                    sysFile.setIsLocal(EXIST_FILE_SAME_NAME_WITH_ID);
                }
                else {
                    sysFile.setIsLocal(EXIST_FILE);
                }
            }
            try {
                update(sysFile);
                updateCount++;
            }catch (Exception e){
                log.error("更新文件失败：【{}】", sysFile);
            }
        }
        Date endDate = new Date();
        long duration = (endDate.getTime() - startDate.getTime()) / 1000;
        String result = String.format("基于【%s】读取到待扫描的文件记录共【%s】条，成功更新多少条【%s】，持续时长【%s】秒",
                System.getProperty("file.encoding"), sysFileList.size(), updateCount, duration);
        log.info(result);
        return new AsyncResult<>(result);
    }

    /**
     * 压缩文件提供下载
     * @param zipFile
     * @param sysFileList
     */
    public void addFilesToZip(File zipFile, List<SysFile> sysFileList) {
        if(null != zipFile && !sysFileList.isEmpty()) {
            try {
                FileOutputStream out = new FileOutputStream(zipFile.getCanonicalFile());
                ZipOutputStream zos = new ZipOutputStream(out);
                for (SysFile sysFile : sysFileList) {
                    //获取真实文件
                    File realFile = getRealFileById(sysFile.getId());
                    if (null != realFile && realFile.exists()) {
                        zos.putNextEntry(new ZipEntry(sysFile.getFileName()));
                        copyFile(realFile, zos);
                        zos.closeEntry();
                    }
                }
                zos.close();
                out.close();
                log.info("处理压缩文件标识【{}】文件名称【{}】成功",
                        sysFileList.stream().map(obj -> obj.getId()).collect(Collectors.joining(ApplicationConstants.COMMA)),
                        sysFileList.stream().map(obj -> obj.getFileName()).collect(Collectors.joining(ApplicationConstants.COMMA)));
            } catch (IOException e) {
                log.error("尝试压缩文件标识【{}】文件名称【{}】发生异常",
                        sysFileList.stream().map(obj -> obj.getId()).collect(Collectors.joining(ApplicationConstants.COMMA)),
                        sysFileList.stream().map(obj -> obj.getFileName()).collect(Collectors.joining(ApplicationConstants.COMMA)));
            }
        }
    }

    private void copyFile(File file, OutputStream os) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while((len = fis.read(buffer)) > 0){
            os.write(buffer, 0, len);
        }
        fis.close();
    }

}
