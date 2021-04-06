/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.security.auth.oauth2.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import com.simbest.cloud.cores.dal.model.GenericModel;
import com.simbest.cloud.cores.utils.json.JacksonUtils;
import com.simbest.cloud.orguser.pojo.MySimpleGrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用途：Oauth2 客户端实体信息
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/12  20:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Oauth2ClientDetails extends GenericModel implements ClientDetails {

    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.cloud.cores.distributed.id.SnowflakeId")
    private String id;

    @Column(nullable = false, unique = true, length = 40)
    private String clientId;

    @Column
    private String clientSecret;

    @Column
    private Integer accessTokenValiditySeconds;

    @Column
    private Integer refreshTokenValiditySeconds;

    @Column
    private String jsonResourceId;

    @Override
    public Set<String> getResourceIds() {
        return JacksonUtils.json2Type(jsonResourceId, new TypeReference<Set<String>>() {});
    }

    @Column
    private Boolean secretRequired;

    @Override
    public boolean isSecretRequired() {
        return secretRequired;
    }

    @Column
    private Boolean scoped;

    @Override
    public boolean isScoped() {
        return scoped;
    }

    @Column
    private String jsonScoped;

    @Override
    public Set<String> getScope() {
        return JacksonUtils.json2Type(jsonScoped, new TypeReference<Set<String>>() {});
    }

    @Column
    private String jsonAuthorizedGrantTypes;

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return JacksonUtils.json2Type(jsonAuthorizedGrantTypes, new TypeReference<Set<String>>() {});
    }

    @Column
    private String jsonRegisteredRedirectUri;

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return JacksonUtils.json2Type(jsonRegisteredRedirectUri, new TypeReference<Set<String>>() {});
    }

    @Column
    private String jsonAuthorities;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = Sets.newHashSet();
        Collection<MySimpleGrantedAuthority> simpleGrantedAuthorities = JacksonUtils.json2Type(jsonAuthorities, new TypeReference<List<MySimpleGrantedAuthority>>() {});
        grantedAuthorities.addAll(simpleGrantedAuthorities);
        return grantedAuthorities;
    }

    @Column
    private Boolean autoApprove;

    @Override
    public boolean isAutoApprove(String s) {
        return autoApprove;
    }

    @Column
    private String jsonAdditionalInformation;

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return JacksonUtils.json2Type(jsonAdditionalInformation, new TypeReference<Map<String, Object>>() {});
    }

}
