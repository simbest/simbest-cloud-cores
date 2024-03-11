/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.model;


import com.simbest.cloud.cores.base.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.base.model.LogicModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;



/**
 * 用途：数据字典
 * 作者: lishuyi
 * 时间: 2018/1/30  17:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_dict")
public class SysDict extends LogicModel {

    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.cloud.cores.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "D") //主键前缀，此为可选项注解
    private String id;

    @Schema(name = "字典代码")
    @Column(nullable = false, unique = true)
    private String dictType;

    @Schema(name = "字典名称")
    @Column(nullable = false, length = 200)
    private String name;

    @Schema(name = "字典描述")
    @Column
    private String description;

    @Schema(name = "字典排序")
    @Column
    private Integer displayOrder;

    @Schema(name = "父亲节点外键")
    @Column
    private String parentId;

    @Schema(name = "流程类型标识")
    @Column(length = 50)
    private String flag;

    @Schema(name = "扩展字段1")
    @Column(length = 200)
    private String spare1;

    @Schema(name = "扩展字段2")
    @Column(length = 200)
    private String spare2;

    //公共字典，blocid和corpid保持为空，所有集团、企业通用
    //非公共字段，blocid和corpid不能为空
    //默认公共，即isPublic=true，数据库字段值=1
    @Schema(name = "是否公共字典")
    @Column
    private Boolean isPublic;


}
