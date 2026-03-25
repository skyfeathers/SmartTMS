package net.lab1024.tms.driver.module.business.pic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.pic.CommonPicService;
import net.lab1024.tms.common.module.business.pic.constants.PicTypeEnum;
import net.lab1024.tms.common.module.business.pic.domain.vo.PicSimpleVO;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhuo
 * @Date 2021/1/22
 */
@Api(tags = {DriverSwaggerTagConst.Business.PIC})
@RestController
public class PicController {

    @Autowired
    private CommonPicService commonPicService;

    @NoNeedLogin
    @ApiOperation("查询轮播图列表")
    @GetMapping("/pic/list")
    public ResponseDTO<List<PicSimpleVO>> queryList() {
        return ResponseDTO.ok(commonPicService.selectByType(PicTypeEnum.DRIVER_APP.getValue(), SmartRequestEnterpriseUtil.getRequestEnterpriseId()));
    }

}
