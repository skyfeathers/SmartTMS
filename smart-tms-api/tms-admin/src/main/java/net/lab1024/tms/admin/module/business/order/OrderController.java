package net.lab1024.tms.admin.module.business.order;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderCreateForm;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderQueryForm;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderScheduleFlagUpdateForm;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderUpdateForm;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderExportVO;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderImportResultVO;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderVO;
import net.lab1024.tms.admin.module.business.order.service.OrderImportService;
import net.lab1024.tms.admin.module.business.order.service.OrderService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单管理
 *
 * @author lidoudou
 * @date 2022/7/13 下午3:09
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.ORDER})
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderImportService orderImportService;

    @ApiOperation(value = "分页查询订单列表 @author lidoudou")
    @PostMapping("/order/page/query")
    public ResponseDTO<PageResult<OrderVO>> queryByPage(@RequestBody @Valid OrderQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return orderService.queryByPage(queryForm);
    }

    @ApiOperation(value = "查询订单详情 @author lidoudou")
    @GetMapping("/order/get/{orderId}")
    public ResponseDTO<OrderVO> getDetail(@PathVariable Long orderId) {
        OrderVO orderVO = orderService.getDetail(orderId);
        if (null == orderVO) {
            return ResponseDTO.userErrorParam("订单数据不存在");
        }
        return ResponseDTO.ok(orderVO);
    }

    @RepeatSubmit
    @ApiOperation(value = "新建订单 @author lidoudou")
    @PostMapping("/order/create")
    public ResponseDTO<Long> createOrder(@RequestBody @Valid OrderCreateForm createForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createForm.setCreateUserId(requestUser.getUserId());
        createForm.setCreateUserName(requestUser.getUserName());
        createForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return orderService.createOrder(createForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "编辑订单 @author lidoudou")
    @PostMapping("/order/update")
    public ResponseDTO<String> updateOrder(@RequestBody @Valid OrderUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        updateForm.setCreateUserId(requestUser.getUserId());
        updateForm.setCreateUserName(requestUser.getUserName());

        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return orderService.updateOrder(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "订单及关联运单-货主-更新 @author zhaoxinyang")
    @GetMapping("/order/shipper/update/{orderId}/{shipperId}")
    public ResponseDTO<String> updateOrderShipper(@PathVariable("orderId") Long orderId, @PathVariable("shipperId") Long shipperId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return orderService.updateOrderShipper(orderId, shipperId, dataTracerRequestForm);
    }

    @ApiOperation(value = "取消订单 @author lidoudou")
    @GetMapping("/order/cancel/{orderId}")
    public ResponseDTO<String> cancelOrder(@PathVariable Long orderId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return orderService.cancelOrder(orderId, dataTracerRequestForm);
    }


    @ApiOperation("订单导入 by lidoudou")
    @PostMapping("/order/doc/import")
    public ResponseDTO<List<OrderImportResultVO>> docImport(MultipartFile uploadFile) throws Exception {
        return orderImportService.validateImportData(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), uploadFile);
    }

    @ApiOperation("订单确认导入 by lidoudou")
    @PostMapping("/order/import")
    public ResponseDTO<String> confirmImport(@RequestBody @Valid ValidateList<OrderImportResultVO> importList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return orderImportService.confirmImportData(importList, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @ApiOperation("订单货物更新 by lidoudou")
    @PostMapping("/order/goods/update")
    public ResponseDTO<String> updateGoodsList(@RequestBody @Valid OrderScheduleFlagUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return orderService.updateGoodsList(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "订单列表导出 by lidoudou")
    @GetMapping("/order/export")
    public void export(OrderQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<OrderExportVO> list = orderService.queryByExport(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "订单导出";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, OrderExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}
