package com.simbest.cloud.cores.sys.model;

import com.simbest.cloud.cores.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.base.model.LogicModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
@EqualsAndHashCode (callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SYS_LOG_OPERATE")
public class SysOperateLog extends LogicModel {

    @Id
    @Column (name = "id", length = 40)
    @GeneratedValue (generator = "snowFlakeId")
    @GenericGenerator (name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix (prefix = "L") //主键前缀，此为可选项注解
    private String id;

    @Column(length = 150)
    @Schema(description = "业务操作主键")
    private String bussinessKey;

    @Column(nullable = false,length = 500)
    @Schema(description = "调用接口")
    private String operateInterface;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Schema(description = "接口参数")
    private String interfaceParam;

    @Column(nullable = false,length = 10)
    @Schema(description = "客户端操作标识")
    private String operateFlag;

    @Column(length = 1500)
    @Schema(description = "错误信息")
    private String errorMsg;

    @Column(length = 2000)
    @Schema(description = "接口返回结果信息")
    private String resultMsg;
}
