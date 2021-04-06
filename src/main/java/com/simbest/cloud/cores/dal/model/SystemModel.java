/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.dal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 用途：系统类实体
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/9  19:22
 */
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class SystemModel extends GenericModel {

    //创建时间
    @Setter
    @Getter
//    @Column(nullable = false, updatable = false)
    @Column(nullable = false)//确保可以更新，在逻辑删除数据后，再次恢复数据有可能创建时间不一样
    @CreationTimestamp// 创建时自动更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdTime;

    //最后更新时间
    @Setter
    @Getter
    @Column(nullable = false)
    @UpdateTimestamp// 更新时自动更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime modifiedTime;

}
