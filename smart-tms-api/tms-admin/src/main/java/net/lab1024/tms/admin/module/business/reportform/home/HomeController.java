package net.lab1024.tms.admin.module.business.reportform.home;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.reportform.home.domain.HomeExpireCertVO;
import net.lab1024.tms.admin.module.business.reportform.home.domain.HomeNumDTO;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/3/17 上午10:36
 */
@RestController
@Api(tags = AdminSwaggerTagConst.Business.HOME)
public class HomeController {

    @Autowired
    private HomeService homeService;

    @ApiOperation(value = "首页单据数量 @author lidoudou")
    @GetMapping("/home/num/summary")
    public ResponseDTO<HomeNumDTO> homeNum() {
        return homeService.homeNum(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "各状态过期证件数量 @author lidoudou")
    @GetMapping("/home/expireCert/summary")
    public ResponseDTO<List<HomeExpireCertVO>> expireCert() {
        return homeService.expireCert(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }
}