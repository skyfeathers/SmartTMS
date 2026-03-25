<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-11
 * @LastEditTime: 2022-08-01
 * @LastEditors: zhuoda
-->
<template>
  <a-card size="small">
    <a-page-header>
      <template #title>
          {{ `运单号：${waybillDetail.waybillNumber} (${$smartEnumPlugin.getDescByValue('WAYBILL_STATUS_ENUM', waybillDetail.waybillStatus)})` }}
          <SmartCopyIcon v-if="waybillDetail.waybillNumber" :value="waybillDetail.waybillNumber" />
        </template>
      <template #extra>
        <a-button @click="createContract" size="small" type="primary" v-privilege="'waybill:uploadDriverContract'">上传司机合同</a-button>
        <a-button @click="showCostModal" v-privilege="'waybill:updateCost'" size="small" type="primary">费用</a-button>
        <a-button danger size="small" @click="cancel" v-privilege="'waybill:cancel'">作废</a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="4" size="small">

            <a-descriptions-item label="订单号">
              <a href="javascript:void(0)" @click="gotoOrder(waybillDetail.orderId)">{{ waybillDetail.orderNo }}</a>
              <SmartCopyIcon v-if="waybillDetail.orderNo" :value="waybillDetail.orderNo" />
            </a-descriptions-item >

            <a-descriptions-item label="箱号">
              <span style="color: red; font-weight: bolder">{{ waybillDetail.containerNumber }}</span>
              <a-button v-privilege="'waybill:updateLeadSealAndContainerNumber'" size="small" type="link" @click="showUpdateLeadSealAndContainerNumber" >{{ waybillDetail.containerNumber ? '填写' : '修改' }}箱号</a-button>
              <SmartCopyIcon v-if="waybillDetail.containerNumber" :value="waybillDetail.containerNumber" />
            </a-descriptions-item>
            <a-descriptions-item label="铅封号">
              <span style="color: red; font-weight: bolder">{{ waybillDetail.leadSealNumber }}</span>
              <a-button v-privilege="'waybill:updateLeadSealAndContainerNumber'" size="small" type="link" @click="showUpdateLeadSealAndContainerNumber" >{{ waybillDetail.leadSealNumber ? '填写' : '修改' }}铅封号</a-button>
              <SmartCopyIcon v-if="waybillDetail.leadSealNumber" :value="waybillDetail.leadSealNumber" />
            </a-descriptions-item>
          </a-descriptions>
          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="客户">{{ waybillDetail.shortName }}</a-descriptions-item>
            <a-descriptions-item label="销售">{{ waybillDetail.orderManagerName }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ waybillDetail.createUserName }}</a-descriptions-item>

            <a-descriptions-item label="运输方式">{{ $smartEnumPlugin.getDescByValue('WAYBILL_TRANSPORT_MODE_ENUM', waybillDetail.transportMode) }}</a-descriptions-item>
          </a-descriptions>

          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="运输路线">{{ waybillDetail.routeName }}</a-descriptions-item>
            <a-descriptions-item label="车队">
              <a href="javascript:void(0)" @click="gotoFleet(waybillDetail.fleetId)">{{ waybillDetail.fleetName }}</a>
            </a-descriptions-item>
            <a-descriptions-item label="司机">
              <a href="javascript:void(0)" @click="gotoDriver(waybillDetail.driverId)">{{ waybillDetail.driverName }}/{{ waybillDetail.driverTelephone }}</a>
              <SmartCopyIcon v-if="waybillDetail.driverName" :value="`${waybillDetail.driverName} ${waybillDetail.driverTelephone}`" />
            </a-descriptions-item>


            <a-descriptions-item label="车辆">
              <a href="javascript:void(0)" @click="gotoVehicle(waybillDetail.vehicleId)">{{ waybillDetail.vehicleNumber }}</a>
              <SmartCopyIcon v-if="waybillDetail.vehicleNumber" :value="waybillDetail.vehicleNumber" />
            </a-descriptions-item>

          </a-descriptions>


          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="应付现金">{{ waybillDetail.payableCashAmount }}</a-descriptions-item>
            <a-descriptions-item label="应付油卡">{{ waybillDetail.payableOilCardAmount }}</a-descriptions-item>
            <a-descriptions-item label="司机工资">{{ waybillDetail.salaryAmount }}</a-descriptions-item>
            <a-descriptions-item label="在途费用">{{ waybillDetail.carCostAmount }}</a-descriptions-item>
           
          </a-descriptions>

          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="合计应付"><span style="color: red; font-weight: bolder">{{ waybillDetail.payableAmount  }}（现金+油卡）</span></a-descriptions-item>
            <a-descriptions-item label="合计应收"><span style="color: red; font-weight: bolder">{{ waybillDetail.receiveAmount  }}</span></a-descriptions-item>
             <a-descriptions-item label="税金"><span style="color: red; font-weight: bolder">{{ waybillDetail.taxAmount }}（税点{{ waybillDetail.taxPoint }}）</span></a-descriptions-item>
            <a-descriptions-item label="利润"><span style="color: red; font-weight: bolder">{{ waybillDetail.profitAmount }}（应收-应付-税金-司机工资-在途费用）</span></a-descriptions-item>
          </a-descriptions>


          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="期望装货时间">{{waybillDetail.loadTime}}</a-descriptions-item>
            <a-descriptions-item label="期望卸货时间">{{waybillDetail.unloadTime}}</a-descriptions-item>

         </a-descriptions>

         <a-descriptions :column="4" size="small">
            <a-descriptions-item label="装货磅重">{{waybillDetail.loadPoundListQuantity}}</a-descriptions-item>
            <a-descriptions-item label="装货磅单">
              <a-image :width="100" v-if="waybillDetail.loadPoundListAttachment && waybillDetail.loadPoundListAttachment.length > 0" :src="waybillDetail.loadPoundListAttachment[0].fileUrl" />
            </a-descriptions-item>
            <a-descriptions-item label="装货时间">{{ waybillDetail.deliverGoodsTime }}</a-descriptions-item>

         </a-descriptions>

         <a-descriptions :column="4" size="small">
            <a-descriptions-item label="卸货磅重">{{waybillDetail.unloadPoundListQuantity}}</a-descriptions-item>
            <a-descriptions-item label="卸货磅单">
              <a-image :width="100" v-if="waybillDetail.unloadPoundListAttachment && waybillDetail.unloadPoundListAttachment.length > 0" :src="waybillDetail.unloadPoundListAttachment[0].fileUrl" />
            </a-descriptions-item>
            <a-descriptions-item label="卸货时间">{{ waybillDetail.receiveGoodsTime }}</a-descriptions-item>

         </a-descriptions>

          <a-descriptions :column="4" size="small">

            <a-descriptions-item label="业务时间">{{ (waybillDetail.businessDate || '').substring(0,7) }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ waybillDetail.createTime }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ waybillDetail.remark }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM',waybillDetail.auditStatus) }}</div>
          </div>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10 content-card">
    <a-tabs>
      <a-tab-pane key="path" tab="路线/货物">
        <WaybillRouteGoods :pathList="waybillDetail.pathList" :goodsList="waybillDetail.goodsList"/>
      </a-tab-pane>
      <a-tab-pane key="payCost" tab="应付费用信息" >
        <WaybillDetailCost :costList="waybillDetail.costList"/>
      </a-tab-pane>
      <a-tab-pane key="receiveCost" tab="应收费用信息" >
        <WaybillDetailReceiveCost :costList="waybillDetail.receiveCostList"/>
      </a-tab-pane>
      <a-tab-pane key="tabulation" tab="在途费用" >
        <WaybillRouteCostTable :waybillId="waybillDetail.waybillId" :vehicleId="waybillDetail.vehicleId" />
      </a-tab-pane>
      <a-tab-pane key="payOrder" tab="结算信息" >
        <WaybillPayOrderList :waybillId="waybillDetail.waybillId"/>
      </a-tab-pane>
      <a-tab-pane key="voucher" tab="凭证/磅单">
        <WaybillVoucherList :waybillId="waybillId" @refresh="getDetail" />
      </a-tab-pane>
      <a-tab-pane key="driverContract" tab="司机合同">
        <ContractList :waybillId="waybillId" />
      </a-tab-pane>

      <a-tab-pane key="exception" tab="异常信息">
        <WaybillDetailException :exceptionList="waybillDetail.exceptionList"/>
      </a-tab-pane>

      <a-tab-pane key="dataTracer" tab="操作记录" >
        <DataTracerList
            :business-id="waybillDetail.waybillId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.WAYBILL.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <FlowTaskList
    :instance-id="waybillDetail.flowInstanceId"
    :processing-flag="FLOW_AUDIT_STATUS_ENUM.WAIT.value == waybillDetail.auditStatus"
    :flowType="FLOW_TYPE_ENUM.WAY_BILL_AUDIT.value"
    @refresh="getDetail"
  />

  <FlowTaskList v-for="item in waybillDetail.oilCardRechargeApplyList" :key="item.rechargeApplyId"
      :instance-id="item.flowInstanceId"
      :processing-flag="FLOW_AUDIT_STATUS_ENUM.WAIT.value == item.auditStatus"
      :flowType="FLOW_TYPE_ENUM.OIL_CARD_AUDIT.value"
      @refresh="getDetail"
  >
    <template #content>
      <a-descriptions :column="4" size="small">
        <a-descriptions-item label="司机">{{ item.driverName }}</a-descriptions-item>
        <a-descriptions-item label="车辆">{{ item.vehicleNumber }}</a-descriptions-item>
        <a-descriptions-item label="油卡">{{ item.oilCardNo }}</a-descriptions-item>
        <a-descriptions-item label="充值金额">{{ item.amount }}</a-descriptions-item>
        <a-descriptions-item label="备注">{{ item.remark }}</a-descriptions-item>
      </a-descriptions>
    </template>
  </FlowTaskList>
  <WaybillLeadSealNumberModal ref="waybillContainerLeadSealModalRef" @reloadList="getDetail" />
  <WaybillCostModal ref="waybillCostModal" @reloadList="getDetail" />
  <ContractCreateModal ref="contractCreateModal" @refresh="getDetail" />
</template>
<script setup>
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { onMounted, provide, reactive, ref ,nextTick} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import { SmartLoading } from '/@/components/smart-loading';
import { WAYBILL_STATUS_ENUM } from '/@/constants/business/waybill-const';
import { FLOW_TYPE_ENUM,FLOW_AUDIT_STATUS_ENUM } from '/@/constants/business/flow-const';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';

import DataTracerList from '/@/components/data-tracer/index.vue';
import FlowTaskList from '/@/components/flow-task-list/index.vue';
import ContractList from '/@/views/business/contract/contract-list.vue';
import WaybillVoucherList from './components/waybill-voucher-list.vue';
import WaybillRouteGoods from './components/waybill-route-goods.vue';
import WaybillLeadSealNumberModal from './components/waybill-lead-seal-and-container-number-modal.vue';
import WaybillCostModal from './components/waybill-cost-modal.vue';
import WaybillDetailCost from './components/waybill-detail-cost.vue';
import WaybillDetailReceiveCost from './components/waybill-detail-receive-cost.vue';
import WaybillPayOrderList from './components/waybill-pay-order-list.vue';
import ContractCreateModal from '/@/views/business/contract/contract-create-modal.vue';
import WaybillDetailException from './components/waybill-detail-exception.vue';
import WaybillSplitTransportModal from './components/waybill-split-transport-modal.vue';
import { useWaybillCancel } from './hooks/use-waybill-cancel';
import WaybillRouteCostTable from './components/waybill-route-cost-table.vue';

// --------------------- 详情数据 ---------------------
let router = useRouter();
let route = useRoute();
let waybillId = ref();
waybillId.value = Number(route.query.waybillId);
onMounted(() => {
  getDetail();
});

let waybillDetail = reactive({});
const vehicleId = ref();
async function getDetail() {
  SmartLoading.show();
  try {
    let responseModel = await waybillApi.getDetail(waybillId.value);
    let detail = responseModel.data;
    vehicleId.value = detail.vehicleId;
    Object.assign(waybillDetail, detail);
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}
provide('vehicleId', vehicleId);

// ----------- 结算 -----------
const waybillCostModal = ref();
function showCostModal() {
  waybillCostModal.value.showModal(route.query.waybillId);
}
// ----------- 跳转-----------
function gotoDriver(driverId) {
  router.push({ path: '/driver/detail', query: { driverId } });
}
function gotoVehicle(vehicleId) {
  router.push({ path: '/vehicle/vehicle-detail', query: { vehicleId } });
}
function gotoFleet(fleetId) {
  router.push({ path: '/fleet/detail', query: { fleetId } });
}
function gotoOrder(orderId) {
  router.push({ path: '/order/detail', query: { orderId } });
}
// ----------- 填写铅封号-----------
const waybillContainerLeadSealModalRef = ref();
function showUpdateLeadSealAndContainerNumber() {
  waybillContainerLeadSealModalRef.value.showModal(waybillDetail);
}
// ----------- 作废-----------
function cancel() {
  useWaybillCancel().confirmCancel(waybillDetail.waybillNumber, waybillDetail.waybillId, getDetail);
}
// ----------------- 合同 --------------------
let contractCreateModal = ref();
function createContract() {
  contractCreateModal.value.showModal(waybillDetail.waybillId, waybillDetail.waybillNumber);
}

// ----------------- 关闭页面 -----------------
function onClose() {
  useUserStore().closePage(route, router);
}


// ----------------- 审核 --------------------

// ----------------- provide --------------------

</script>
<style lang="less" scoped>
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
