package net.lab1024.tms.admin.module.business.waybill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderGoodsDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderPathDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillSplitTransportDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillVoucherDao;
import net.lab1024.tms.admin.module.business.waybill.domain.form.*;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.*;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillCostManager;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.*;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.*;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherForm;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherUpdateForm;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import net.lab1024.tms.common.module.support.baidumap.domain.BaiduLonLatAddressVO;
import net.lab1024.tms.common.module.support.baidumap.domain.BaiduReverseGeocodingQueryForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:04 下午
 */
@Service
public class WaybillService {

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private FleetDao fleetDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private WaybillDetailService waybillDetailService;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillCommonService waybillCommonService;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private WaybillSplitTransportDao waybillSplitTransportDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private WaybillCostManager waybillCostManager;
    @Autowired
    private WaybillVoucherDao waybillVoucherDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private WaybillDataTracerService waybillDataTracerService;
    @Autowired
    private RoleEmployeeDao roleEmployeeDao;
    @Resource
    private OrderPathDao orderPathDao;

    /**
     * 创建运单
     * 网货订单在审核通过后同步
     * @param wayBillCreateForm
     * @return
     */
    public ResponseDTO<String> createWaybill(WayBillCreateForm wayBillCreateForm, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        OrderEntity orderEntity = orderDao.selectById(wayBillCreateForm.getOrderId());
        if (orderEntity == null || OrderStatusEnum.CANCEL.equalsValue(orderEntity.getStatus())) {
            return ResponseDTO.userErrorParam("订单不存在");
        }
        if (!orderEntity.getEnterpriseId().equals(enterpriseId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您登录的企业不是该订单的所属公司");
        }
        if (orderEntity.getSplitTransportFlag() && (wayBillCreateForm.getSplitTransportForm() == null || wayBillCreateForm.getSplitTransportForm().getProvince() == null)) {
            return ResponseDTO.userErrorParam("分段运输请输入本段结束地址");
        }
        // 货物信息
        List<WayBillCreateGoodsForm> goodsFormList = wayBillCreateForm.getGoodsFormList();
        goodsFormList = goodsFormList.stream().filter(e -> e.getGoodsQuantity().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsFormList)) {
            return ResponseDTO.userErrorParam("货物信息错误，货物量不能为0");
        }
        List<Long> orderGoodsIdList = goodsFormList.stream().map(WayBillCreateGoodsForm::getOrderGoodsId).collect(Collectors.toList());
        List<OrderGoodsEntity> orderGoodsEntityList = orderGoodsDao.selectBatchIds(orderGoodsIdList);
        if (CollectionUtils.isEmpty(orderGoodsEntityList)) {
            return ResponseDTO.userErrorParam("货物信息不存在");
        }

        ResponseDTO<String> driverResp = this.driverValidate(wayBillCreateForm, enterpriseId);
        if (!driverResp.getOk()) {
            return driverResp;
        }
        ResponseDTO<String> vehicleResp = this.vehicleValidate(wayBillCreateForm, enterpriseId);
        if (!vehicleResp.getOk()) {
            return vehicleResp;
        }

        ResponseDTO<String> validateResp = this.fleetValidate(wayBillCreateForm);
        if (!validateResp.getOk()) {
            return validateResp;
        }
        Integer settleType = wayBillCreateForm.getFleetId() == null ? WaybillSettleTypeEnum.DRIVER.getValue() : WaybillSettleTypeEnum.FLEET.getValue();
        // 运单号
        String waybillNumber = serialNumberService.generate(SerialNumberIdEnum.WAYBILL);
        // 应收初始运费
        BigDecimal receiveAmount = wayBillCreateForm.getReceiveAmount();
        // 运费
        BigDecimal cashAmount = wayBillCreateForm.getCashAmount();
        // 油卡
        BigDecimal oilCardAmount = wayBillCreateForm.getOilCardAmount();
        // 合计应付
        BigDecimal payableAmount = cashAmount.add(oilCardAmount);
        //利润
        BigDecimal profitAmount = waybillCommonService.calculateProfitAmount(receiveAmount, payableAmount, BigDecimal.ZERO, BigDecimal.ZERO, orderEntity.getTaxPoint());

        // 运输路线名字
        List<OrderPathEntity> orderPathEntityList = orderPathDao.selectByOrderIdList(Lists.newArrayList(orderEntity.getOrderId()));
        Optional<OrderPathEntity> loadPath= orderPathEntityList.stream().filter(e -> PathTypeEnum.PLACING_LOADING.equalsValue(e.getType()))
                .min(Comparator.comparingLong(OrderPathEntity::getOrderPathId));
        Optional<OrderPathEntity> unloadPath = orderPathEntityList.stream().filter(e -> PathTypeEnum.UNLOADING_LOCATION.equalsValue(e.getType()))
                .max(Comparator.comparingLong(OrderPathEntity::getOrderPathId));
        String routeName = loadPath.isPresent() && unloadPath.isPresent() ?
                loadPath.get().getCityName() + "-" + unloadPath.get().getCityName() : StringConst.EMPTY;

        WaybillEntity waybillEntity = SmartBeanUtil.copy(wayBillCreateForm, WaybillEntity.class);
        waybillEntity.setWaybillNumber(waybillNumber);
        waybillEntity.setSettleType(settleType);
        waybillEntity.setSplitTransportFlag(orderEntity.getSplitTransportFlag());
        waybillEntity.setOrderId(orderEntity.getOrderId());
        waybillEntity.setShipperId(orderEntity.getShipperId());
        waybillEntity.setEnterpriseId(orderEntity.getEnterpriseId());
        waybillEntity.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        waybillEntity.setWaybillStatus(WaybillStatusEnum.TRANSPORT.getValue());
        waybillEntity.setRouteName(routeName);
        waybillEntity.setDistance(orderEntity.getDistance());
        waybillEntity.setReceiveAmount(receiveAmount);
        waybillEntity.setPayableCashAmount(cashAmount);
        waybillEntity.setPayableOilCardAmount(oilCardAmount);
        waybillEntity.setPayableAmount(payableAmount);
        waybillEntity.setProfitAmount(profitAmount);
        waybillEntity.setTaxPoint(orderEntity.getTaxPoint());
        waybillEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        waybillEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        //分段信息
        WaybillSplitTransportEntity waybillSplitTransportEntity = null;
        if (orderEntity.getSplitTransportFlag()) {
            waybillSplitTransportEntity = SmartBeanUtil.copy(wayBillCreateForm.getSplitTransportForm(), WaybillSplitTransportEntity.class);
        }
        // 运单路线
        List<WaybillPathEntity> waybillPathEntityList = this.buildWaybillPath(orderEntity.getOrderId());
        //持久化
        waybillManager.createWaybillHandle(waybillEntity, orderGoodsEntityList, goodsFormList, wayBillCreateForm, waybillSplitTransportEntity, waybillPathEntityList, dataTracerRequestForm);
        waybillDataTracerService.saveLog(waybillEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    private List<WaybillPathEntity> buildWaybillPath(Long orderId) {
        List<OrderPathEntity> orderPathEntityList = orderPathDao.selectByOrderIdList(Lists.newArrayList(orderId));
        if (CollectionUtils.isEmpty(orderPathEntityList)) {
            return Lists.newArrayList();
        }
        return SmartBeanUtil.copyList(orderPathEntityList, WaybillPathEntity.class);
    }


    /**
     * 数据校验
     *
     * @param wayBillCreateForm
     * @return
     */
    private ResponseDTO<String> driverValidate(WayBillCreateForm wayBillCreateForm, Long enterpriseId) {
        //校验 司机
        DriverEntity driverEntity = driverDao.selectById(wayBillCreateForm.getDriverId());
        if (driverEntity == null || driverEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("司机不存在");
        }
        return ResponseDTO.ok();
    }

    /**
     * 车辆校验
     *
     * @param wayBillCreateForm
     * @return
     */
    private ResponseDTO<String> vehicleValidate(WayBillCreateForm wayBillCreateForm, Long enterpriseId) {
        //校验车辆
        VehicleEntity vehicleEntity = vehicleDao.selectById(wayBillCreateForm.getVehicleId());
        if (vehicleEntity == null || vehicleEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("车辆不存在");
        }
        return ResponseDTO.ok();
    }

    /**
     * 数据校验
     *
     * @param wayBillCreateForm
     * @return
     */
    private ResponseDTO<String> fleetValidate(WaybillSettleTypeForm wayBillCreateForm) {
        // 车队结算
        if (wayBillCreateForm.getFleetId() == null) {
            return ResponseDTO.ok();
        }
        FleetEntity fleetEntity = fleetDao.selectById(wayBillCreateForm.getFleetId());
        if (fleetEntity == null || fleetEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("车队不存在");
        }
        return ResponseDTO.ok();
    }

    /**
     * 查询运单
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<WaybillVO>> query(WaybillQueryForm queryForm) {
        if (BooleanUtils.isTrue(queryForm.getHideCancelFlag())) {
            queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        }
        if (queryForm.getCreateUserRoleId() != null) {
            List<Long> employeeIdList = roleEmployeeDao.selectEmployeeIdByRoleIdList(Lists.newArrayList(queryForm.getCreateUserRoleId()));
            queryForm.setCreateUserIdList(employeeIdList);
        }
        queryForm.setWaybillNumberList(SmartStringUtil.joinIrregularString(queryForm.getWaybillNumbers()));
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<WaybillVO> list = waybillDao.query(page, queryForm);
        // 构建司机、车辆、车队、订单信息
        waybillDetailService.buildDriverVehicleFleetOrder(list);
        waybillDetailService.buildPoundListAttachment(list);
        PageResult<WaybillVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询运单列表
     *
     * @param: queryForm
     * @return: ResponseDTO<List<WaybillVO>>
     **/
    public ResponseDTO<List<WaybillVO>> queryWaybillVOList(WaybillQueryForm queryForm) {
        if (BooleanUtils.isTrue(queryForm.getHideCancelFlag())) {
            queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        }
        if (queryForm.getCreateUserRoleId() != null) {
            List<Long> employeeIdList = roleEmployeeDao.selectEmployeeIdByRoleIdList(Lists.newArrayList(queryForm.getCreateUserRoleId()));
            queryForm.setCreateUserIdList(employeeIdList);
        }

        List<WaybillVO> list = waybillDao.query(null, queryForm);
        // 构建司机、车辆、车队、订单信息
        waybillDetailService.buildDriverVehicleFleetOrder(list);
        return ResponseDTO.ok(list);
    }


    /**
     * 统计运单数量
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<Integer> count(WaybillQueryForm queryForm) {
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        Integer waybillCount = waybillDao.countWaybill(queryForm);
        return ResponseDTO.ok(waybillCount);
    }

    /**
     * 根据订单id查询运单
     *
     * @param orderId
     * @return
     */
    public ResponseDTO<List<WaybillVO>> queryByOrderId(Long orderId) {
        List<Object> statusList = SmartBaseEnumUtil.differenceValueList(WaybillStatusEnum.class, WaybillStatusEnum.CANCEL);
        List<WaybillVO> list = waybillDao.queryByOrderId(orderId, statusList);
        // 构建司机、车辆、车队、订单信息
        waybillDetailService.buildDriverVehicleFleetOrder(list);
        return ResponseDTO.ok(list);

    }

    /**
     * 运单详情
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<WaybillDetailVO> detail(Long waybillId) {
        WaybillDetailVO waybillVO = waybillDetailService.getDetail(waybillId);
        if (waybillVO == null) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        return ResponseDTO.ok(waybillVO);
    }

    /**
     * 更新箱号和铅封号
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateLeadSealAndContainerNumber(WaybillLeadSealAndContainerNumberUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = updateForm.getWaybillId();
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (waybillEntity == null || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        if (waybillEntity.getSubmitReceiveFlag()) {
            return ResponseDTO.userErrorParam("运单已提交应收核算，无法更新箱号和铅封号");
        }
        OrderEntity orderEntity = orderDao.selectById(waybillEntity.getOrderId());
        if (!TransportationTypeEnum.CONTAINER_TRANSPORT.getValue().equals(orderEntity.getBusinessTypeCode())) {
            return ResponseDTO.userErrorParam("非集装箱业务不能更新箱号和铅封号");
        }

        // 更新
        WaybillEntity updateEntity = new WaybillEntity();
        updateEntity.setWaybillId(waybillId);
        updateEntity.setContainerNumber(updateForm.getContainerNumber());
        updateEntity.setLeadSealNumber(updateForm.getLeadSealNumber());
        waybillDao.updateById(updateEntity);
        waybillDataTracerService.updateLeadSealAndContainerNumberLog(updateForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 修改运单
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateWaybill(WaybillUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillEntity waybillEntity = waybillDao.selectById(updateForm.getWaybillId());
        if (waybillEntity == null || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        if (waybillEntity.getSubmitReceiveFlag()) {
            return ResponseDTO.userErrorParam("运单已提交应收核算，无法更新运单");
        }
        ResponseDTO<String> payInfoResp = validatePayInfo(updateForm, waybillEntity);
        if (!payInfoResp.getOk()) {
            return payInfoResp;
        }
        waybillManager.updateWaybill(updateForm, waybillEntity, dataTracerRequestForm);
        WaybillEntity afterEntity = waybillDao.selectById(updateForm.getWaybillId());
        waybillDataTracerService.updateLog(waybillEntity, afterEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 运单已提交应付后，不允许修改应付相关的信息
     *
     * @return
     */
    private ResponseDTO<String> validatePayInfo(WaybillUpdateForm updateForm, WaybillEntity waybillEntity) {
        List<Object> statusList = SmartBaseEnumUtil.differenceValueList(PayOrderStatusEnum.class, PayOrderStatusEnum.CANCEL);
        List<PayOrderEntity> payOrderEntityList = payOrderDao.queryByWaybillId(updateForm.getWaybillId(), statusList);
        if (CollectionUtils.isEmpty(payOrderEntityList)) {
            return ResponseDTO.ok();
        }
        if (!Objects.equals(updateForm.getDriverId(), waybillEntity.getDriverId())) {
            return ResponseDTO.userErrorParam("已提交应付，无法修改司机");
        }
        if (!Objects.equals(updateForm.getVehicleId(), waybillEntity.getVehicleId())) {
            return ResponseDTO.userErrorParam("已提交应付，无法修改车辆");
        }
        if (!Objects.equals(updateForm.getSettleMode(), waybillEntity.getSettleMode())) {
            return ResponseDTO.userErrorParam("已提交应付，无法修改结算方式");
        }
        if (!Objects.equals(updateForm.getFleetId(), waybillEntity.getFleetId())) {
            return ResponseDTO.userErrorParam("已提交应付，无法修改车队");
        }
        return ResponseDTO.ok();
    }

    /**
     * 作废（取消）
     *
     * @param waybillId
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> cancel(Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (waybillEntity == null || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        if (waybillEntity.getSubmitReceiveFlag()) {
            return ResponseDTO.userErrorParam("运单已提交应收核算，无法作废");
        }
        //存在有效状态的付款单
        List<Object> statusList = SmartBaseEnumUtil.differenceValueList(PayOrderStatusEnum.class, PayOrderStatusEnum.CANCEL);
        List<PayOrderEntity> payOrderEntityList = payOrderDao.queryByWaybillId(waybillId, statusList);
        if (CollectionUtils.isNotEmpty(payOrderEntityList)) {
            return ResponseDTO.userErrorParam("存在有效状态的付款记录，无法作废");
        }
        waybillManager.cancelHandle(waybillEntity, dataTracerRequestForm);

        waybillDataTracerService.cancelLog(waybillId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 查询物流平直
     *
     * @param waybillId
     * @return
     */
    public List<WaybillVoucherVO> getVoucherList(Long waybillId) {
        List<WaybillVoucherVO> voucherList = waybillVoucherDao.selectByWaybillId(waybillId);
        if (CollectionUtils.isEmpty(voucherList)) {
            return Lists.newArrayList();
        }
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        for (WaybillVoucherVO waybillVoucherVO : voucherList) {
            if (WaybillVoucherTypeEnum.LOAD.equalsValue(waybillVoucherVO.getType())) {
                waybillVoucherVO.setPoundListQuantity(waybillEntity.getLoadPoundListQuantity());
            }
            if (WaybillVoucherTypeEnum.UNLOAD.equalsValue(waybillVoucherVO.getType())) {
                waybillVoucherVO.setPoundListQuantity(waybillEntity.getUnloadPoundListQuantity());
            }
        }
        return voucherList;
    }

    /**
     * 添加凭证
     *
     * @param voucherForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> addVoucher(WaybillVoucherForm voucherForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = voucherForm.getWaybillId();
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (waybillEntity == null || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        WaybillVoucherEntity voucherEntity = waybillVoucherDao.selectByWaybillIdAndType(waybillId, voucherForm.getType());
        if (voucherEntity != null) {
            return ResponseDTO.userErrorParam("此类型凭证已存在");
        }

        WaybillVoucherEntity waybillVoucherEntity = SmartBeanUtil.copy(voucherForm, WaybillVoucherEntity.class);
        waybillVoucherEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        waybillVoucherEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        waybillVoucherEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        waybillVoucherEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        waybillManager.addVoucherHandle(waybillVoucherEntity, waybillEntity, voucherForm.getPoundListQuantity(), dataTracerRequestForm);
        waybillDataTracerService.addVoucherLog(waybillVoucherEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新凭证
     * @param voucherUpdateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateVoucher(WaybillVoucherUpdateForm voucherUpdateForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillEntity waybillEntity = waybillDao.selectById(voucherUpdateForm.getWaybillId());
        if (waybillEntity == null || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        WaybillVoucherEntity voucherEntity = waybillVoucherDao.selectById(voucherUpdateForm.getWaybillVoucherId());
        if (voucherEntity == null) {
            return ResponseDTO.userErrorParam("此类型凭证不存在");
        }
        if (!voucherEntity.getType().equals(voucherUpdateForm.getType())) {
            return ResponseDTO.userErrorParam("凭证类型不允许编辑");
        }
        WaybillVoucherEntity waybillVoucherEntity = SmartBeanUtil.copy(voucherUpdateForm, WaybillVoucherEntity.class);
        waybillVoucherEntity.setCreateTime(voucherUpdateForm.getCreateTime());
        waybillVoucherEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        waybillVoucherEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        waybillVoucherEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        waybillVoucherEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

        waybillManager.updateVoucherHandle(waybillVoucherEntity, waybillEntity, voucherUpdateForm.getPoundListQuantity(), dataTracerRequestForm);
        waybillDataTracerService.updateVoucherLog(voucherEntity, waybillVoucherEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除凭证
     * @param waybillVoucherId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> deleteVoucher(Long waybillVoucherId, DataTracerRequestForm dataTracerRequestForm) {
        WaybillVoucherEntity voucherEntity = waybillVoucherDao.selectById(waybillVoucherId);
        if (voucherEntity == null) {
            return ResponseDTO.ok();
        }
        waybillVoucherDao.deleteById(waybillVoucherId);
        waybillDataTracerService.deleteVoucher(voucherEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 运单费用维护
     *
     * @param waybillCostForm
     * @return
     */
    public ResponseDTO<String> cost(WaybillCostForm waybillCostForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillCostForm.getWaybillId());
        if (waybillEntity == null || WaybillStatusEnum.CANCEL.equalsValue(waybillEntity.getWaybillStatus())) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        OrderEntity orderEntity = orderDao.selectById(waybillEntity.getOrderId());
        if (waybillEntity == null || OrderStatusEnum.CANCEL.equalsValue(orderEntity.getStatus())) {
            return ResponseDTO.userErrorParam("订单不存在");
        }
        List<WaybillCostItemForm> costItemFormList = waybillCostForm.getCostItemList();
        if (CollectionUtils.isEmpty(costItemFormList)) {
            return ResponseDTO.ok();
        }
        List<Long> waybillCostIdList = costItemFormList.stream().map(WaybillCostItemForm::getWaybillCostId).collect(Collectors.toList());
        List<WaybillCostEntity> waybillCostEntityList = waybillCostManager.getBaseMapper().selectBatchIds(waybillCostIdList);
        if (CollectionUtils.isEmpty(waybillCostEntityList)) {
            return ResponseDTO.userErrorParam("运单费用项不存在");
        }
        // 过滤存在费用修改的不同费用项
        Map<Long,WaybillCostItemForm> costItemFormMap = costItemFormList.stream().collect(Collectors.toMap(WaybillCostItemForm::getWaybillCostId, Function.identity()));
        // 油卡费用是否变更
        Boolean oilCardChangeFlag = waybillCostEntityList.stream().anyMatch(e-> {
            WaybillCostItemForm costItemForm = costItemFormMap.get(e.getWaybillCostId());
            if (costItemForm.getCostAmount().compareTo(e.getCostAmount()) != 0 && CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory()) ) {
                return true;
            }
            return false;
        });
        // 现金费用是否变更
        Boolean cashChangeFlag = waybillCostEntityList.stream().anyMatch(e-> {
            WaybillCostItemForm costItemForm = costItemFormMap.get(e.getWaybillCostId());
            if (costItemForm.getCostAmount().compareTo(e.getCostAmount()) != 0 && !CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory()) ) {
                return true;
            }
            return false;
        });
        if (!PayOrderStatusEnum.NO_APPLY.equalsValue(waybillEntity.getOilCardPayStatus()) && oilCardChangeFlag) {
            return ResponseDTO.userErrorParam("运单已申请油卡充值申请，无法修改油卡费用");
        }
        if (!PayOrderStatusEnum.NO_APPLY.equalsValue(waybillEntity.getCashPayStatus()) && cashChangeFlag) {
            return ResponseDTO.userErrorParam("运单已申请付款申请，无法修改现金部分费用");
        }
        //持久化
        waybillManager.costHandle(waybillCostForm, waybillEntity, orderEntity.getTaxPoint(), dataTracerRequestForm);
        waybillDataTracerService.costLog(waybillCostForm, waybillEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }



    /**
     * 费用结算查询
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<WaybillSettleVO> cashCostSettleQuery(Long waybillId) {
        //运单信息
        WaybillVO waybillVO = waybillDao.detail(waybillId);
        if (waybillVO == null) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        //只能对未作废，已审核通过的进行费用结算
        if (WaybillStatusEnum.CANCEL.equalsValue(waybillVO.getWaybillStatus()) || !FlowAuditStatusEnum.PASS.equalsValue(waybillVO.getAuditStatus())) {
            return ResponseDTO.userErrorParam("存在已作废或未审核通过的运单");
        }
        //运单费用信息
        List<WaybillCostEntity> waybillCostEntityList = waybillCostManager.getBaseMapper().selectByWaybillId(waybillId);
        // 过滤掉 可支付金额为0 的费用项
        waybillCostEntityList = waybillCostEntityList.stream().filter(e -> e.getCostAmount().compareTo(BigDecimal.ZERO) > 0 && !CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(waybillCostEntityList)) {
            return ResponseDTO.userErrorParam("暂无需要支付的费用项");
        }
        // 构建司机、车辆、车队、订单信息
        waybillDetailService.buildDriverVehicleFleetOrder(Lists.newArrayList(waybillVO));
        WaybillSettleVO waybillSettleVO = buildWaybillSettleInfo(waybillCostEntityList, waybillVO);
        return ResponseDTO.ok(waybillSettleVO);
    }

    /**
     * 费用结算查询
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<WaybillSettleVO> oilCardCostSettleQuery(Long waybillId) {
        //运单信息
        WaybillVO waybillVO = waybillDao.detail(waybillId);
        if (waybillVO == null) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        //只能对未作废，已审核通过的进行费用结算
        if (WaybillStatusEnum.CANCEL.equalsValue(waybillVO.getWaybillStatus()) || !FlowAuditStatusEnum.PASS.equalsValue(waybillVO.getAuditStatus())) {
            return ResponseDTO.userErrorParam("存在已作废或未审核通过的运单");
        }

        //运单费用信息
        List<WaybillCostEntity> waybillCostEntityList = waybillCostManager.getBaseMapper().selectByWaybillId(waybillId);
        // 过滤掉 可支付金额为0 的费用项
        waybillCostEntityList = waybillCostEntityList.stream().filter(e -> e.getCostAmount().compareTo(BigDecimal.ZERO) > 0 && CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(waybillCostEntityList)) {
            return ResponseDTO.userErrorParam("暂无需要支付的费用项");
        }
        // 构建司机、车辆、车队、订单信息
        waybillDetailService.buildDriverVehicleFleetOrder(Lists.newArrayList(waybillVO));
        WaybillSettleVO waybillSettleVO = buildWaybillSettleInfo(waybillCostEntityList, waybillVO);
        return ResponseDTO.ok(waybillSettleVO);
    }

    /**
     * 结算信息
     * @param waybillCostEntityList
     * @param waybillVO
     * @return
     */
    private WaybillSettleVO buildWaybillSettleInfo(List<WaybillCostEntity> waybillCostEntityList, WaybillVO waybillVO) {
        // 设置运单信息
        List<WaybillSettleCostVO> costSettleVOList = SmartBeanUtil.copyList(waybillCostEntityList, WaybillSettleCostVO.class);
        for (WaybillSettleCostVO waybillSettleCostVO : costSettleVOList) {
            waybillSettleCostVO.setWaybillNumber(waybillVO.getWaybillNumber());
            waybillSettleCostVO.setShipperId(waybillVO.getShipperId());
            waybillSettleCostVO.setShipperName(waybillVO.getShipperName());
            waybillSettleCostVO.setEnterpriseId(waybillVO.getEnterpriseId());
            waybillSettleCostVO.setEnterpriseName(waybillVO.getEnterpriseName());
            waybillSettleCostVO.setContainerNumber(waybillVO.getContainerNumber());
            waybillSettleCostVO.setSettleType(waybillVO.getSettleType());
            waybillSettleCostVO.setDriverId(waybillVO.getDriverId());
            waybillSettleCostVO.setDriverName(waybillVO.getDriverName());
            waybillSettleCostVO.setDriverTelephone(waybillVO.getDriverTelephone());
            waybillSettleCostVO.setVehicleId(waybillVO.getVehicleId());
            waybillSettleCostVO.setVehicleNumber(waybillVO.getVehicleNumber());
            waybillSettleCostVO.setFleetId(waybillVO.getFleetId());
            waybillSettleCostVO.setFleetName(waybillVO.getFleetName());
            waybillSettleCostVO.setFleetCaptainPhone(waybillVO.getFleetCaptainPhone());
            waybillSettleCostVO.setThisPayableAmount(waybillSettleCostVO.getCostAmount());
        }
        // 结算对象信息
        WaybillSettleTypeEnum settleTypeEnum = SmartBaseEnumUtil.getEnumByValue(waybillVO.getSettleType(), WaybillSettleTypeEnum.class);

        WaybillSettleVO waybillSettleVO = new WaybillSettleVO();
        waybillSettleVO.setSettleType(settleTypeEnum.getValue());
        waybillSettleVO.setEnterpriseId(waybillVO.getEnterpriseId());
        if (WaybillSettleTypeEnum.DRIVER == settleTypeEnum) {
            waybillSettleVO.setSettleObjectId(waybillVO.getDriverId());
            waybillSettleVO.setSettleObjectName(waybillVO.getDriverName());
            waybillSettleVO.setSettleObjectPhone(waybillVO.getDriverTelephone());
        } else {
            waybillSettleVO.setSettleObjectId(waybillVO.getFleetId());
            waybillSettleVO.setSettleObjectName(waybillVO.getFleetName());
            waybillSettleVO.setSettleObjectPhone(waybillVO.getFleetCaptainPhone());
        }
        waybillSettleVO.setSettleCostList(costSettleVOList);
        return waybillSettleVO;
    }

    /**
     * 更新为运输完成状态
     *
     * @param completeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> complete(WaybillCompleteForm completeForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> waybillIdList = completeForm.getWaybillIdList();
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return ResponseDTO.ok();
        }
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        List<WaybillEntity> errorStatusList = waybillEntityList.stream().filter(e -> !WaybillStatusEnum.TRANSPORT.equalsValue(e.getWaybillStatus())).collect(Collectors.toList());
        if (errorStatusList.size() > 0) {
            return ResponseDTO.userErrorParam("只能更新运输中的运单");
        }

        List<WaybillEntity> updateEntityList = Lists.newArrayList();
        for (Long waybillId : waybillIdList) {
            WaybillEntity waybillEntity = new WaybillEntity();
            waybillEntity.setWaybillId(waybillId);
            waybillEntity.setWaybillStatus(WaybillStatusEnum.COMPLETE.getValue());
            waybillEntity.setCompleteTime(completeForm.getCompleteTime());
            updateEntityList.add(waybillEntity);
        }
        waybillManager.updateBatchById(updateEntityList);
        waybillDataTracerService.batchCompleteLog(completeForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 查询运单
     *
     * @param queryForm
     * @return
     */
    public List<WaybillExportVO> queryByExport(WaybillQueryForm queryForm) {
        if (BooleanUtils.isTrue(queryForm.getHideCancelFlag())) {
            queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        }
        if (queryForm.getStartTime() == null || queryForm.getEndTime() == null) {
            throw new BusinessException("请选择创建时间进行导出，最多3个月");
        }
        int maxCreateTimeSearchDays = (int) ChronoUnit.DAYS.between(queryForm.getStartTime(), queryForm.getEndTime()) + 1;
        if (maxCreateTimeSearchDays > 100) {
            throw new BusinessException("创建时间错误,最大支持三个月的查询");
        }

        List<WaybillVO> list = waybillDao.query(null, queryForm);
        List<WaybillExportVO> exportList = Lists.newArrayList();
        int pageSize = 200;
        int count = list.size();
        int pageCount = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        for (int page = 1; page <= pageCount; page++) {
            int fromIndex = (page - 1) * pageSize;
            int toIndex = page * pageSize > count ? count : page * pageSize;
            List<WaybillVO> pageList = list.subList(fromIndex, toIndex);
            // 构建司机、车辆、车队、订单信息
            waybillDetailService.buildDriverVehicleFleetOrder(pageList);

            List<WaybillExportVO> exportPageList = pageList.stream().map(e -> {
                WaybillExportVO exportVO = SmartBeanUtil.copy(e, WaybillExportVO.class);
                exportVO.setAuditStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getAuditStatus(), FlowAuditStatusEnum.class));
                exportVO.setWaybillStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getWaybillStatus(), WaybillStatusEnum.class));
                exportVO.setSettleModeDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getSettleMode(), WaybillSettleModeEnum.class));
                exportVO.setSettleTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getSettleType(), WaybillSettleTypeEnum.class));
                exportVO.setSubmitReceiveDesc(e.getSubmitReceiveFlag() ? "已提交" : "未提交");
                return exportVO;
            }).collect(Collectors.toList());
            exportList.addAll(exportPageList);
        }

        return exportList;
    }

    /**
     * 上传回单
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> uploadReceiptAttachment(WaybillReceiptUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillEntity waybillEntity = waybillDao.selectById(updateForm.getWaybillId());
        if (null == waybillEntity) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        WaybillEntity updateEntity = new WaybillEntity();
        updateEntity.setWaybillId(updateForm.getWaybillId());
        updateEntity.setReceiptAttachment(updateForm.getReceiptAttachment());
        waybillManager.updateById(updateEntity);
        waybillDataTracerService.uploadReceiptAttachmentLog(updateForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 上传派车单
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> uploadTrunkOrderAttachment(WaybillTruckOrderUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillEntity waybillEntity = waybillDao.selectById(updateForm.getWaybillId());
        if (null == waybillEntity) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        WaybillEntity updateEntity = new WaybillEntity();
        updateEntity.setWaybillId(updateForm.getWaybillId());
        updateEntity.setTruckOrderAttachment(updateForm.getTruckOrderAttachment());
        waybillManager.updateById(updateEntity);
        waybillDataTracerService.uploadTrunkOrderAttachmentLog(updateForm, dataTracerRequestForm);
        return ResponseDTO.ok();

    }

    /**
     * 统计运单金额
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<WaybillStatisticAmountVO> statisticAmount(WaybillQueryForm queryForm) {
        if (BooleanUtils.isTrue(queryForm.getHideCancelFlag())) {
            queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        }
        if (queryForm.getCreateUserRoleId() != null) {
            List<Long> employeeIdList = roleEmployeeDao.selectEmployeeIdByRoleIdList(Lists.newArrayList(queryForm.getCreateUserRoleId()));
            queryForm.setCreateUserIdList(employeeIdList);
        }
        queryForm.setWaybillNumberList(SmartStringUtil.joinIrregularString(queryForm.getWaybillNumbers()));
        WaybillStatisticAmountVO statisticAmountVO = waybillDao.statisticAmount(queryForm);
        return ResponseDTO.ok(statisticAmountVO);
    }

    /**
     * 统计运单金额
     *
     * @return
     */
    public ResponseDTO<WaybillStatisticAmountAppVO> appStatisticAmount(Long enterpriseId) {
        WaybillQueryForm queryForm = new WaybillQueryForm();
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        queryForm.setEnterpriseId(enterpriseId);
        if (queryForm.getCreateUserRoleId() != null) {
            List<Long> employeeIdList = roleEmployeeDao.selectEmployeeIdByRoleIdList(Lists.newArrayList(queryForm.getCreateUserRoleId()));
            queryForm.setCreateUserIdList(employeeIdList);
        }
        WaybillStatisticAmountVO statisticAmountVO = waybillDao.statisticAmount(queryForm);

        queryForm.setStartTime(LocalDate.now().withDayOfMonth(1));
        queryForm.setEndTime(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
        WaybillStatisticAmountVO currentMonth = waybillDao.statisticAmount(queryForm);

        WaybillStatisticAmountAppVO waybillStatisticAmountAppVO = SmartBeanUtil.copy(statisticAmountVO,WaybillStatisticAmountAppVO.class);
        waybillStatisticAmountAppVO.setCurrentMonthProfitAmount(currentMonth.getProfitAmount());
        waybillStatisticAmountAppVO.setCurrentMonthReceiveAmount(currentMonth.getReceiveAmount());
        return ResponseDTO.ok(waybillStatisticAmountAppVO);
    }


    /**
     * 查询分段运输信息
     * @param waybillId
     * @return
     */
    public List<WaybillSplitTransportVO> getSplitTransport(Long waybillId) {
        return waybillSplitTransportDao.selectByWaybillId(waybillId);
    }

    public ResponseDTO<String> splitTransportDispatch(WaybillSplitTransportDispatchForm dispatchForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillSplitTransportEntity waybillSplitTransportEntity = SmartBeanUtil.copy(dispatchForm, WaybillSplitTransportEntity.class);
        waybillSplitTransportDao.insert(waybillSplitTransportEntity);
        waybillDataTracerService.splitTransportDispatchLog(dispatchForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> splitTransportDelete(Long splitTransportId, DataTracerRequestForm dataTracerRequestForm) {
        WaybillSplitTransportVO waybillSplitTransportVO = waybillSplitTransportDao.detail(splitTransportId);
        if (waybillSplitTransportVO == null) {
            return ResponseDTO.ok();
        }
        Long waybillId = waybillSplitTransportVO.getWaybillId();
        Long driverId = waybillSplitTransportVO.getDriverId();
        List<PayOrderEntity> payOrderEntityList = payOrderDao.selectByWaybillIdAndFleetDriverId(waybillId, null, driverId, PayOrderStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isNotEmpty(payOrderEntityList)) {
            return ResponseDTO.userErrorParam("此段运输司机已存在结算信息无法删除");
        }
        waybillSplitTransportDao.deleteById(splitTransportId);
        waybillDataTracerService.splitTransportDeleteLog(waybillSplitTransportVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}