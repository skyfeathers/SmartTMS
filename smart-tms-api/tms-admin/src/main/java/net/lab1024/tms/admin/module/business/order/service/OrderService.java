package net.lab1024.tms.admin.module.business.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.material.businesstype.BusinessTypeService;
import net.lab1024.tms.admin.module.business.material.cabinet.CabinetService;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderGoodsDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderMailAddressDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderPathDao;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderGoodsDTO;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.admin.module.business.order.domain.form.*;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderExportVO;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderGoodsVO;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderVO;
import net.lab1024.tms.admin.module.business.order.manager.OrderGoodsManager;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderWaybillDTO;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillGoodsDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.msg.MsgCommonService;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.domain.MsgSendDTO;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillGoodsEntity;
import net.lab1024.tms.common.module.support.baidumap.domain.BaiduAddressDistanceQueryForm;
import net.lab1024.tms.common.module.support.baidumap.BaiduMapService;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 订单Service
 *
 * @author lidoudou
 * @date 2022/7/13 下午2:38
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private OrderPathDao orderPathDao;
    @Autowired
    private BusinessTypeService businessTypeService;
    @Autowired
    private CabinetService cabinetService;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private OrderDataTracerService orderDataTracerService;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private OrderMailAddressDao orderMailAddressDao;
    @Autowired
    private WaybillGoodsDao waybillGoodsDao;
    @Autowired
    private MsgCommonService msgCommonService;
    @Autowired
    private BaiduMapService baiduMapService;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private OrderGoodsManager orderGoodsManager;
    @Autowired
    private ReceiveOrderDao receiveOrderDao;

    /**
     * 分页查询信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<OrderVO>> queryByPage(OrderQueryForm queryForm) {
        if (BooleanUtils.isTrue(queryForm.getHideCancelFlag())) {
            queryForm.setExcludeOrderStatus(OrderStatusEnum.CANCEL.getValue());
        }
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OrderVO> orderList = orderDao.queryByPage(page, queryForm);
        PageResult<OrderVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, orderList);
        this.buildOtherInfo(orderList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 设置路线
     *
     * @param orderList
     */
    private void buildPathList(List<OrderVO> orderList) {
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }
        List<Long> orderIdList = orderList.stream().map(OrderVO::getOrderId).collect(Collectors.toList());
        List<OrderPathEntity> pathList = orderPathDao.selectByOrderIdList(orderIdList);
        Map<Long, List<OrderPathEntity>> pathMap = pathList.stream().collect(groupingBy(OrderPathEntity::getOrderId));
        orderList.forEach(item -> {
            List<OrderPathEntity> orderPathList = pathMap.getOrDefault(item.getOrderId(), Lists.newArrayList());
            orderPathList.sort((Comparator.comparing(OrderPathEntity::getType)));
            item.setPathList(SmartBeanUtil.copyList(orderPathList, OrderPathDTO.class));
        });
    }

    /**
     * 设置业务类型 和 柜型
     *
     * @param orderList
     */
    private void buildOtherInfo(List<OrderVO> orderList) {
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }
        List<Long> orderIdList = orderList.stream().map(OrderVO::getOrderId).collect(Collectors.toList());
        // 业务类型
        Set<Long> businessTypeIdList = orderList.stream().filter(e -> e.getContainerBusinessTypeId() != null).map(OrderVO::getContainerBusinessTypeId).collect(Collectors.toSet());
        Map<Long, String> businessTypeNameMap = businessTypeService.getBusinessTypeMap(businessTypeIdList);
        // 柜型
        Set<Long> cabinetIdList = orderList.stream().filter(e -> e.getContainerBusinessTypeId() != null).map(OrderVO::getCabinetId).collect(Collectors.toSet());
        Map<Long, String> cabinetNameMap = cabinetService.getCabinetMap(cabinetIdList);
        // 业务负责人
        Set<Long> managerIdSet = orderList.stream().filter(e->e.getManagerId() != null).map(OrderVO::getManagerId).collect(Collectors.toSet());
        Map<Long, String> employeeNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(managerIdSet)) {
            List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(managerIdSet);
            employeeNameMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        }
        // 设置货物信息
        List<OrderGoodsVO> orderGoodsVOList = orderGoodsService.selectByOrderIdList(orderIdList);
        Map<Long, List<OrderGoodsVO>>  orderGoodsMap = orderGoodsVOList.stream().collect(groupingBy(OrderGoodsVO::getOrderId));
        for (OrderVO item : orderList) {
            item.setContainerBusinessTypeName(businessTypeNameMap.getOrDefault(item.getContainerBusinessTypeId(), StringConst.EMPTY));
            item.setCabinetName(cabinetNameMap.getOrDefault(item.getCabinetId(), StringConst.EMPTY));
            item.setAreaDesc(dictCacheService.selectValueNameByValueCode(item.getAreaId()));
            item.setManagerName(employeeNameMap.getOrDefault(item.getManagerId(), ""));
            item.setGoodsList(orderGoodsMap.getOrDefault(item.getOrderId(), Lists.newArrayList()));
        }

    }

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    public OrderVO getDetail(Long orderId) {
        if (orderId == null) {
            return null;
        }
        OrderVO orderVO = orderDao.getOrder(orderId, Boolean.FALSE);
        if (null == orderVO) {
            return null;
        }
        this.buildPathList(Arrays.asList(orderVO));
        orderVO.setAreaDesc(dictCacheService.selectValueNameByValueCode(orderVO.getAreaId()));
        // 设置货物信息
        orderVO.setGoodsList(orderGoodsService.selectByOrderIdList(Lists.newArrayList(orderId)));
        this.buildOtherInfo(Arrays.asList(orderVO));
        return orderVO;
    }

    /**
     * 创建订单
     *
     * @param createForm
     * @return
     */
    public ResponseDTO<Long> createOrder(OrderCreateForm createForm, DataTracerRequestForm dataTracerRequestForm) {
        ResponseDTO<String> validateResult = validate(createForm);
        if (!validateResult.getOk()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, validateResult.getMsg());
        }
        // 距离自动计算
        ResponseDTO<BigDecimal> distanceResp = this.getDistance(createForm.getPathList());
        if (!distanceResp.getOk()) {
            return ResponseDTO.userErrorParam(distanceResp.getMsg());
        }
        BigDecimal distance = distanceResp.getData();
        // 设置路线经纬度
        this.buildPathLatLon(createForm.getPathList());
        // 处理订单实体
        OrderEntity orderEntity = SmartBeanUtil.copy(createForm, OrderEntity.class);
        orderEntity.setOrderNo(serialNumberService.generate(SerialNumberIdEnum.ORDER));
        orderEntity.setStatus(OrderStatusEnum.TRANSPORT.getValue());
        orderEntity.setDistance(distance);
        orderManager.saveOrder(orderEntity, createForm);
        OrderVO orderVO = this.getDetail(orderEntity.getOrderId());
        orderDataTracerService.saveLog(orderEntity.getOrderId(), createForm, orderVO, dataTracerRequestForm);

        MsgSendDTO sendDTO = new MsgSendDTO();
        sendDTO.setEnterpriseId(orderEntity.getEnterpriseId());
        sendDTO.setMsgSubType(MsgSubTypeEnum.ORDER_CREATE);
        sendDTO.setReceiverType(MsgReceiverTypeEnum.ADMIN);
        sendDTO.setReceiverId(orderEntity.getScheduleId());
        sendDTO.setDataId(orderEntity.getOrderId());
        msgCommonService.send(sendDTO);

        return ResponseDTO.ok(orderEntity.getOrderId());
    }

    /**
     * 设置路线经纬度
     * @param pathList
     */
    private void buildPathLatLon(List<OrderPathDTO> pathList){
        if (CollectionUtils.isEmpty(pathList)) {
            return;
        }
        for (OrderPathDTO pathDTO : pathList) {
            String address = this.getLocation(pathDTO);
            Map<String, String> latLngMap = baiduMapService.getLocationLatLng(address);
            BigDecimal lat = SmartBigDecimalUtil.setScale(new BigDecimal(latLngMap.getOrDefault(BaiduMapService.LAT, "0")), 6);
            BigDecimal lng = SmartBigDecimalUtil.setScale(new BigDecimal(latLngMap.getOrDefault(BaiduMapService.LNG, "0")), 6);
            pathDTO.setLatitude(lat);
            pathDTO.setLongitude(lng);
        }
    }

    /**
     * 自动计算距离
     *
     * @param pathList
     * @return
     */
    public ResponseDTO<BigDecimal> getDistance(List<OrderPathDTO> pathList) {
        List<OrderPathDTO> placingLoadingList = pathList.stream().filter(e -> PathTypeEnum.PLACING_LOADING.equalsValue(e.getType())).collect(Collectors.toList());
        String placingLoadingAddress = "";
        String placingLoadingLatLng = "";
        if (CollectionUtils.isNotEmpty(placingLoadingList)) {
            placingLoadingAddress = this.getLocation(placingLoadingList.get(0));
            placingLoadingLatLng = this.getLocationLatLng(placingLoadingList.get(0));
        }
        List<OrderPathDTO> unloadingLocationList = pathList.stream().filter(e -> PathTypeEnum.UNLOADING_LOCATION.equalsValue(e.getType())).collect(Collectors.toList());
        String unloadingLocationAddress = "";
        String unloadingLocationLatLng = "";
        if (CollectionUtils.isNotEmpty(unloadingLocationList)) {
            unloadingLocationAddress = this.getLocation(unloadingLocationList.get(unloadingLocationList.size() - 1));
            unloadingLocationLatLng =  this.getLocationLatLng(unloadingLocationList.get(unloadingLocationList.size() - 1));
        }
        if (StringUtils.isBlank(placingLoadingAddress) || StringUtils.isBlank(unloadingLocationAddress)) {
            return ResponseDTO.userErrorParam("装卸货地址不能为空");
        }
        BaiduAddressDistanceQueryForm distanceQueryForm = new BaiduAddressDistanceQueryForm();
        distanceQueryForm.setSourceAddress(placingLoadingAddress);
        distanceQueryForm.setSourceLatLng(placingLoadingLatLng);
        distanceQueryForm.setDestAddress(unloadingLocationAddress);
        distanceQueryForm.setDestLatLng(unloadingLocationLatLng);
        ResponseDTO<Integer> resp = baiduMapService.distanceQuery(distanceQueryForm);
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        return ResponseDTO.ok(new BigDecimal(resp.getData()));
    }

    /**
     * 获取经纬度
     * @param path
     * @return
     */
    private String getLocationLatLng(OrderPathDTO path) {
        String latLng = StringConst.EMPTY;
        if (null != path.getLatitude() && null != path.getLongitude()) {
            latLng = path.getLatitude() + "," + path.getLongitude();
        }
        return latLng;
    }

    private String getLocation(OrderPathDTO path) {
        String address = "";
        if (StringUtils.isNotBlank(path.getProvinceName())) {
            address = address + path.getProvinceName();
        }
        if (StringUtils.isNotBlank(path.getCityName())) {
            address = address + path.getCityName();
        }
        if (StringUtils.isNotBlank(path.getDistrictName())) {
            address = address + path.getDistrictName();
        }
        if (StringUtils.isNotBlank(path.getAddress())) {
            address = address + path.getAddress();
        }
        return address;
    }
    /**
     * 更新订单信息
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateOrder(OrderUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long orderId = updateForm.getOrderId();
        OrderEntity orderEntity = orderDao.selectById(orderId);
        if (null == orderEntity || orderEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "订单数据不存在");
        }
        if (!orderEntity.getEnterpriseId().equals(updateForm.getEnterpriseId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您登录的企业不是该订单的所属公司");
        }
        //基础校验
        ResponseDTO<String> validateResult = validate(updateForm);
        if (!validateResult.getOk()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, validateResult.getMsg());
        }
        // 本次删除的商品信息
        List<OrderGoodsEntity> dbOrderGoodsList = orderGoodsDao.selectByOrderIdList(Lists.newArrayList(orderId));
        List<Long> goodsIdList = updateForm.getGoodsList().stream().filter(e -> e.getOrderGoodsId() != null).map(OrderGoodsDTO::getOrderGoodsId).collect(Collectors.toList());
        List<Long> deleteGoodsIdList = dbOrderGoodsList.stream().map(OrderGoodsEntity::getOrderGoodsId).collect(Collectors.toList())
                .stream().filter(e -> !goodsIdList.contains(e)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deleteGoodsIdList)) {
            List<WaybillGoodsEntity> waybillGoodsEntityList = waybillGoodsDao.selectByOrderGoodsIdList(deleteGoodsIdList, WaybillStatusEnum.CANCEL.getValue());
            if (CollectionUtils.isNotEmpty(waybillGoodsEntityList)) {
                Set<String> goodsNameList = waybillGoodsEntityList.stream().map(WaybillGoodsEntity::getGoodsName).collect(Collectors.toSet());
                return ResponseDTO.userErrorParam(StringUtils.join(goodsNameList, ",") + "已分配过运单，不能删除");
            }
        }

        OrderVO beforeData = this.getDetail(orderId);
        updateForm.setOrderType(orderEntity.getOrderType());
        orderManager.updateOrder(updateForm, deleteGoodsIdList, dataTracerRequestForm);
        OrderVO afterData = this.getDetail(orderId);
        orderDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新订单货主
     *
     * @param orderId
     * @param newShipperId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> updateOrderShipper(Long orderId, Long newShipperId, DataTracerRequestForm dataTracerRequestForm) {
        OrderEntity orderEntity = orderDao.selectById(orderId);
        if (orderEntity == null || orderEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "订单数据不存在");
        }
        Long oldShipperId = orderEntity.getShipperId();
        if (Objects.equals(oldShipperId, newShipperId)) {
            return ResponseDTO.ok();
        }
        // 如果订单下的运单已经提交应收，则不允许修改
        List<WaybillEntity> waybillEntityList = waybillDao.selectByOrderId(orderId, WaybillStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isNotEmpty(waybillEntityList)) {
            List<Long> waybillIdList = waybillEntityList.stream().map(WaybillEntity::getWaybillId).collect(Collectors.toList());
            List<ReceiveOrderWaybillDTO> receiveOrderWaybillDTOList = receiveOrderDao.selectByWaybillIdList(waybillIdList, CheckStatusEnum.CANCEL.getValue());
            if (CollectionUtils.isNotEmpty(receiveOrderWaybillDTOList)) {
                return ResponseDTO.userErrorParam("已有运单提交应收，不可修改货主");
            }
        }

        ShipperEntity newShipperEntity = shipperDao.selectById(newShipperId);
        if (newShipperEntity == null || newShipperEntity.getDeletedFlag() || newShipperEntity.getDisableFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "更换的货主不存在或已禁用");
        }
        ShipperEntity oldShipperEntity = shipperDao.selectById(oldShipperId);

        orderManager.updateOrderShipperHandler(orderId, newShipperEntity, dataTracerRequestForm);
        orderDataTracerService.updateShipperLog(orderId, oldShipperEntity, newShipperEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 验证提交参数
     *
     * @param createForm
     * @return
     */
    private ResponseDTO<String> validate(OrderCreateForm createForm) {
        ShipperEntity shipperEntity = shipperDao.selectById(createForm.getShipperId());
        if (null == shipperEntity || shipperEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("货主信息不存在");
        }
        List<ShipperPrincipalDTO> managerList = shipperPrincipalDao.selectByShipperIdListAndType(Lists.newArrayList(createForm.getShipperId()), PrincipalTypeEnum.MANAGER.getValue());
        if (CollectionUtils.isEmpty(managerList)) {
            return ResponseDTO.userErrorParam("货主业务负责人不能为空");
        }
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(createForm.getEnterpriseId());
        if (null == enterpriseEntity || enterpriseEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("订单所属公司不存在");
        }
        EnterpriseEntity shipperEnterpriseEntity = enterpriseDao.selectById(shipperEntity.getEnterpriseId());
        // 如果订单所属公司与货主的公司不一致，提示
        if (!createForm.getEnterpriseId().equals(shipperEntity.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("货主属于【" + shipperEnterpriseEntity.getEnterpriseName() + "】与订单所属公司不一致");
        }
        createForm.setManagerId(managerList.get(NumberConst.ZERO).getEmployeeId());
        return ResponseDTO.ok();
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> cancelOrder(Long orderId, DataTracerRequestForm dataTracerRequestForm) {
        OrderEntity orderEntity = orderDao.selectById(orderId);
        if (null == orderEntity || orderEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "订单数据不存在");
        }
        List<WaybillEntity> waybillEntityList = waybillDao.selectByOrderId(orderId, WaybillStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isNotEmpty(waybillEntityList)) {
            return ResponseDTO.userErrorParam("该订单已分配过运单，不能取消");
        }
        orderEntity.setStatus(OrderStatusEnum.CANCEL.getValue());
        orderDao.updateById(orderEntity);
        orderDataTracerService.cancelLog(orderId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 导出订单列表
     *
     * @param queryForm
     * @return
     */
    public List<OrderExportVO> queryByExport(OrderQueryForm queryForm) {
        if (BooleanUtils.isTrue(queryForm.getHideCancelFlag())) {
            queryForm.setExcludeOrderStatus(OrderStatusEnum.CANCEL.getValue());
        }
        queryForm.setDeletedFlag(Boolean.FALSE);
        List<OrderVO> orderList = orderDao.queryByPage(null, queryForm);
        this.buildOtherInfo(orderList);
        List<OrderExportVO> exportList = orderList.stream().map(e -> {
            OrderExportVO exportVO = SmartBeanUtil.copy(e, OrderExportVO.class);
            exportVO.setOrderTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getOrderType(), OrderTypeEnum.class));
            exportVO.setContainerBusinessTypeName(SmartBaseEnumUtil.getEnumDescByValue(e.getBusinessTypeCode(), TransportationTypeEnum.class));
            exportVO.setStatusDesc("正常");
            if (OrderStatusEnum.CANCEL.equalsValue(e.getStatus())) {
                exportVO.setStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getStatus(), OrderStatusEnum.class));
            }
            return exportVO;
        }).collect(Collectors.toList());
        return exportList;
    }

    /**
     * 更新订单分配状态、商品数量
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> updateGoodsList(OrderScheduleFlagUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long orderId = updateForm.getOrderId();
        OrderEntity dbOrderEntity = orderDao.selectById(orderId);
        if (null == dbOrderEntity || dbOrderEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("订单不存在");
        }
        Set<Long> orderGoodsIdList = updateForm.getGoodsList().stream().map(OrderGoodsCountUpdateForm::getOrderGoodsId).collect(Collectors.toSet());
        List<OrderGoodsEntity> dbOrderGoodsEntityList = orderGoodsDao.selectBatchIds(orderGoodsIdList);
        if (dbOrderGoodsEntityList.size() != orderGoodsIdList.size()) {
            return ResponseDTO.userErrorParam("商品不存在");
        }
        // 设置更新商品
        Map<Long, String> orderGoodsNameMap = dbOrderGoodsEntityList.stream().collect(Collectors.toMap(OrderGoodsEntity::getOrderGoodsId, OrderGoodsEntity::getGoodsName));
        List<OrderGoodsEntity> orderGoodsEntityList = updateForm.getGoodsList().stream().map(goods -> {
            OrderGoodsEntity orderGoodsEntity = SmartBeanUtil.copy(goods, OrderGoodsEntity.class);
            orderGoodsEntity.setGoodsName(orderGoodsNameMap.get(goods.getOrderGoodsId()));
            return orderGoodsEntity;
        }).collect(Collectors.toList());
        orderGoodsManager.updateBatchById(orderGoodsEntityList);
        orderGoodsService.updateOrderScheduleFlag(orderId);
        orderDataTracerService.updateScheduleFlagLog(orderId, dbOrderGoodsEntityList, orderGoodsEntityList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
