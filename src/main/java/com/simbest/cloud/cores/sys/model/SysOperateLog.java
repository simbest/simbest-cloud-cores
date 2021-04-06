package com.simbest.cloud.cores.sys.model;

import com.simbest.cloud.cores.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.dal.model.LogicModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * <strong>Title : SysOperateLog</strong><br>
 * <strong>Description : 系统操作日志</strong><br>
 * <strong>Create on : 2018/10/10</strong><br>
 * <strong>Modify on : 2018/10/10</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SYS_LOG_OPERATE")
public class SysOperateLog extends LogicModel {

    @Id
    @Column (name = "id", length = 40)
    @GeneratedValue (generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.cloud.cores.distributed.id.SnowflakeId")
    @EntityIdPrefix (prefix = "L") //主键前缀，此为可选项注解
    private String id;

    @Column(length = 150)
    @ApiModelProperty(value = "业务操作主键")
    private String bussinessKey;

    @Column(nullable = false,length = 500)
    @ApiModelProperty(value = "调用接口")
    private String operateInterface;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "接口参数")
    private String interfaceParam;

    @Column(nullable = false,length = 10)
    @ApiModelProperty(value = "客户端操作标识")
    private String operateFlag;

    @Column(length = 1500)
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    @Column(length = 2000)
    @ApiModelProperty(value = "接口返回结果信息")
    private String resultMsg;
}
