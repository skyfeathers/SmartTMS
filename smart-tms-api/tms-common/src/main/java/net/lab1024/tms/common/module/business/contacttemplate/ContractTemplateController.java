package net.lab1024.tms.common.module.business.contacttemplate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.business.contacttemplate.domain.vo.ContractTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *
 *
 */
@Api(tags = SwaggerTagConst.Support.CONTRACT_TEMPLATE)
@RestController
public class ContractTemplateController{

    @Autowired
    private ContractTemplateService contractTemplateService;


    @ApiOperation("合同-模板列表 by yandy")
    @GetMapping("/contract/template/list")
    public ResponseDTO<List<ContractTemplateVO>> templateList() {
        return contractTemplateService.templateList();
    }
}
