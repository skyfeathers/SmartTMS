<template>
  <a-card size="small">
    <a-page-header :title="fleetDetail.fleetName">
      <template #extra>
        <a-button danger @click="confirmDelete(fleetDetail.fleetId,onClose)" v-privilege="'fleet:delete'">删除</a-button>
        <a-button @click="updateFleet" v-privilege="'fleet:edit'">编辑</a-button>
        <a-button v-if="fleetDetail.auditStatus === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" type="primary"
                  @click="auditFleet" v-privilege="'fleet:batchAudit'">审核
        </a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="速记码">{{ fleetDetail.shorthandCode }}</a-descriptions-item>
            <a-descriptions-item label="车队长">{{ fleetDetail.captainName }}</a-descriptions-item>
            <a-descriptions-item label="车队长联系方式">{{ fleetDetail.captainPhone }}</a-descriptions-item>
            <a-descriptions-item label="负责人">{{ fleetDetail.managerName }}</a-descriptions-item>
            <a-descriptions-item label="身份证号">{{ fleetDetail.captainIdCard }}</a-descriptions-item>
            <a-descriptions-item label="身份证（人面像）">
              <file-preview :fileList="fleetDetail.captainIdCardBackPic"/>
            </a-descriptions-item>
            <a-descriptions-item label="身份证（国徽像）">
              <file-preview :fileList="fleetDetail.captainIdCardFrontPic"/>
            </a-descriptions-item>
            <a-descriptions-item label="备注">{{ fleetDetail.remark }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ fleetDetail.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ fleetDetail.createTime }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ $smartEnumPlugin.getDescByValue('AUDIT_STATUS_ENUM',fleetDetail.auditStatus) }}</div>
          </div>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card v-if="fleetId" class="smart-margin-top10 content-card" size="small">
    <a-tabs>
      <a-tab-pane key="1" tab="车队车辆">
        <DetailVehicle :fleetId="fleetId"/>
      </a-tab-pane>
      <a-tab-pane key="2" tab="车队司机">
        <DetailDriver :fleetId="fleetId"/>
      </a-tab-pane>
      <a-tab-pane key="3" tab="银行卡信息">
        <DetailBank :detail="fleetDetail"/>
      </a-tab-pane>
      <a-tab-pane key="4" tab="合同信息">
        <ContractList :signerType="CONTRACT_SIGNER_TYPE_ENUM.FLEET.value" :signerBelongId="fleetDetail.fleetId"/>
      </a-tab-pane>
      <a-tab-pane key="5" tab="操作记录" >
        <DataTracerList
            :business-id="fleetId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.FLEET.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <AuditModal ref="auditModal" @refresh="getDetail"/>
</template>
<script setup>
import {useSpinStore} from "/@/store/modules/system/spin";
import {fleetApi} from "/@/api/business/fleet/fleet-api";
import {onMounted, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useFleetDelete} from "/@/views/business/fleet/hooks/use-fleet-delete";
import {useUserStore} from "/@/store/modules/system/user";
import {AUDIT_STATUS_ENUM} from "/@/constants/common-const";
import FilePreview from '/@/components/file-preview/index.vue'
import DetailDriver from './components/detail-driver.vue';
import DetailVehicle from './components/detail-vehicle.vue';
import DetailBank from './components/detail-bank.vue';
import AuditModal from '/@/components/audit-modal/index.vue'
import ContractList from '/@/views/business/contract/contract-list.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import {CONTRACT_SIGNER_TYPE_ENUM} from "/@/constants/business/contract-const";

let router = useRouter();
let route = useRoute();
let fleetId = ref();
onMounted(() => {
  fleetId.value = Number(route.query.fleetId);
  getDetail();
})
// --------------------- 车队详情 ---------------------
let fleetDetail = reactive({});

async function getDetail() {
  useSpinStore().show();
  try {
    let responseModel = await fleetApi.getFleet(fleetId.value);
    let detail = responseModel.data;
    Object.assign(fleetDetail, detail);
  } catch (error) {
    console.log("error", error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------- 删除 --------------------
let {confirmDelete} = useFleetDelete();

// ----------------- 关闭页面 -----------------
function onClose() {
  useUserStore().closePage(route, router);
}

// ----------------- 跳转 --------------------
function updateFleet() {
  router.push({path: '/fleet/operate', query: {fleetId: fleetId.value}})
}

// ----------------- 审核 --------------------
let auditModal = ref();

function auditFleet() {
  let params = {fleetIdList: [fleetId.value]};
  auditModal.value.showModal(params, fleetApi.batchAuditFleet);
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
