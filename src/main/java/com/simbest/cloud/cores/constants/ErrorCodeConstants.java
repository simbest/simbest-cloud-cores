/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.constants;

/**
 * 用途：常见错误信息
 * 作者: lishuyi
 * 时间: 2018/3/24  17:24
 */
public class ErrorCodeConstants {
    public static final String WECHAT_ERROR_COMMUNICATION= "微信服务器通讯异常";
    public static final String WECHAT_ERROR_GET_USER = "微信获取用户信息失败";
    public static final String WECHAT_ERROR_PARSE_USER = "解析微信用户信息异常";
    public static final String WECHAT_ERROR_USER_PHONE = "微信用户手机号码后台未登记";
    public static final String WECHAT_ERROR_CODE_BEEN_USED = "code been used";
    public static final String WECHAT_ERROR_CODE_INVALID_USED = "invalid code";
    public static final String WECHAT_ERROR_CODE_BEEN_USED_MSG = "微信CODE不可重复使用";

    public static final Integer ERRORCODE_ATTACHMENT_SIZE_EXCEEDS = 20;

}
