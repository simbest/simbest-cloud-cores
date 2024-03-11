/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.util.distribution.id.model;


import com.simbest.cloud.cores.base.model.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;



/**
 * 用途：Redis序列号前缀实体
 * 作者: lishuyi
 * 时间: 2020/5/14  15:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_redis_id_key", uniqueConstraints = {
        @UniqueConstraint(name="sys_redis_id_day_name", columnNames = {"day", "name"})
})
public class SysRedisIdKey extends GenericModel {

    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.cloud.cores.util.distribution.id.SnowflakeId")
    private String id;

    @Schema(name = "发生日期")
    @Column
    private String day;

    @NonNull
    @Schema(name = "ID名称")
    @Column(nullable = false)
    private String name;

    @NonNull
    @Schema(name = "ID值")
    @Column(nullable = false)
    private Long value;

}
