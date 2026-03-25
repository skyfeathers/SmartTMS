package net.lab1024.tms.admin.module.business.pic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.pic.domain.form.PicAddForm;
import net.lab1024.tms.admin.module.business.pic.domain.form.PicQueryForm;
import net.lab1024.tms.admin.module.business.pic.domain.form.PicUpdateForm;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.business.pic.CommonPicService;
import net.lab1024.tms.common.module.business.pic.constants.PicTypeEnum;
import net.lab1024.tms.common.module.business.pic.domain.vo.PicSimpleVO;
import net.lab1024.tms.common.module.business.pic.domain.vo.PicVO;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhuo
 * @Date 2021/1/22
 */
@Api(tags = {AdminSwaggerTagConst.Business.PIC})
@RestController
public class PicController {

    @Autowired
    private PicService picService;
    @Autowired
    private CommonPicService commonPicService;

    @NoNeedLogin
    @ApiOperation("查询轮播图列表")
    @GetMapping("/pic/list")
    public ResponseDTO<List<PicSimpleVO>> queryList() {
        return ResponseDTO.ok(commonPicService.selectByType(PicTypeEnum.ADMIN_APP.getValue(), SmartRequestEnterpriseUtil.getRequestEnterpriseId()));
    }

    @ApiOperation("分页查询  @author zhuo")
    @PostMapping("/pic/query")
    public ResponseDTO<PageResult<PicVO>> query(@RequestBody @Valid PicQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return picService.query(queryForm);
    }

    @RepeatSubmit
    @ApiOperation("添加  @author zhuo")
    @PostMapping("/pic/add")
    public ResponseDTO<String> add(@RequestBody @Valid PicAddForm addForm) {
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        addForm.setCreateUserId(SmartRequestUtil.getRequestUserId());
        addForm.setCreateUserName(SmartRequestUtil.getRequestUser().getUserName());
        return picService.add(addForm);
    }

    @RepeatSubmit
    @ApiOperation("更新  @author zhuo")
    @PostMapping("/pic/update")
    public ResponseDTO<String> update(@RequestBody @Valid PicUpdateForm updateForm) {
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return picService.update(updateForm);
    }

    @RepeatSubmit
    @ApiOperation("详情  @author lidoudou")
    @GetMapping("/pic/detail/{picId}")
    public ResponseDTO<PicVO> detail(@PathVariable Long picId) {
        return picService.getDetail(picId);
    }


    @RepeatSubmit
    @ApiOperation("更新排序  @author zhuo")
    @GetMapping("/pic/updateSeq/{picId}/{seq}")
    public ResponseDTO<String> updateSeq(@PathVariable Long picId, @PathVariable Integer seq) {
        return picService.updateSort(picId, seq);
    }

    @RepeatSubmit
    @ApiOperation("批量启用/禁用  @author zhuo")
    @GetMapping("/pic/enable/{picId}")
    public ResponseDTO<String> batchUpdateEnable(@PathVariable Long picId) {
        return picService.batchUpdateEnable(picId);
    }

    @RepeatSubmit
    @ApiOperation("批量删除  @author yandy")
    @PostMapping("/pic/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody List<Long> picIdList) {
        return picService.batchDelete(picIdList);
    }


}
