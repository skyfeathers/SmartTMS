package net.lab1024.tms.admin.module.business.bracket.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceVO;

import java.util.List;

/***
 * 车辆详情
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:20
 */
@Data
public class BracketDetailVO extends BracketListVO {

    @ApiModelProperty("保险列表")
    private List<InsuranceVO> insuranceList;

}
