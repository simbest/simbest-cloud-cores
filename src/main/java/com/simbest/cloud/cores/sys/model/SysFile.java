/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.simbest.cloud.cores.annotations.EntityIdPrefix;
import com.simbest.cloud.cores.base.model.LogicModel;
import com.simbest.cloud.cores.enums.StoreLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


/**
 * 用途：统一文件管理
 * 作者: lishuyi
 * 时间: 2018/3/7  23:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Schema(description = "系统管理-统一文件管理")
public class SysFile extends LogicModel {

    //文件不可读取状态
    public static final Integer NOT_EXIST_FILE = -1;
    //文件可以读取状态
    public static final Integer EXIST_FILE = 1;
    //文件可以读取状态
    public static final Integer EXIST_FILE_SAME_NAME_WITH_ID = 2;

    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "F") //主键前缀，此为可选项注解
    private String id;

    @Schema(description = "文件名称")
    @Column(nullable = false, length = 200)
    @NonNull
    private String fileName;

    @Schema(description = "文件类型")
    @Column(nullable = false, length = 20)
    @NonNull
    private String fileType;

    @Schema(description = "文件实际存储服务器路径")
    @Column(nullable = false, length = 500)
    @NonNull
    @JsonIgnore //隐藏不对外暴露内部路径
    private String filePath;

    @Schema(description = "文件大小")
    @Column(nullable = false, length = 50)
    @NonNull
    private Long fileSize;

    @Schema(description = "归属流程")
    @Column
    private String pmInsType;

    @Schema(description = "归属流程ID")
    @Column
    private String pmInsId;

    @Schema(description = "归属流程区块")
    @Column
    private String pmInsTypePart;

    @Schema(description = "文件下载URL")
    @Column(nullable = false, length = 500)
    @NonNull
    private String downLoadUrl;

    @Schema(description = "专门用于标识是否跟随应用，不跟随云存储的文件")
    @Column
//    private Boolean isLocal = false;
    private Integer isLocal;

    @Schema(description = "隐藏不对外暴露内部备份路径")
    @Column(length = 500)
    @JsonIgnore
    private String backupPath;

    @Schema(description = "手机端下载路径")
    @Column(length = 500)
    private String mobileFilePath;

    @Schema(description = "API下载路径")
    @Column(length = 500)
    private String apiFilePath;

    @Schema(description = "匿名端下载路径")
    @Column(length = 500)
    private String anonymousFilePath;

    @Schema(description = "文件存储方式")
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private StoreLocation storeLocation;

    public static SysFile createSysFile(String fileName, String fileType, String filePath, Long fileSize, String downLoadUrl){
        return SysFile.builder().fileName(fileName).fileType(fileType).filePath(filePath)
                .fileSize(fileSize).downLoadUrl(fileType).build();
    }

}
