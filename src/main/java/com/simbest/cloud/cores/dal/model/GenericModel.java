/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 用途：通用实体
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:21
 */
@Setter
@Getter
@MappedSuperclass
public abstract class GenericModel implements Serializable, Comparable {

    @Transient
    //动态排序
    private String orderByClause;

    @Transient
    //时间区间-开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ssDate;

    @Transient
    //时间区间-结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eeDate;

    @Transient
    //分页起始页码
    private Integer pageIndex;

    @Transient
    //分页每页记录数
    private Integer pagesize;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public int compareTo(Object obj) {
        return CompareToBuilder.reflectionCompare(this, obj);
    }
}
