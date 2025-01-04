/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.model;

import com.simbest.cloud.cores.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.base.model.LogicModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
/**
 * 用途：系统健康检查模型
 * 作者: lishuyi
 * 时间: 2019/12/9  9:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "系统管理-系统健康检查模型")
public class SysHealth {

    private Boolean result;

    private String message;

    //应用主机
    private String hostIp;

    //应用主机端口
    private Integer hostPort;

}
