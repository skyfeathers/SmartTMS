package net.lab1024.tms.fixedasset.module.business.transfer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferAddForm;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferQueryForm;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.TRANSFER)
@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/transfer/queryPage")
    public ResponseDTO<PageResult<TransferVO>> queryPage(@RequestBody @Valid TransferQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(transferService.queryPage(queryForm));
    }

    @ApiOperation("新建资产转移 @author lidoudou")
    @PostMapping("/asset/transfer/add")
    public ResponseDTO<String> add(@RequestBody @Valid TransferAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return transferService.addTransfer(addForm, dataTracerRequestForm);
    }

    @ApiOperation("确认资产转移 @author lidoudou")
    @GetMapping("/asset/transfer/complete/{transferId}")
    public ResponseDTO<String> complete(@PathVariable Long transferId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return transferService.complete(transferId, dataTracerRequestForm);
    }

    @ApiOperation("驳回资产转移申请 @author lidoudou")
    @GetMapping("/asset/transfer/reject/{transferId}")
    public ResponseDTO<String> reject(@PathVariable Long transferId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return transferService.reject(transferId, dataTracerRequestForm);
    }


    @ApiOperation("获取详情 @author lidoudou")
    @GetMapping("/asset/transfer/detail/{transferId}")
    public ResponseDTO<TransferVO> getDetail(@PathVariable Long transferId) {
        TransferVO assetVO = transferService.getDetail(transferId);
        if (null == assetVO) {
            return ResponseDTO.userErrorParam("采购详情不存在");
        }
        return ResponseDTO.ok(assetVO);
    }
}