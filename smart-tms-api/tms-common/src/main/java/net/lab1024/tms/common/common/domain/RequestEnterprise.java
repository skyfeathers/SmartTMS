package net.lab1024.tms.common.common.domain;

import lombok.Data;

/**
 * @author zhuoda
 * @Date 2021-11-01
 */
@Data
public class RequestEnterprise {

    private Long enterpriseId;

    private String enterpriseName;

    private Boolean platformFlag;
}
