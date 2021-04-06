/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.feign;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用途：
 * 作者: lishuyi@simbest.com.cn
 * 时间: 2021/3/15  19:33
 */
@Configuration
public class FeignContractConfig {

    @Bean
    public Contract feignContract() {
        return new HierarchicalContract();
    }

}
