package net.lab1024.tms.admin.module.business.oa.cost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 费用申请列表展示
 *
 * @author lidoudou
 * @date 2023/3/29 下午5:00
 */
@Data
public class CostApplyDetailVO extends CostApplyVO {

    @ApiModelProperty(value = "资产列表")
    private List<CostApplyItemVO> itemList;


}