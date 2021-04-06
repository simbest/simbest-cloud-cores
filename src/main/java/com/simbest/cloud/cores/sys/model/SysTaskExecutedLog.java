/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.model;

import com.simbest.cloud.cores.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.dal.model.SystemModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用途：定时任务执行日志
 * 作者: lishuyi
 * 时间: 2018/3/7  23:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SysTaskExecutedLog extends SystemModel {
    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.cloud.cores.distributed.id.SnowflakeId")
    @EntityIdPrefix(prefix = "TL") //主键前缀，此为可选项注解
    private String id;

    @Column(nullable = false, length = 200)
    @NonNull
    private String taskName;

    @Column(nullable = false, length = 50)
    @NonNull
    private String hostname;

    @Column(nullable = false)
    @NonNull
    private Integer port;

    //持续时间(毫秒)
    @Column(nullable = false)
    @NonNull
    private Long durationTime;

    //文件大小
    @Column(length = 4000)
    private String content;

    @NonNull
    @Column(nullable = false)
    //是否可用
    private Boolean executeFlag = true;
}
