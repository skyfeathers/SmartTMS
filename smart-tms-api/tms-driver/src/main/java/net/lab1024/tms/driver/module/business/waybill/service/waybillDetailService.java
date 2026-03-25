package net.lab1024.tms.driver.module.business.waybill.service;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillGoodsEntity;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.driver.module.business.order.dao.OrderPathDao;
import net.lab1024.tms.driver.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.driver.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.driver.module.business.shipper.domain.dto.ShipperSimpleNameDTO;
import net.lab1024.tms.driver.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.driver.module.business.waybill.dao.*;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class waybillDetailService {
    
    @Resource
    private WaybillDao waybillDao;

    @Resource
    private DriverDao driverDao;

    @Resource
    private VehicleDao vehicleDao;
    
    @Resource
    private FleetDao fleetDao;
    
    @Resource
    private WaybillGoodsDao waybillGoodsDao;
    
    @Resource
    private DictCacheService dictCacheService;
    
    @Resource
    private OrderPathDao orderPathDao;
    
    @Resource
    private ShipperDao shipperDao;
    
    @Resource
    private WaybillVoucherDao waybillVoucherDao;
    
    /**
     * 设置订单详情信息
     *
     * @param waybillId
     * @return
     */
    public WaybillDetailVO selectWaybillDetail(Long waybillId) {
        WaybillVO waybillVO = waybillDao.selectWaybillDetail(waybillId);
        if (ObjectUtil.isEmpty(waybillVO)) {
            return null;
        }
        WaybillDetailVO waybillDetailVO = SmartBeanUtil.copy(waybillVO, WaybillDetailVO.class);
        // 构建车辆、车队、货主
        this.buildVehicleFleetShipper(Lists.newArrayList(waybillDetailVO));
        // 商品信息
        this.buildGoodsInfo(Lists.newArrayList(waybillDetailVO));
        // 路线信息
        this.buildOrderPathList(Lists.newArrayList(waybillDetailVO));
        // 物流凭证信息
        this.buildWaybillVoucherList(Lists.newArrayList(waybillDetailVO));
        return waybillDetailVO;
    }
    
    /**
     * 设置车辆、车队、货主信息
     *
     * @param waybillVOList
     */
    public void buildVehicleFleetShipper(List<WaybillVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        //司机信息
        Set<Long> driverIdList = waybillVOList.stream().map(WaybillVO::getDriverId).collect(Collectors.toSet());
        Map<Long, DriverEntity> driverMap = this.getDriverMap(driverIdList);
        //车辆
        Set<Long> vehicleIdList = waybillVOList.stream().map(WaybillVO::getVehicleId).collect(Collectors.toSet());
        Map<Long, VehicleEntity> vehicleMap = this.getVehicleMap(vehicleIdList);
        //车队
        Set<Long> fleetIdList = waybillVOList.stream().filter(e -> e.getFleetId() != null).map(WaybillVO::getFleetId).collect(Collectors.toSet());
        Map<Long, FleetEntity> fleetMap = this.getFleetMap(fleetIdList);
        //货主
        Set<Long> shipperIdList = waybillVOList.stream().map(WaybillVO::getShipperId).collect(Collectors.toSet());
        Map<Long, ShipperSimpleNameDTO> shipperMap = this.getShipperMap(shipperIdList);
        
        //进行注入
        for (WaybillVO waybillVO : waybillVOList) {
            // 订单货主信息
            this.buildShipperInfo(waybillVO, shipperMap);
            waybillVO.setSettleTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(waybillVO.getSettleType(), WaybillSettleTypeEnum.class));
            // 车辆、车队
            DriverEntity driverEntity = driverMap.get(waybillVO.getDriverId());
            if (driverEntity != null) {
                waybillVO.setDriverName(driverEntity.getDriverName());
                waybillVO.setDriverTelephone(driverEntity.getTelephone());
            }
            VehicleEntity vehicleEntity = vehicleMap.get(waybillVO.getVehicleId());
            if (vehicleEntity != null) {
                waybillVO.setVehicleNumber(vehicleEntity.getVehicleNumber());
            }
            if (waybillVO.getFleetId() == null) {
                continue;
            }
            FleetEntity fleetEntity = fleetMap.get(waybillVO.getFleetId());
            if (fleetEntity != null) {
                waybillVO.setFleetName(fleetEntity.getFleetName());
                waybillVO.setFleetCaptainPhone(fleetEntity.getCaptainPhone());
            }
        }
    }
    
    /**
     * 商品信息
     *
     * @param waybillVOList
     */
    public void buildGoodsInfo(List<WaybillDetailVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        List<Long> waybillIdList = waybillVOList.stream().map(WaybillVO::getWaybillId).collect(Collectors.toList());
        // 运单货物信息
        List<WaybillGoodsEntity> waybillGoodsEntityList = waybillGoodsDao.selectByWaybillIdList(waybillIdList);
        if (CollectionUtils.isEmpty(waybillGoodsEntityList)) {
            waybillVOList.forEach(e -> e.setGoodsList(Lists.newArrayList()));
            return;
        }
        
        //合并数据
        List<WaybillGoodsVO> waybillGoodsVOList = SmartBeanUtil.copyList(waybillGoodsEntityList, WaybillGoodsVO.class);
        Map<Long, List<WaybillGoodsVO>> waybillGoodsListMap = waybillGoodsVOList.stream().collect(groupingBy(WaybillGoodsVO::getWaybillId));
        
        for (WaybillDetailVO waybillDetailVO : waybillVOList) {
            Long waybillId = waybillDetailVO.getWaybillId();
            //运单货物
            List<WaybillGoodsVO> goodsList = waybillGoodsListMap.getOrDefault(waybillId, Lists.newArrayList());
            
            for (WaybillGoodsVO waybillGoodsVO : goodsList) {
                waybillGoodsVO.setGoodsTypeName(dictCacheService.selectValueNameByValueCode(waybillGoodsVO.getGoodsType()));
            }
            waybillDetailVO.setGoodsList(goodsList);
        }
    }
    
    /**
     * 设置订单路径信息
     *
     * @param waybillVOList
     */
    private void buildOrderPathList(List<WaybillDetailVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        List<Long> orderIdList = waybillVOList.stream().map(WaybillDetailVO::getOrderId).collect(Collectors.toList());
        List<OrderPathEntity> pathList = orderPathDao.selectByOrderIdList(orderIdList);
        Map<Long, List<OrderPathEntity>> pathMap = pathList.stream().collect(groupingBy(OrderPathEntity::getOrderId));
        waybillVOList.forEach(item -> {
            List<OrderPathEntity> orderPathList = pathMap.getOrDefault(item.getOrderId(), Lists.newArrayList());
            orderPathList.sort((Comparator.comparing(OrderPathEntity::getType)));
            item.setPathList(SmartBeanUtil.copyList(orderPathList, OrderPathDTO.class));
        });
    }

    /**
     * 司机信息
     *
     * @param driverIdList
     * @return
     */
    public Map<Long, DriverEntity> getDriverMap(Collection<Long> driverIdList) {
        Map<Long, DriverEntity> driverMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(driverIdList)) {
            return driverMap;
        }
        List<DriverEntity> driverEntityList = driverDao.selectBatchIds(driverIdList);
        if (CollectionUtils.isNotEmpty(driverEntityList)) {
            driverMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, Function.identity()));

        }
        return driverMap;
    }
    
    /**
     * 车辆信息
     *
     * @param vehicleIdList
     * @return
     */
    public Map<Long, VehicleEntity> getVehicleMap(Collection<Long> vehicleIdList) {
        Map<Long, VehicleEntity> vehicleMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            return vehicleMap;
        }
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        if (CollectionUtils.isNotEmpty(vehicleEntityList)) {
            vehicleMap = vehicleEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, Function.identity()));
        }
        return vehicleMap;
    }
    
    /**
     * 车队信息
     *
     * @param fleetIdList
     * @return
     */
    private Map<Long, FleetEntity> getFleetMap(Collection<Long> fleetIdList) {
        Map<Long, FleetEntity> fleetMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(fleetIdList)) {
            return fleetMap;
        }
        List<FleetEntity> fleetEntityList = fleetDao.selectBatchIds(fleetIdList);
        if (CollectionUtils.isNotEmpty(fleetEntityList)) {
            fleetMap = fleetEntityList.stream().collect(Collectors.toMap(FleetEntity::getFleetId, Function.identity()));
        }
        return fleetMap;
    }
    
    /**
     * 货主信息
     *
     * @param shipperIdList
     * @return
     */
    private Map<Long, ShipperSimpleNameDTO> getShipperMap(Collection<Long> shipperIdList) {
        Map<Long, ShipperSimpleNameDTO> shipperMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return shipperMap;
        }
        List<ShipperEntity> shipperEntityList = shipperDao.selectBatchIds(shipperIdList);
    
        List<ShipperSimpleNameDTO> simpleNameDTOList = SmartBeanUtil.copyList(shipperEntityList, ShipperSimpleNameDTO.class);
        shipperMap= simpleNameDTOList.stream().collect(Collectors.toMap(ShipperSimpleNameDTO::getShipperId, Function.identity()));
        return shipperMap;
    }
    
    /**
     * 设置货主信息
     *
     * @param waybillVO
     * @param shipperMap
     */
    private void buildShipperInfo(WaybillVO waybillVO, Map<Long, ShipperSimpleNameDTO> shipperMap) {
        ShipperSimpleNameDTO shipperSimpleNameDTO = shipperMap.get(waybillVO.getShipperId());
        if (ObjectUtil.isNotEmpty(shipperSimpleNameDTO)) {
            waybillVO.setShipperName(shipperSimpleNameDTO.getConsignor());
            waybillVO.setShortName(shipperSimpleNameDTO.getShortName());
        }
    }
    
    /**
     * 设置物流凭证信息
     *
     * @param waybillVOList
     */
    private void buildWaybillVoucherList(List<WaybillDetailVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        for (WaybillDetailVO waybillDetailVO : waybillVOList) {
            List<WaybillVoucherVO> waybillVoucherVOList = waybillVoucherDao.selectByWaybillId(waybillDetailVO.getWaybillId());
            waybillDetailVO.setVoucherVOList(waybillVoucherVOList);
        }
    }
}
