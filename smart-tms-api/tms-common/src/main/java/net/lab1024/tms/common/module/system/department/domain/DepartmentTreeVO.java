package net.lab1024.tms.common.module.system.department.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Data
public class DepartmentTreeVO extends DepartmentVO {

    @ApiModelProperty("同级上一个元素id")
    private Long preId;

    @ApiModelProperty("同级下一个元素id")
    private Long nextId;

    @ApiModelProperty("子部门")
    private List<DepartmentTreeVO> children;

    @ApiModelProperty("自己和所有递归子部门的id集合")
    private List<Long> selfAndAllChildrenIdList;

}
