package net.lab1024.tms.admin.module.business.shipper.domain.form;

import lombok.Data;

/***
 * 根据全称以及公司查询
 *
 * @author lidoudou
 * @date 2023/2/27 上午10:05
 */
@Data
public class ShipperConsignorEnterpriseSearchForm {

    private Long enterpriseId;

    private String consignor;
}
