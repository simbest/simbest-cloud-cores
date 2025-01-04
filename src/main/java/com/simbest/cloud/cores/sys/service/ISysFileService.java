package com.simbest.cloud.cores.sys.service;


import com.simbest.cloud.cores.base.service.ILogicService;
import com.simbest.cloud.cores.sys.model.SysFile;
import com.simbest.cloud.cores.sys.model.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 用途：统一系统文件管理逻辑层
 * 作者: lishuyi
 * 时间: 2018/2/23  10:14
 */
public interface ISysFileService extends ILogicService<SysFile, String> {

    /**
     * 上传并保存单个文件
     * @param multipartFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return SysFile
     */
    SysFile uploadProcessFile(MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart);

    /**
     * 浏览器更新上传单个文件
     * @param multipartFile 上传文件
     * @param pmInsId 流程ID
     * @param id 附件ID
     * @return SysFile
     */
    SysFile updateProcessFile(MultipartFile multipartFile, String pmInsId, String id);

    /**
     * 上传并保存多个文件
     * @param multipartFiles 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return List<SysFile>
     */
    List<SysFile> uploadProcessFiles(Collection<MultipartFile> multipartFiles, String pmInsType, String pmInsId, String pmInsTypePart);

    /**
     * 上传并保存单个文件
     * @param multipartFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @param customFileName   自定义文件名称
     * @param customDirectory  自定义路径
     * @return SysFile
     */
    SysFile uploadProcessFile(MultipartFile multipartFile,String customFileName,String customDirectory,String pmInsType, String pmInsId, String pmInsTypePart);

    /**
     * 上传并保存多个文件
     * @param multipartFiles 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @param customFileName  自定义文件名称
     * @param customDirectory  自定义路径
     * @return List<SysFile>
     */
    List<SysFile> uploadProcessFiles(Collection<MultipartFile> multipartFiles,String customFileName,String customDirectory,String pmInsType, String pmInsId, String pmInsTypePart);


    /**
     * 上传并保存多个文件
     * @param localFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return List<SysFile>
     */
    SysFile uploadLocalProcessFile(File localFile,String pmInsType, String pmInsId, String pmInsTypePart);


    /**
     * 上传并保存多个文件
     * @param localFiles 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @return List<SysFile>
     */
    List<SysFile> uploadLocalProcessFiles(Collection<File> localFiles,String pmInsType, String pmInsId, String pmInsTypePart);


    /**
     * 上传并保存多个文件
     * @param localFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @param customFileName  自定义文件名称
     * @param customDirectory  自定义路径
     * @return List<SysFile>
     */
    SysFile uploadLocalProcessFile(File localFile,String customFileName,String customDirectory,String pmInsType, String pmInsId, String pmInsTypePart);


    /**
     * 上传并保存多个文件
     * @param localFiles 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @param customFileName  自定义文件名称
     * @param customDirectory  自定义路径
     * @return List<SysFile>
     */
    List<SysFile> uploadLocalProcessFiles(Collection<File> localFiles,String customFileName,String customDirectory,String pmInsType, String pmInsId, String pmInsTypePart);


    /**
     * 按照质量压缩图片
     * @param imageFile
     * @param quality   压缩精度 0.5f
     * @param pmInsType
     * @param pmInsId
     * @param pmInsTypePart
     * @return
     */
    SysFile uploadCompressImage(File imageFile, float quality, String pmInsType, String pmInsId, String pmInsTypePart);

    //按照质量压缩图片
    List<SysFile> uploadCompressImages(Collection<MultipartFile> multipartFiles, float quality, String pmInsType, String pmInsId, String pmInsTypePart);


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
    SysFile uploadCompressImage2(File imageFile, String pmInsType, String pmInsId, String pmInsTypePart, String compressWidth, String fileSuffix);

    //按照分辨率压缩图片
    List<SysFile> uploadCompressImages2(Collection<MultipartFile> multipartFiles, String pmInsType, String pmInsId, String pmInsTypePart, String compressWidth, String fileSuffix);

    /**
     * 导入Excel文件--指定某个sheet页
     * @param multipartFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @param clazz 导入对象类
     * @param sheetName sheet页名称
     * @param <T>
     * @return UploadFileResponse
     */
    <T> UploadFileResponse importExcel(MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart, Class<T> clazz, String sheetName);

    /**
     * 导入Excel文件--指定某个sheet页
     * @param multipartFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @param clazz 导入对象类
     * @param sheetName sheet页名称
     * @param inputRow 起始导入行数
     * @param <T>
     * @return UploadFileResponse
     */
    <T> UploadFileResponse importExcel(MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart, Class<T> clazz, String sheetName,int inputRow);

    /**
     * 导入Excel文件--支持多个sheet页
     * @param multipartFile 上传文件
     * @param pmInsType 流程类型
     * @param pmInsId 流程ID
     * @param pmInsTypePart 流程区块
     * @param clazz 导入对象类
     * @param <T>
     * @return UploadFileResponse
     */
    <T> UploadFileResponse importExcel(MultipartFile multipartFile, String pmInsType, String pmInsId, String pmInsTypePart, Class<T> clazz);

    /**
     * 通过SysFile的ID获取实际文件
     * @param id
     * @return File
     */
    File getRealFileById(String id);

    /**
     * 仅删除数据，保留物理文件
     * @param id
     */
    void deleteByIdNoFile(String id);

    /**
     * 扫描不可读取的文件，可成功读取标识为1，因文件乱码等原因不可读取标识为-1，文件记录主键与文件路径名称一致为2
     * @param id
     * @param isLocal
     * @return
     */
    Future<String> findFileByIsLocal(String id, Integer isLocal);

    /**
     * 压缩文件提供下载
     * @param zipFile
     * @param sysFileList
     */
    void addFilesToZip(File zipFile, List<SysFile> sysFileList);

    /**
     * 根据pmInsId查找SysFile
     * @param pmInsId
     */
    List<SysFile> getFilesByPmInsId(String pmInsId);

    /**
     * 根据processInsId查找SysFile
     * @param processInsId
     */
    List<SysFile> getFilesByProcessInsId(String processInsId);
}
