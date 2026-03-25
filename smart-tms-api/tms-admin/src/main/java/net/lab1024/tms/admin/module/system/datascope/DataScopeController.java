package net.lab1024.tms.admin.module.system.datascope;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.system.datascope.domain.DataScopeAndViewTypeVO;
import net.lab1024.tms.common.module.system.datascope.service.DataScopeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 查询支持的数据范围类型
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-03-18 20:59:17
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_DATA_SCOPE})
public class DataScopeController extends AdminBaseController {

    @Resource
    private DataScopeService dataScopeService;

    @ApiOperation(value = "获取当前系统所配置的所有数据范围 @author 罗伊")
    @GetMapping("/dataScope/list")
    public ResponseDTO<List<DataScopeAndViewTypeVO>> dataScopeList() {
        return dataScopeService.dataScopeList();
    }


}
