<!--
 * @Description:付款单详情
 * @Author: zhuoda
 * @Date: 2022-07-11
 * @LastEditTime: 2022-08-06
 * @LastEditors: zhuoda
-->
<template>
  <a-card size="small">
    <a-page-header
        :title="`付款单号：${payOrderDetail.payOrderNumber} (${$smartEnumPlugin.getDescByValue('PAY_ORDER_STATUS_ENUM', payOrderDetail.payOrderStatus)})`"
    >
      <template #extra>
        <template v-if="payOrderDetail.payOrderType == PAY_ORDER_TYPE_ENUM.CASH.value">
          <a-button v-privilege="'payOrder:pay'" :disabled="disablePayFlag" size="small" type="primary"
                    @click="showPayModal">
            支付
          </a-button>
        </template>
        <template v-if="payOrderDetail.payOrderType == PAY_ORDER_TYPE_ENUM.OIL_CARD.value">
          <a-button v-privilege="'oilCardRecharge:recharge'" :disabled="disablePayFlag" size="small" type="primary"
                    @click="showRechargeModal">
            油卡充值
          </a-button>
        </template>

        <a-button v-privilege="'payOrder:verification;oilCardRecharge:verification'" :disabled="disableVerificationFlag" size="small"
                  type="primary" @click="showVerificationModal">
          核销
        </a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="司机">{{ payOrderDetail.driverName }}</a-descriptions-item>
            <a-descriptions-item label="车辆">{{ payOrderDetail.vehicleNumber }}</a-descriptions-item>
            <a-descriptions-item label="车队">{{ payOrderDetail.fleetName }}</a-descriptions-item>
            <a-descriptions-item label="联系电话">{{ payOrderDetail.settleObjectPhone }}</a-descriptions-item>

            <a-descriptions-item label="创建人">{{ payOrderDetail.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ payOrderDetail.createTime }}</a-descriptions-item>
          </a-descriptions>

          <a-descriptions :column="4" size="small">
            <a-descriptions-item v-if="payOrderDetail.payOrderType == PAY_ORDER_TYPE_ENUM.OIL_CARD.value" label="油卡卡号">{{ payOrderDetail.oilCardNo }}</a-descriptions-item>
            <a-descriptions-item label="应付金额">{{ payOrderDetail.payableTotalAmount }}</a-descriptions-item>

            <a-descriptions-item label="已付金额">{{ payOrderDetail.paidTotalAmount }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ payOrderDetail.remark }}</a-descriptions-item>

          </a-descriptions>
          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="支付时间">{{ payOrderDetail.payTime }}</a-descriptions-item>
            <a-descriptions-item label="支付流水号">{{ payOrderDetail.sequenceCode }}</a-descriptions-item>
            <a-descriptions-item label="付款凭证">
              <file-preview :fileList="payOrderDetail.attachment" v-if="payOrderDetail && payOrderDetail.attachment" />
            </a-descriptions-item>
          </a-descriptions>

          <div v-if="payOrderDetail.receiveVO && payOrderDetail.payOrderType == PAY_ORDER_TYPE_ENUM.CASH.value">
            <hr class="smart-hr"/>
            <a-typography-title :level="5" class="smart-margin-top10">收款账户</a-typography-title>
            <a-descriptions :column="2" size="small">
              <a-descriptions-item label="银行名称">{{ payOrderDetail.receiveVO.receiveBankName }}</a-descriptions-item>
              <a-descriptions-item label="支行名称">{{payOrderDetail.receiveVO.receiveBankBranchName}}
              </a-descriptions-item>
              <a-descriptions-item label="开户名">{{ payOrderDetail.receiveVO.receiveAccountName }}</a-descriptions-item>
              <a-descriptions-item label="银行账号">{{ payOrderDetail.receiveVO.receiveBankAccount }}</a-descriptions-item>
            </a-descriptions>
          </div>
        </div>

        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{
                $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', payOrderDetail.auditStatus)
              }}
            </div>
          </div>
        </div>
      </div>
    </a-page-header>
  </a-card>

  <a-card class="smart-margin-top10 content-card">
    <a-tabs>
      <a-tab-pane key="1" tab="付款信息" v-if="payOrderDetail.paymentVO && payOrderDetail.payOrderType == PAY_ORDER_TYPE_ENUM.CASH.value">
        <PayOrderDetailPayment :paymentDetail="payOrderDetail.paymentVO"/>
      </a-tab-pane>
      <a-tab-pane key="2" tab="核销信息" v-if="payOrderDetail.verificationVO">
        <PayOrderDetailVerification :verificationDetail="payOrderDetail.verificationVO"/>
      </a-tab-pane>
      <a-tab-pane key="3" tab="费用明细">
        <PayOrderDetailItemList :costList="payOrderDetail.costList" :payOrderDetail="payOrderDetail"/>
      </a-tab-pane>
      <a-tab-pane key="4" tab="操作记录">
        <DataTracerList
            :business-id="payOrderDetail.payOrderId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.PAY_ORDER.value"/>
      </a-tab-pane>
    </a-tabs>

    <PayOrderPaymentModal ref="paymentModalRef" @reloadList="getDetail"/>
    <PayOrderVerificationModal ref="verificationRef" @reloadList="getDetail"/>
    <!-- 充值 -->
    <RechargeModal ref="rechargeRef" @reloadList="getDetail"/>
  </a-card>

  <FlowTaskList :flowType="payOrderDetail.payOrderType == PAY_ORDER_TYPE_ENUM.CASH.value ? FLOW_TYPE_ENUM.PAY_AUDIT.value : FLOW_TYPE_ENUM.OIL_CARD_AUDIT.value"
                :instance-id="payOrderDetail.flowInstanceId"
                :processing-flag="FLOW_AUDIT_STATUS_ENUM.WAIT.value == payOrderDetail.auditStatus"
                @refresh="getDetail"
  />
</template>
<script setup>
import { message } from 'ant-design-vue';
import { computed, onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import PayOrderPaymentModal from './components/pay-order-payment-modal.vue';
import PayOrderVerificationModal from './components/pay-order-verification-modal.vue';
import PayOrderDetailPayment from './components/pay-order-detail-payment.vue';
import PayOrderDetailVerification from './components/pay-order-detail-verification.vue';
import PayOrderDetailItemList from './components/pay-order-detail-item-list.vue';
import RechargeModal from './components/oil-card-recharge-recharge-modal.vue';
import FlowTaskList from '/@/components/flow-task-list/index.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import { payOrderApi } from '/@/api/business/pay/pay-order-api';
import { SmartLoading } from '/@/components/smart-loading';
import { PAY_ORDER_STATUS_ENUM,PAY_ORDER_TYPE_ENUM } from '/@/constants/business/pay-order-const';
import { FLOW_TYPE_ENUM,FLOW_AUDIT_STATUS_ENUM } from '/@/constants/business/flow-const';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import FilePreview from '/@/components/file-preview/index.vue'
// --------------------- 详情数据 ---------------------
let router = useRouter();
let route = useRoute();
let payOrderId = ref();
payOrderId.value = Number(route.query.payOrderId);
onMounted(() => {
  getDetail();
});

let payOrderDetail = reactive({});

async function getDetail () {
  SmartLoading.show();
  try {
    let responseModel = await payOrderApi.getDetail(payOrderId.value);
    let detail = responseModel.data;
    Object.assign(payOrderDetail, detail);
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}

const disablePayFlag = computed(() => {
  if (!payOrderDetail) {
    return true;
  }
  if (FLOW_AUDIT_STATUS_ENUM.PASS.value != payOrderDetail.auditStatus) {
    return true;
  }
  if (PAY_ORDER_STATUS_ENUM.PAID.value == payOrderDetail.payOrderStatus) {
    return true;
  }
  return false;
});

const disableVerificationFlag = computed(() => {
  if (!payOrderDetail) {
    return true;
  }
  if (FLOW_AUDIT_STATUS_ENUM.PASS.value != payOrderDetail.auditStatus) {
    return true;
  }
  if (PAY_ORDER_STATUS_ENUM.PAID.value != payOrderDetail.payOrderStatus) {
    return true;
  }
  if (payOrderDetail.verificationFlag) {
    return true;
  }
  return false;
});

// ----------- 支付-----------
const paymentModalRef = ref();

function showPayModal () {
  paymentModalRef.value.showModal([payOrderDetail]);
}

// ----------- 核销-----------
const verificationRef = ref();

function showVerificationModal () {
  if (payOrderDetail.payOrderStatus !== PAY_ORDER_STATUS_ENUM.PAID.value) {
    message.error('此单据无法核销');
    return;
  }
  verificationRef.value.showModal([payOrderDetail]);
}

// ------- 充值
const rechargeRef = ref();

function showRechargeModal () {
  rechargeRef.value.showModal(payOrderDetail.payOrderId);
}
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
