/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.model;


import com.simbest.cloud.cores.base.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.base.model.LogicModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;



/**
 * 用途：实体自定义字段值
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_custom_field_value")
public class SysCustomFieldValue extends LogicModel {

    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.cloud.cores.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "V") //主键前缀，此为可选项注解
    private String id;

    //所属实体分类
    @Column(nullable = false)
    private String fieldClassify;

    //所属实体分类主键
    @Column(nullable = false)
    private Long fieldEntityId;

    @Column(nullable = false)
    private String customFieldId;

    @Column(nullable = false)
    private String customFieldValue;
}
