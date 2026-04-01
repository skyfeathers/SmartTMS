<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-11
 * @LastEditTime: 2022-07-12
 * @LastEditors: zhuoda
-->
<template>
  <a-card size="small">
    <a-page-header>
      <template #title>
        {{ '车牌号：' + vehicleDetail.vehicleNumber }}<SmartCopyIcon v-if="vehicleDetail.vehicleNumber" :value="vehicleDetail.vehicleNumber" />
      </template>
      <template #extra>
        <a-button danger @click="deleteVehicle([vehicleDetail.vehicleId], onClose)" v-privilege="'vehicle:delete'">删除</a-button>
        <a-button @click="updateVehicle" v-privilege="'vehicle:edit'">编辑</a-button>
        <a-button v-if="vehicleDetail.auditStatus === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" v-privilege="'vehicle:batchAudit'" type="primary" @click="auditVehicle">审核 </a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="经营方式">{{
              $smartEnumPlugin.getDescByValue('VEHICLE_BUSINESS_MODE_ENUM', vehicleDetail.businessMode)
            }}</a-descriptions-item>
           
            <a-descriptions-item label="绑定挂车">{{ vehicleDetail.bracketNo }}</a-descriptions-item>
            <a-descriptions-item label="绑定司机">{{ driverList.join(',') }}</a-descriptions-item>
            <a-descriptions-item label="负责人">{{ vehicleDetail.managerName }}</a-descriptions-item>
            
            
            
            <a-descriptions-item label="备注">{{ vehicleDetail.remark }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ vehicleDetail.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ vehicleDetail.createTime }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10 content-card" size="small">
    <a-tabs>
      <a-tab-pane key="1" tab="证件信息">
        <VehicleLicenseDetail :detail="vehicleDetail" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="维修信息">
        <RepairList :moduleType="REPAIR_MODULE_TYPE_ENUM.VEHICLE.value" :moduleId="Number(vehicleId)" />
      </a-tab-pane>
      <a-tab-pane key="3" tab="保险信息">
        <InsuranceList :moduleType="INSURANCE_MODULE_TYPE_ENUM.VEHICLE.value" :moduleId="vehicleId" />
      </a-tab-pane>
      <a-tab-pane key="contact" tab="运单信息">
        <WaybillTableList :vehicleId="vehicleId"/>
      </a-tab-pane>
      <a-tab-pane key="4" tab="审车登记">
        <VehicleReview :moduleId="Number(vehicleId)"/>
      </a-tab-pane>
      <a-tab-pane key="5" tab="车辆保养">
        <VehicleMaintenance :moduleId="Number(vehicleId)"/>
      </a-tab-pane>
      <a-tab-pane key="6" tab="操作记录" >
        <DataTracerList
            :business-id="vehicleDetail.vehicleId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.VEHICLE.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <AuditModal ref="auditModal" @refresh="getDetail" />
</template>
<script setup>
import { vehicleApi } from '/@/api/business/vehicle/vehicle-api';
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import { SmartLoading } from '/@/components/smart-loading';

import { AUDIT_STATUS_ENUM } from '/@/constants/common-const';
import { INSURANCE_MODULE_TYPE_ENUM } from '/@/constants/business/insurance-const';
import { REPAIR_MODULE_TYPE_ENUM } from '/@/constants/business/repair-const';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { VEHICLE_BUSINESS_MODE_ENUM } from '/@/constants/business/vehicle-const';

import { deleteVehicle } from './components/vehicle-operate';

import WaybillTableList from '/@/components/waybill-list/index.vue';
import VehicleLicenseDetail from './components/vehicle-license-detail.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import AuditModal from '/@/components/audit-modal/index.vue';
import InsuranceList from '/@/views/business/insurance/insurance-list.vue';
import RepairList from '/@/views/business/repair/index.vue';
import VehicleReview from '/@/views/business/vehicle-review/index.vue';
import VehicleMaintenance from '/@/views/business/vehicle-maintenance/index.vue';

// --------------------- 详情数据 ---------------------
let router = useRouter();
let route = useRoute();
let vehicleId = ref();
onMounted(() => {
  vehicleId.value = Number(route.query.vehicleId);
  getDetail();
});
let vehicleDetail = reactive({});

async function getDetail() {
  SmartLoading.show();
  try {
    let responseModel = await vehicleApi.getDetail(vehicleId.value);
    let detail = responseModel.data;
    Object.assign(vehicleDetail, detail);
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}

// 获取司机列表
const driverList = computed(() => {
  return (vehicleDetail.driverVehicleList || []).map(e => {
    return `${e.driverName}-${e.telephone}`;
  });
});
// ----------------- 关闭页面 -----------------
function onClose() {
  useUserStore().closePage(route, router);
}

// ----------------- 跳转 --------------------
function updateVehicle() {
  router.push({ path: '/vehicle/vehicle-form', query: { vehicleId: vehicleId.value } });
}

// ----------------- 审核 --------------------
let auditModal = ref();

function auditVehicle() {
  let params = { vehicleIdList: [vehicleId.value] };
  auditModal.value.showModal(params, vehicleApi.batchAudit);
}

</script>
<style lang="less" scoped>
.content-card {
  ::v-deep(.ant-card-body) {
    padding-top: 0px;
  }
}
</style>
