/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.base.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 用途：从哪创建用户
 * 作者: lishuyi 
 * 时间: 2019/10/16  14:39
 */
public enum CreateUserFrom implements GenericEnum {

    inner("应用内"),  outter("应用外"), wxma("微信小程序"), wxmp("微信公众号");

    @Setter @Getter
    private String value;

    CreateUserFrom(String value) {
        this.value = value;
    }

}
