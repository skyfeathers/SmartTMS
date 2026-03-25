package net.lab1024.tms.admin.module.business.material.yard;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardCreateForm;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardQueryForm;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardUpdateForm;
import net.lab1024.tms.admin.module.business.material.yard.domain.YardVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 业务资料-堆场管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:31
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.MATERIAL_YARD})
public class YardController {

    @Autowired
    private YardService yardService;

    @ApiOperation(value = "分页查询堆场模块 @author lihaifan")
    @PostMapping("/yard/page/query")
    public ResponseDTO<PageResult<YardVO>> queryByPage(@RequestBody @Valid YardQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return yardService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询堆场详情 @author lihaifan")
    @GetMapping("/yard/get/{yardId}")
    public ResponseDTO<YardVO> getDetail(@PathVariable Long yardId) {
        return yardService.getDetail(yardId);
    }

    @ApiOperation(value = "新建堆场 @author lihaifan")
    @PostMapping("/yard/create")
    public ResponseDTO<String> createYard(@RequestBody @Valid YardCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return yardService.createYard(createVO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "编辑堆场 @author lihaifan")
    @PostMapping("/yard/update")
    public ResponseDTO<String> updateYard(@RequestBody @Valid YardUpdateForm updateVO) {
        return yardService.updateYard(updateVO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "删除堆场 @author lihaifan")
    @GetMapping("/yard/delete/{yardId}")
    public ResponseDTO<String> deleteYard(@PathVariable Long yardId) {
        return yardService.deleteYard(yardId);
    }

    @ApiOperation(value = "查询堆场列表 @author lihaifan")
    @GetMapping("/yard/list")
    public ResponseDTO<List<YardVO>> queryList() {
        return yardService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }
}
