package com.simbest.cloud.cores.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckIpIsDevOpsResult {

    private boolean checkRet;

    private String whiteIps;

    private String currentIp;

}
