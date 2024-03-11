package com.simbest.cloud.cores.base.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LogicModel extends SystemModel {

    @Setter
    @Getter
    @Column(nullable = false)
    //是否可用
    private Boolean enabled;

    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime removedTime;

    @Setter
    @Getter
//    @Column(nullable = false, updatable = false)
    @Column(nullable = false) //确保可以更新，在逻辑删除数据后，再次恢复数据有可能创建人不一样
    //创建人 CREATOR
    private String creator;

    @Setter
    @Getter
    @Column(nullable = false)
    //更新人 MODIFIER
    private String modifier;

}