package com.simbest.cloud.cores.security.controller;


import com.simbest.boot.security.IUser;
import com.simbest.cloud.cores.base.model.CheckIpIsDevOpsResult;
import com.simbest.cloud.cores.exceptions.AccesssAppDeniedException;
import com.simbest.cloud.cores.json.JacksonUtils;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.security.utils.LoginUtils;
import com.simbest.cloud.cores.security.utils.SecurityUtils;
import com.simbest.cloud.cores.utils.encrypt.AesEncryptor;
import com.simbest.cloud.cores.utils.server.HostUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.simbest.cloud.cores.constants.ApplicationConstants.*;
import static com.simbest.cloud.cores.constants.AuthoritiesConstants.ACCESS_FORBIDDEN;
import static com.simbest.cloud.cores.constants.ErrorCodeConstants.LOGIN_ERROR_OPT_IP;

/**
 * 用途：首页控制器
 * 作者: lishuyi
 * 时间: 2018/1/31  15:49
 */
@Tag(name = "IndexController", description = "权限管理-首页控制器")
@Slf4j
@Controller
public class IndexController {

    @Autowired
    private AesEncryptor aesEncryptor;

    @Operation(summary = "匿名访问首页", description = "互联网应用为主站点页面，企业应用为登录页面，调整以下welcome模板页面")
    @RequestMapping(value = {"/"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String root(HttpServletRequest request) {
        return "redirect:/welcome";
    }

    @Operation(summary = "匿名访问首页", description = "互联网应用welcome为主站点页面，企业应用welcome为登录页面")
    @GetMapping("/welcome")
    public ModelAndView welcome() {
        return new ModelAndView("welcome");
    }

    @Operation(summary = "匿名安全登录首页", description = "匿名安全登录首页，不需要session")
    @GetMapping("/signin")
    public String signin(HttpServletRequest request) {
        CheckIpIsDevOpsResult checkDevOpsResult = LoginUtils.checkIpIsDevOps(HostUtil.getClientIpAddress(request));
        if(checkDevOpsResult.isCheckRet()){
            return "signin";
        }
        else{
            log.error(String.format(LOGIN_ERROR_OPT_IP, String.join(COMMA, checkDevOpsResult.getWhiteIps()) , checkDevOpsResult.getCurrentIp()));
            throw new AccesssAppDeniedException(ACCESS_FORBIDDEN);
        }
    }

    @Operation(summary = "需要SESSION信息的后台首页", description = "支持SSO单点登录")
    @RequestMapping(value = {"/home", "/sso"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String home(HttpServletRequest request, Model indexModel) {
        String menuId = request.getParameter("menuId");
        indexModel.addAttribute("menuId", menuId);
        log.debug("登录成功后获取url中的参数{}", menuId);
        if(StringUtils.isEmpty(request.getQueryString())){
            return "redirect:/index";
        }
        else {
            return "redirect:/index?" + request.getQueryString();
        }
    }

    @Operation(summary = "需要SESSION信息的后台首页")
    @RequestMapping(value = "/index", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView index(Model indexModel) {
        IUser iuser = SecurityUtils.getCurrentUser();
        indexModel.addAttribute("iuser", iuser);
        return new ModelAndView("index", "indexModel", indexModel);
    }

//    @Operation(summary = "获取当前登陆人信息")
//    @PostMapping(value={"/getCurrentUser","/getCurrentUser/sso","/getCurrentUser/api"})
//    @ResponseBody
//    public JsonResponse getCurrentUser() {
//        IUser iuser = SecurityUtils.getCurrentUser();
//        return JsonResponse.success(iuser);
//    }

    @Operation(summary = "获取当前登陆人信息")
    @PostMapping(value={"/getCurrentUserInfo","/getCurrentUserInfo/sso","/getCurrentUserInfo/api"})
    @ResponseBody
    public JsonResponse getCurrentUserInfo() {
//        IUser iuser = SecurityUtils.getCurrentUser();
//        SimpleUser simpleUser = new SimpleUser();
//        BeanUtils.copyProperties(iuser, simpleUser);
//        simpleUser.setPreferredMobile(aesEncryptor.encrypt(simpleUser.getPreferredMobile()));
//        simpleUser.setEmail(aesEncryptor.encrypt(simpleUser.getEmail()));
//        return JsonResponse.success(simpleUser);
//        com.fasterxml.jackson.databind.JsonMappingException: java.lang.ClassCastException@70be4007 (through reference chain: com.simbest.boot.base.web.response.JsonResponse["data"]->com.simbest.boot.security.SimpleUser["authorities"]->java.util.TreeSet[0]->com.simbest.boot.anddoc.uums.permission.model.SysPermission["authority"])

//        //此方式不会出现JSON解析错误，但只针对了手机号码和邮箱两个字段
//        IUser iuser = SecurityUtils.getCurrentUser();
//        String iuserString = JacksonUtils.obj2json(iuser);
//        JsonNode userNode = JacksonUtils.json2node(iuserString);
//        if(StringUtils.isNotEmpty(iuser.getEmail())) {
//            ((ObjectNode) userNode).put("email", aesEncryptor.encrypt(iuser.getEmail()));
//        }
//        if(StringUtils.isNotEmpty(iuser.getPreferredMobile())) {
//            ((ObjectNode) userNode).put("preferredMobile", aesEncryptor.encrypt(iuser.getPreferredMobile()));
//        }
//        return JsonResponse.success(userNode);

        IUser iuser = SecurityUtils.getCurrentUser();
        if(null != iuser) {
            String aesUser = aesEncryptor.encrypt(JacksonUtils.obj2json(iuser));
            return JsonResponse.success(aesUser, MSG_SUCCESS);
        }
        else {
            return JsonResponse.fail("获取用户身份失败", MSG_FAIL);
        }
    }

    @Operation(summary = "获取当前请求IP地址,判断IPv4或IPv6访问")
    @RequestMapping(value = {"/anonymous/getClientIP"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResponse getClientIP(HttpServletRequest request) {
        return JsonResponse.success(HostUtil.getClientIpAddress(request), MSG_SUCCESS);
    }

    @Operation(summary = "页面转发")
    @RequestMapping(value = {"/anonymous/{pageName}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView redirectView(@PathVariable String pageName) {
        log.debug("redirectView3: Redirect to page: " + pageName);
        return new ModelAndView(pageName);
    }

}
