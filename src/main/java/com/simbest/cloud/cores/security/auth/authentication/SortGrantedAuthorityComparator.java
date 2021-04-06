/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.authentication;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 用途：用户角色权限比较器
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/11  10:28
 */
public class SortGrantedAuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

    @Override
    public int compare(GrantedAuthority o1, GrantedAuthority o2) {
        return o1.equals(o2) ? 0 : -1;
    }

}
