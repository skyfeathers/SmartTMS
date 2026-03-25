package net.lab1024.tms.fixedasset.module.business.repair.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 固定资产-维修登记 新建表单
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class RepairAddForm {

    @ApiModelProperty(value = "所属公司", hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(value = "维修厂家", required = true)
    @NotBlank(message = "维修厂家 不能为空")
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String repairCompany;

    @ApiModelProperty(value = "业务日期", required = true)
    @NotNull(message = "业务日期 不能为空")
    private LocalDate businessDate;

    @ApiModelProperty(value = "维修内容")
    private String content;

    @ApiModelProperty(value = "申请维修人员", required = true)
    @NotNull(message = "申请维修人员 不能为空")
    private Long applyUserId;

    @ApiModelProperty(value = "维修花费")
    private BigDecimal repairCost;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "维修配件附件")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String mountingsFiles;

    @ApiModelProperty(value = "发票附件")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String invoiceFiles;


    @ApiModelProperty(value = "资产id集合")
    @NotEmpty(message = "资产不能为空")
    private List<Long> assetIdList;

}