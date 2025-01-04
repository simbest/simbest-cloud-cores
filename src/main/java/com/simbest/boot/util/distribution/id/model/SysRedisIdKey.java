/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.distribution.id.model;

import com.simbest.cloud.cores.base.model.GenericModel;
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
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    private String id;

    @Column
    private String day;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private Long value;

}
