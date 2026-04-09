<!--
 * @Author: lp
 * @Date: 2022-07-13 17:59:11
 * @LastEditors: lp
 * @LastEditTime: 2022-07-17 17:27:48
 * @Description: 挂车详情
 * @FilePath: \nft-admin-web\src\views\business\bracket\bracket-detail.vue
-->
<template>
  <div>
    <a-card size="small">
      <a-page-header>
        <template #title>
          {{ bracketDetail.bracketNo }}
          <SmartCopyIcon v-if="bracketDetail.bracketNo" :value="bracketDetail.bracketNo" />
        </template>
        <template #extra>
          <a-button danger @click="confirmDelete([bracketDetail.bracketId], onClose)"
            v-privilege="'bracket:delete'">删除</a-button>
          <a-button @click="updateBracket" v-privilege="'bracket:edit'">编辑</a-button>
          <a-button v-if="bracketDetail.auditStatus === AUDIT_STATUS_ENUM.WAIT_AUDIT.value"
            v-privilege="'bracket:batchAudit'" type="primary" @click="auditBracket">审核
          </a-button>
        </template>
        <div class="content">
          <div class="main">
            <a-descriptions :column="3" size="small">
              <a-descriptions-item label="经营方式">{{$smartEnumPlugin.getDescByValue('VEHICLE_BUSINESS_MODE_ENUM', bracketDetail.businessMode)}}</a-descriptions-item>
              <a-descriptions-item label="负责人">{{ bracketDetail.managerName }}</a-descriptions-item>
              <a-descriptions-item label="创建人">{{ bracketDetail.createUserName }}</a-descriptions-item>
              <a-descriptions-item label="创建时间">{{ bracketDetail.createTime }}</a-descriptions-item>
            </a-descriptions>
          </div>
          <div class="extra">
            <div class="status">
              <div class="label">状态</div>
              <div class="value">{{ $smartEnumPlugin.getDescByValue('AUDIT_STATUS_ENUM', bracketDetail.auditStatus) }}
              </div>
            </div>
          </div>
        </div>
      </a-page-header>
    </a-card>
    <a-card class="smart-margin-top10 content-card" size="small">
      <a-tabs>
        <a-tab-pane key="1" tab="证件信息">
          <DetailCertificate :detail="bracketDetail" />
        </a-tab-pane>
        <a-tab-pane key="2" tab="维修信息">
          <RepairList :moduleType="REPAIR_MODULE_TYPE_ENUM.BRACKET.value" :moduleId="bracketId" />
        </a-tab-pane>
        <a-tab-pane key="3" tab="保险信息">
          <InsuranceList :moduleType="INSURANCE_MODULE_TYPE_ENUM.BRACKET.value" :moduleId="bracketId" />
        </a-tab-pane>
        <a-tab-pane key="4" tab="操作记录">
          <DataTracerList :business-id="bracketDetail.bracketId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.BRACKET.value" />
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <AuditModal ref="auditModal" @refresh="getDetail" />
  </div>
</template>
<script setup>
import { useSpinStore } from "/@/store/modules/system/spin";
import { bracketApi } from '/@/api/business/bracket/bracket-api';
import { onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useDriverDelete } from "/@/views/business/driver/hooks/use-driver-delete";
import { useUserStore } from "/@/store/modules/system/user";
import { AUDIT_STATUS_ENUM } from "/@/constants/common-const";
import { REPAIR_MODULE_TYPE_ENUM } from '/@/constants/business/repair-const';
import { INSURANCE_MODULE_TYPE_ENUM } from '/@/constants/business/insurance-const';
import AuditModal from '/@/components/audit-modal/index.vue'
import DetailCertificate from "./components/detail-certificate.vue";

import RepairList from '/@/views/business/repair/index.vue';
import InsuranceList from '/@/views/business/insurance/insurance-list.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { VEHICLE_BUSINESS_MODE_ENUM } from '/@/constants/business/vehicle-const';

let router = useRouter();
let route = useRoute();
let bracketId = ref();
onMounted(() => {
  bracketId.value = parseInt(route.query.bracketId);
  getDetail();
})
// --------------------- 挂车详情 ---------------------
let bracketDetail = reactive({});

async function getDetail() {
  useSpinStore().show();
  try {
    let result = await bracketApi.bracketDetail(bracketId.value);
    let detail = result.data;
    Object.assign(bracketDetail, detail);
    bracketDetail.vehicleList = (bracketDetail.driverVehicleList || []).map(e => e.vehicleNumber).join(',');
  } catch (error) {
    console.log("error", error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------- 删除 --------------------
let { confirmDelete } = useDriverDelete();

// ----------------- 关闭页面 -----------------
function onClose() {
  useUserStore().closePage(route, router);
}

// ----------------- 跳转 --------------------
function updateBracket() {
  router.push({ path: '/bracket/operate', query: { bracketId: bracketId.value } })
}

// ----------------- 审核 --------------------
let auditModal = ref();

function auditBracket() {
  let params = { bracketIdList: [bracketId.value] };
  auditModal.value.showModal(params, bracketApi.batchAudit);
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
