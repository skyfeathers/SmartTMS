<template>
  <a-card size="small">
    <a-page-header>
      <template #title>
        {{ driverDetail.driverName }}
        <SmartCopyIcon v-if="driverDetail.driverName" :value="driverDetail.driverName" />
      </template>
      <template #extra>
        <a-button danger @click="confirmDelete(driverDetail.driverId,onClose)" v-privilege="'driver:delete'">删除</a-button>
        <a-button @click="updateDriver" v-privilege="'driver:edit'">编辑</a-button>
        <a-button v-if="driverDetail.auditStatus === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" type="primary" v-privilege="'driver:audit'"
                  @click="auditDriver">审核
        </a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="司机电话">{{ driverDetail.telephone }}<SmartCopyIcon v-if="driverDetail.telephone" :value="driverDetail.telephone" /></a-descriptions-item>
            <a-descriptions-item label="经营方式">{{
                $smartEnumPlugin.getDescByValue('DRIVER_BUSINESS_MODE_ENUM',  driverDetail.businessMode)
              }}</a-descriptions-item>
            <a-descriptions-item label="关联车辆">{{ driverDetail.vehicleList }}</a-descriptions-item>
            <a-descriptions-item label="负责人">{{ driverDetail.managerName }}</a-descriptions-item>
            <a-descriptions-item label="余额">{{ driverDetail.balance }}</a-descriptions-item>
            <a-descriptions-item label="司机大头照"><file-preview type="picture" :fileList="driverDetail.photo"/></a-descriptions-item>
            <a-descriptions-item label="备注">{{ driverDetail.remark }}</a-descriptions-item>
            <a-descriptions-item label="状态">{{
                $smartEnumPlugin.getDescByValue('DRIVER_STATUS_ENUM', driverDetail.status)
              }}
            </a-descriptions-item>
            <a-descriptions-item label="创建人">{{ driverDetail.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ driverDetail.createTime }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ $smartEnumPlugin.getDescByValue('AUDIT_STATUS_ENUM',driverDetail.auditStatus) }}</div>
          </div>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10 content-card" size="small">
    <a-tabs>
      <a-tab-pane key="1" tab="证件信息">
        <DetailIdentityInformation :detail="driverDetail"/>
      </a-tab-pane>
      <a-tab-pane key="2" tab="银行卡信息">
        <DetailBank :detail="driverDetail"/>
      </a-tab-pane>
      <a-tab-pane key="3" tab="合同信息">
        <ContractList :signerType="CONTRACT_SIGNER_TYPE_ENUM.DRIVER.value" :signerBelongId="driverDetail.driverId"/>
      </a-tab-pane>
      <a-tab-pane key="contact" tab="运单信息">
        <WaybillTableList :driverId="driverId"/>
      </a-tab-pane>
      <a-tab-pane key="4" tab="操作记录" >
        <DataTracerList
            :business-id="driverDetail.driverId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.DRIVER.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <AuditModal ref="auditModal" @refresh="getDetail"/>
</template>
<script setup>
import {useSpinStore} from "/@/store/modules/system/spin";
import {driverApi} from "/@/api/business/driver/driver-api";
import {onMounted, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useDriverDelete} from "/@/views/business/driver/hooks/use-driver-delete";
import {useUserStore} from "/@/store/modules/system/user";
import {AUDIT_STATUS_ENUM} from "/@/constants/common-const";
import DetailIdentityInformation from "./components/detail-identity-information.vue"
import DetailBank from "./components/detail-bank.vue"
import AuditModal from '/@/components/audit-modal/index.vue'
import ContractList from '/@/views/business/contract/contract-list.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import FilePreview from '/@/components/file-preview/index.vue'
import WaybillTableList from '/@/components/waybill-list/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { CONTRACT_SIGNER_TYPE_ENUM } from '/@/constants/business/contract-const';
import { VEHICLE_BUSINESS_MODE_ENUM } from '/@/constants/business/vehicle-const';

let router = useRouter();
let route = useRoute();
let driverId = ref();
onMounted(() => {
  driverId.value = route.query.driverId;
  getDetail();
})
// --------------------- 司机详情 ---------------------
let driverDetail = reactive({});

async function getDetail() {
  useSpinStore().show();
  try {
    let responseModel = await driverApi.getDriver(driverId.value);
    let detail = responseModel.data;
    Object.assign(driverDetail, detail);
    driverDetail.vehicleList = (driverDetail.driverVehicleList || []).map(e=>e.vehicleNumber).join(',');
  } catch (error) {
    console.log("error", error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------- 删除 --------------------
let {confirmDelete} = useDriverDelete();

// ----------------- 关闭页面 -----------------
function onClose() {
  useUserStore().closePage(route, router);
}

// ----------------- 跳转 --------------------
function updateDriver() {
  router.push({path: '/driver/operate', query: {driverId: driverId.value}})
}

// ----------------- 审核 --------------------
let auditModal = ref();

function auditDriver() {
  let params = {driverId: driverId.value};
  auditModal.value.showModal(params, driverApi.auditDriver);
}
</script>
<style lang='less' scoped>
.content {
  display: flex;
}

.status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  width: max-content;
  margin-right: 30px;

  .label {
    font-size: 15px;
    color: #8c8c8c;
  }

  .value {
    font-size: 25px;
    color: #1f1f1f;
  }
}

.ant-page-header {
  padding: 0;
}
.content-card {
  ::v-deep(.ant-card-body) {
    padding-top: 0px;
  }
}
</style>
