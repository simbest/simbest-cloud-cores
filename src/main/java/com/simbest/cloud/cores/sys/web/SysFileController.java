/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;


import com.alibaba.nacos.shaded.com.google.common.collect.ImmutableList;
import com.simbest.cloud.cores.base.enums.StoreLocation;
import com.simbest.cloud.cores.base.web.controller.LogicController;
import com.simbest.cloud.cores.base.web.response.JsonResponse;
import com.simbest.cloud.cores.config.AppConfig;
import com.simbest.cloud.cores.constants.ApplicationConstants;
import com.simbest.cloud.cores.sys.model.SysFile;
import com.simbest.cloud.cores.sys.model.UploadFileResponse;
import com.simbest.cloud.cores.sys.service.ISysFileService;
import com.simbest.cloud.cores.util.AppFileUtil;
import com.simbest.cloud.cores.util.UrlEncoderUtils;
import com.simbest.cloud.cores.util.encrypt.Md5Encryptor;
import com.simbest.cloud.cores.util.encrypt.UrlEncryptor;
import com.simbest.cloud.cores.util.encrypt.WebOffice3Des;
import com.simbest.cloud.cores.util.http.BrowserUtil;
import com.simbest.cloud.cores.util.json.JacksonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.Future;

import static com.simbest.cloud.cores.util.AppFileUtil.NGINX_STATIC_FILE_LOCATION;
import static org.apache.commons.lang3.StringUtils.EMPTY;


/**
 * 用途：统一系统文件管理控制器
 * 作者: lishuyi https://www.mkyong.com/spring-boot/spring-boot-file-upload-example-ajax-and-rest/
 * 时间: 2018/2/23  10:14
 */
@Tag(name = "SysFileController", description = "系统管理-文件管理")
@Slf4j
@Controller
@RequestMapping("/sys/file")
public class SysFileController extends LogicController<SysFile, String> {

    public final static String UPLOAD_PROCESS_FILES_URL = "/uploadProcessFiles";
    public final static String UPLOAD_PROCESS_FILES_URL_SSO = "/uploadProcessFiles/sso";
    public final static String UPLOAD_PROCESS_FILES_URL_API = "/uploadProcessFiles/api";
    public final static String UPLOAD_PROCESS_FILES_URL_REST = "/uploadProcessFiles/rest";
    public final static String UPLOAD_PROCESS_FILES_URL_REST_SSO = "/uploadProcessFiles/rest/sso";
    public final static String UPLOAD_PROCESS_FILES_URL_REST_API = "/uploadProcessFiles/rest/api";
    public final static String DOWNLOAD_URL = "/download";
    public final static String DOWNLOAD_URL_SSO = "/download/sso";
    public final static String DOWNLOAD_URL_API = "/download/api";
    public final static String DOWNLOAD_URL_ANONYMOUSI = "/download/anonymous";
    public final static String DOWNLOAD_FULL_URL = "/sys/file/download";
    public final static String DOWNLOAD_FULL_URL_API = "/sys/file/download/api";
    public final static String DOWNLOAD_FULL_URL_ANONYMOUS = "/sys/file/download/anonymous";
    public final static String OPEN_URL = "/open";
    public final static String OPEN_URL_SSO = "/open/sso";
    public final static String OPEN_URL_API = "/open/api";
    public final static String OPEN_URL_ANONYMOUS = "/open/anonymous";
    public final static String DELETE_URL = "/deleteById";

    @Autowired
    private ISysFileService fileService;

    @Autowired
    private UrlEncryptor urlEncryptor;

    @Autowired
    private AppFileUtil appFileUtil;

    @Setter
    @Autowired
    private AppConfig config;

    @Autowired
    public SysFileController(ISysFileService fileService) {
        super(fileService);
        this.fileService = fileService;
    }

    @PostMapping(value = {"/uploadFile", "/uploadFile/sso", "/uploadFile/api"})
    @ResponseBody
    public JsonResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "pmInsType", required = false) String pmInsType,
                                   @RequestParam(value = "pmInsId", required = false) String pmInsId,
                                   @RequestParam(value = "pmInsTypePart", required = false) String pmInsTypePart) {
        SysFile sysFile = fileService.uploadProcessFile(file, pmInsType, pmInsId, pmInsTypePart);
        JsonResponse jsonResponse;
        if (null != sysFile) {
            UploadFileResponse uploadFileResponse = new UploadFileResponse();
            uploadFileResponse.setSysFiles(ImmutableList.of(sysFile));
            jsonResponse = JsonResponse.success(uploadFileResponse);
        } else {
            jsonResponse = JsonResponse.defaultErrorResponse();
        }
        return jsonResponse;
    }

    @Operation(summary = "传统方式上传附件（支持IE8）,支持关联流程", description = "会保存到数据库SYS_FILE")
    @PostMapping(value = {UPLOAD_PROCESS_FILES_URL, UPLOAD_PROCESS_FILES_URL_SSO, UPLOAD_PROCESS_FILES_URL_API})
    @ResponseBody
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResponse jsonResponse = doUploadFile(request, response);
        String result = "<script type=\"text/javascript\">parent.result=" + JacksonUtils.obj2json(jsonResponse) + "</script>";
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        out.close();
    }

    @Operation(summary = "REST方式上传附件,支持关联流程", description = "会保存到数据库SYS_FILE")
    @PostMapping(value = {UPLOAD_PROCESS_FILES_URL_REST, UPLOAD_PROCESS_FILES_URL_REST_SSO, UPLOAD_PROCESS_FILES_URL_REST_API})
    @ResponseBody
    public ResponseEntity<?> uploadFileRest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResponse jsonResponse = doUploadFile(request, response);
        return new ResponseEntity(jsonResponse, HttpStatus.OK);
    }


    @Operation(summary = "传统方式分辨率压缩图片（支持IE8）,支持关联流程", description = "会保存到数据库SYS_FILE")
    @PostMapping(value = {"/uploadCompressImages2", "/uploadCompressImages2/sso","/uploadCompressImages2/api"})
    public void uploadCompressImages2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResponse jsonResponse = doUploadCompressImages2(request, response);
        String result = "<script type=\"text/javascript\">parent.result=" + JacksonUtils.obj2json(jsonResponse) + "</script>";
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        out.close();
    }

    @Operation(summary = "REST方式分辨率压缩图片,支持关联流程", description = "会保存到数据库SYS_FILE")
    @PostMapping(value = {"/uploadCompressImages2/rest", "/uploadCompressImages2/rest/sso","/uploadCompressImages2/rest/api"})
    @ResponseBody
    public ResponseEntity<?> uploadCompressImages2Rest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResponse jsonResponse = doUploadCompressImages2(request, response);
        return new ResponseEntity(jsonResponse, HttpStatus.OK);
    }

    /**
     * 上传文件,支持关联流程
     */
    private JsonResponse doUploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Set<MultipartFile> uploadingFileSet = Sets.newHashSet();
        MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
        //优先通过指定参数名称file获取文件
        Collection<MultipartFile> uploadingFileList = mureq.getFiles("file");
        uploadingFileList.forEach(f -> uploadingFileSet.add(f));
        //再通过不指定参数名称获取文件
        Map<String, MultipartFile> multipartFiles = mureq.getFileMap();
        multipartFiles.values().forEach(f -> uploadingFileSet.add(f));
        List<SysFile> sysFiles = fileService.uploadProcessFiles(uploadingFileSet,
                request.getParameter("pmInsType"),
                request.getParameter("pmInsId"),
                request.getParameter("pmInsTypePart"));
        JsonResponse jsonResponse;
        if (!sysFiles.isEmpty()) {
            UploadFileResponse uploadFileResponse = new UploadFileResponse();
            uploadFileResponse.setSysFiles(sysFiles);
            jsonResponse = JsonResponse.success(uploadFileResponse);
        } else {
            jsonResponse = JsonResponse.defaultErrorResponse();
        }
        return jsonResponse;
    }

    /**
     * 上传按照分辨率压缩图片
     */
    private JsonResponse doUploadCompressImages2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Set<MultipartFile> uploadingFileSet = Sets.newHashSet();
        MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
        //优先通过指定参数名称file获取文件
        Collection<MultipartFile> uploadingFileList = mureq.getFiles("file");
        uploadingFileList.forEach(f -> uploadingFileSet.add(f));
        //再通过不指定参数名称获取文件
        Map<String, MultipartFile> multipartFiles = mureq.getFileMap();
        multipartFiles.values().forEach(f -> uploadingFileSet.add(f));
        List<SysFile> sysFiles = fileService.uploadCompressImages2(uploadingFileSet,
                request.getParameter("pmInsType"),
                request.getParameter("pmInsId"),
                request.getParameter("pmInsTypePart"),
                request.getParameter("compressWidth"),
                request.getParameter("fileSuffix"));
        JsonResponse jsonResponse;
        if (!sysFiles.isEmpty()) {
            UploadFileResponse uploadFileResponse = new UploadFileResponse();
            uploadFileResponse.setSysFiles(sysFiles);
            jsonResponse = JsonResponse.success(uploadFileResponse);
        } else {
            jsonResponse = JsonResponse.defaultErrorResponse();
        }
        return jsonResponse;
    }

    /**
     * 下载视频文件(解决iOS操作系统播放MP4)
     * 参考：https://blog.csdn.net/weixin_42553179/article/details/100008911
     *
     * @param request
     * @param id
     * @throws IOException
     */
    @GetMapping(value = {"/downloadVideo", "/downloadVideo/sso", "/downloadVideo/api", "/downloadVideo/anonymous"})
    @ResponseBody
    public void downloadVideo(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) throws IOException {
        SysFile sysFile = fileService.findById(id);
        Assert.notNull(sysFile, String.format("通过文件Id【%s】无法获取文件", id));
        //获取真实文件
        File realFile = fileService.getRealFileById(id);
        RandomAccessFile randomFile = new RandomAccessFile(realFile, "r");//只读模式
        long contentLength = randomFile.length();
        String range = request.getHeader("Range");
        int start = 0, end = 0;
        if (range != null && range.startsWith("bytes=")) {
            String[] values = range.split("=")[1].split("-");
            start = Integer.parseInt(values[0]);
            if (values.length > 1) {
                end = Integer.parseInt(values[1]);
            }
        }
        int requestSize = 0;
        if (end != 0 && end > start) {
            requestSize = end - start + 1;
        } else {
            requestSize = Integer.MAX_VALUE;
        }
        String fileType = AppFileUtil.getFileType(realFile); //支持 video/mp4
        log.debug("下载文件【{}】类型为【{}】", sysFile.getFileName(), fileType);
//        response.setContentType("video/mp4");
        response.setContentType(fileType);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", sysFile.getFileName());
        response.setHeader("Last-Modified", new Date().toString());
        //第一次请求只返回content length来让客户端请求多次实际数据
        if (range == null) {
            log.debug("分段请求Range区间【{}】", range);
            response.setHeader("Content-length", contentLength + "");
        } else {
            log.debug("分段请求Range区间【{}】", range);
            //以后的多次以断点续传的方式来返回视频数据
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);//206
            long requestStart = 0, requestEnd = 0;
            String[] ranges = range.split("=");
            if (ranges.length > 1) {
                String[] rangeDatas = ranges[1].split("-");
                requestStart = Integer.parseInt(rangeDatas[0]);
                if (rangeDatas.length > 1) {
                    requestEnd = Integer.parseInt(rangeDatas[1]);
                }
            }
            long length = 0;
            if (requestEnd > 0) {
                length = requestEnd - requestStart + 1;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
            } else {
                length = contentLength - requestStart;
                response.setHeader("Content-length", "" + length);
                response.setHeader("Content-Range", "bytes " + requestStart + "-" + (contentLength - 1) + "/" + contentLength);
            }
        }
        ServletOutputStream out = response.getOutputStream();
        int needSize = requestSize;
        randomFile.seek(start);
        while (needSize > 0) {
            byte[] buffer = new byte[4096];
            int len = randomFile.read(buffer);
            if (needSize < buffer.length) {
                out.write(buffer, 0, needSize);
            } else {
                out.write(buffer, 0, len);
                if (len < buffer.length) {
                    break;
                }
            }
            needSize -= buffer.length;
        }
        randomFile.close();
        out.close();
    }

    /**
     * 下载文件(图片和通用文档)
     *
     * @param request
     * @param id
     * @return JsonResponse
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    @Operation(summary = "下载文件")
    @GetMapping(value = {DOWNLOAD_URL, DOWNLOAD_URL_SSO, DOWNLOAD_URL_API, DOWNLOAD_URL_ANONYMOUSI})
    @ResponseBody
    public ResponseEntity<?> download(HttpServletRequest request, @RequestParam("id") String id) throws IOException {
        SysFile sysFile = fileService.findById(id);
        Assert.notNull(sysFile, String.format("通过文件Id【%s】无法获取文件", id));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        //获取真实文件
        File realFile = fileService.getRealFileById(id);

        //设置文件类型
        String fileType = AppFileUtil.getFileType(realFile); // text/plain image/jpeg image/png video/mp4  application/vnd.openxmlformats-officedocument.wordprocessingml.document 等文件类型
        log.debug("下载文件【{}】类型为【{}】", sysFile.getFileName(), fileType);
        String[] fileTypes = StringUtils.split(fileType, ApplicationConstants.SLASH);
        //1-响应图片
        if (AppFileUtil.isImage(realFile)) {
            headers.setContentType(new MediaType(fileTypes[0], fileTypes[1]));
        }
        //2-响应视频--iOS操作系统无法播放，可使用/downloadVideo
        else if ("video".equals(fileTypes[0])) {
            headers.setContentType(new MediaType(fileTypes[0], fileTypes[1]));
        }
        //3-响应文件流
        else {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }

        //设置文件名称
        boolean isMSIE = BrowserUtil.isMSBrowser(request);
        String fileName;
        if(StringUtils.contains(request.getRequestURI(), "anonymous")){
//            fileName = CodeGenerator.timestampRandomLast();
            fileName = Md5Encryptor.getFileMd5(realFile)+ApplicationConstants.DOT+AppFileUtil.getFileSuffix(realFile.getName());
        }
        else {
            if (isMSIE) {
                fileName = URLEncoder.encode(sysFile.getFileName(), ApplicationConstants.UTF_8);
            } else {
                fileName = new String(sysFile.getFileName().getBytes(ApplicationConstants.UTF_8), "ISO-8859-1");
            }
        }
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");

        //设置文件大小
        RandomAccessFile randomFile = new RandomAccessFile(realFile, "r");//只读模式
        headers.setContentLength(randomFile.length());
        randomFile.close();

        //输出文件
        Resource resource = new InputStreamResource(new FileInputStream(realFile));
        return ResponseEntity.ok().headers(headers).body(resource);
    }


    /**
     * 借助Nginx直接访问文件, 依赖于Nginx的反向代理
     *
     * @param id
     * @param uploadPath
     * @return String
     */
    @GetMapping(value = {"/ngopen", "/ngopen/sso", "/ngopen/api"})
    public String ngopen(@RequestParam("id") String
                                 id, @RequestParam(value = "uploadPath", required = false) String uploadPath) {
        SysFile sysFile = fileService.findById(id);
        Assert.notNull(sysFile, String.format("通过文件Id【%s】无法获取文件", id));
        log.debug("尝试预览文件地址为【{}】", sysFile.getFilePath());
        if (StringUtils.isEmpty(uploadPath)) {
            uploadPath = config.getUploadPath();
        }
        String nginxUrl = config.getAppHostPort() + NGINX_STATIC_FILE_LOCATION + StringUtils.remove(sysFile.getFilePath(), uploadPath);
        log.debug("转换后webOfficeUrl地址为【{}】", nginxUrl);
        return "redirect:" + nginxUrl;
    }


    /**
     * 在线预览文件, 适用于保存在FastDfs和共享存储Disk中的文件, 并且依赖http://www.officeweb365.com/
     *
     * @param id
     * @return String
     * @throws Exception
     */
    @GetMapping(value = {OPEN_URL, OPEN_URL_SSO, OPEN_URL_API, OPEN_URL_ANONYMOUS})
    public String open(@RequestParam("id") String id) throws Exception {
        String redirectUrl;
        String fileUrl = null;
        SysFile sysFile = fileService.findById(id);
        Assert.notNull(sysFile, String.format("通过文件Id【%s】无法获取文件", id));
        log.debug("尝试预览文件信息为【{}】", sysFile);
        if(StoreLocation.fastdfs.equals(sysFile.getStoreLocation())) {
            fileUrl = config.getAppHostPort() + ApplicationConstants.SLASH + sysFile.getFilePath();
        }
        if(StoreLocation.disk.equals(sysFile.getStoreLocation())) {
            fileUrl = sysFile.getAnonymousFilePath();
        }
        redirectUrl = getOfficeweb365Url(fileUrl);
        log.debug("转换前地址【{}】，转换后webOfficeUrl地址为【{}】", fileUrl, redirectUrl);
        return "redirect:" + redirectUrl;
    }

    /**
     * 在线预览文件, 支持任意免认证的URL, 并且依赖http://www.officeweb365.com/
     *
     * @param url url必须可以匿名直接访问
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = {"/openurl", "/openurl/sso", "/openurl/api"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String openurl(@RequestParam String url) throws Exception {
        return "redirect:" + getOfficeweb365Url(url);
    }

    /**
     * 在线预览文件，支持任意免认证的URL,不进行重定向, 并且依赖http://www.officeweb365.com/
     *
     * @param url url必须可以匿名直接访问
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/get/url", "/get/url/sso", "/get/url/api"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseEntity openurlNoRedirect(@RequestParam String url) throws Exception {
        return new ResponseEntity(JsonResponse.success(getOfficeweb365Url(url), EMPTY), HttpStatus.OK);
    }

    /**
     * 获取Officeweb365在预览文件时浏览器重定向后的URL
     * 说明如下：
     * 1、原始文件地址url：http://10.92.81.163:8088/wgjk/sys/file/download/anonymous?id=F316872804598370304
     * 2、先获取在浏览器通过Officeweb365可以通过的地址，如：
     * http://10.92.81.163:8088/webOffice/?furl=KL59YOPs3YY4Zu7UwAeLOLGmw6gush8pYGkMaXY6kxr5PyH1brzeAedxcO0C4xWD@4vGoKD65@S6wfJkJluaQgHA4eB@ahVltc9Hzg1rZTev@kCQbpihBg==
     * 3、再转换为浏览器打开后重定向的地址，如：
     * 返回结果
     * http://10.92.81.163:8088/e/MTAuOTIuODEuMTYzLjgwODhceGxzeDEueGxzeA%3d%3d?furl=KL59YOPs3YY4Zu7UwAeLOLGmw6gush8pYGkMaXY6kxr5PyH1brzeAedxcO0C4xWD%404vGoKD65%40S6wfJkJluaQgHA4eB%40ahVltc9Hzg1rZTev%40kCQbpihBg%3D%3D
     *
     * @param url url必须可以匿名直接访问
     * @return
     * @throws Exception
     */
/*    @RequestMapping(value = {"/getRedirectUrl", "/getRedirectUrl/sso", "/getRedirectUrl/api"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseEntity getOfficeweb365RedirectUrl(@RequestParam String url) throws Exception {
        //先获取在浏览器通过Officeweb365可以通过的地址
        url = getOfficeweb365Url(url);
        //再转换为浏览器打开后重定向的地址
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        String redirectUrl = webClient.getPage(url).getUrl().toString();
        webClient.close(); //关闭webclient
        return new ResponseEntity(JsonResponse.success(redirectUrl, EMPTY), HttpStatus.OK);
    }*/

    private String getOfficeweb365Url(String url) throws Exception {
        if (UrlEncoderUtils.hasUrlEncoded(url)) {
            url = urlEncryptor.decrypt(url);
        }
        log.debug("尝试预览文件地址为【{}】", url);
        String redirectUrl = config.getAppHostPort() + "/webOffice/?furl=" + WebOffice3Des.encode(url);
        log.debug("转换后webOfficeUrl地址为【{}】", redirectUrl);
        return redirectUrl;
    }

    /**
     * 删除数据，并物理删除文件
     * @param id
     * @return
     */
    @PostMapping(value = DELETE_URL)
    @ResponseBody
    public JsonResponse deleteById(@RequestParam("id") String id) {
        fileService.deleteById(id);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * 仅删除数据，保留物理文件
     * @param id
     * @return
     */
    @PostMapping(value = {"/deleteByIdNoFile", "/deleteByIdNoFile/api", "/deleteByIdNoFile/sso"})
    @ResponseBody
    public JsonResponse deleteByIdNoFile(@RequestParam("id") String id) {
        fileService.deleteByIdNoFile(id);
        return JsonResponse.defaultSuccessResponse();
    }


    @Override
    @ResponseBody
    @PostMapping(value = {"/update", "/update/api", "/update/sso"})
    public JsonResponse update(@RequestBody SysFile sysFile) {
        JsonResponse jsonResponse = super.update(sysFile);
        return jsonResponse;
    }



    /**
     * 扫描不可读取的文件，可成功读取标识为1，因文件乱码等原因不可读取标识为-1，文件记录主键与文件路径名称一致为2
     * @param id    文件主键ID
     * @param isLocal 文件是否可读扫描标识
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/scanCheckRealFile"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResponse scanCheckRealFile(@RequestParam(required=false) String id, @RequestParam(required=false) Integer isLocal) throws Exception {
        Future<String> scanResult = fileService.findFileByIsLocal(id, isLocal);
        return JsonResponse.success(scanResult.get());
    }


    @GetMapping(value = {"/downloadZip", "/downloadZip/sso", "/downloadZip/api", "/downloadZip/anonymous"})
    @ResponseBody
    public ResponseEntity<?> downloadZip(HttpServletRequest request, @RequestParam("ids") String ids) throws IOException {
        final String[] idList = StringUtils.split(ids, ApplicationConstants.COMMA);
        List<SysFile> sysFileList = Lists.newArrayList();
        for(String id : idList){
            SysFile sysFile = fileService.findById(id);
            if(null != sysFile) {
                sysFileList.add(sysFile);
            }
        }
        File tempZipFile = appFileUtil.createTempFile("zip");
        fileService.addFilesToZip(tempZipFile, sysFileList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);


        //设置文件名称
        boolean isMSIE = BrowserUtil.isMSBrowser(request);
        String fileName;
        if(StringUtils.contains(request.getRequestURI(), "anonymous")){
            fileName = Md5Encryptor.getFileMd5(tempZipFile)+ApplicationConstants.DOT+AppFileUtil.getFileSuffix(tempZipFile.getName());
        }
        else {
            if (isMSIE) {
                fileName = URLEncoder.encode(tempZipFile.getName(), ApplicationConstants.UTF_8);
            } else {
                fileName = new String(tempZipFile.getName().getBytes(ApplicationConstants.UTF_8), "ISO-8859-1");
            }
        }
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");

        //设置文件大小
        RandomAccessFile randomFile = new RandomAccessFile(tempZipFile, "r");//只读模式
        headers.setContentLength(randomFile.length());
        randomFile.close();

        //输出文件
        Resource resource = new InputStreamResource(new FileInputStream(tempZipFile));
        return ResponseEntity.ok().headers(headers).body(resource);
    }




}
