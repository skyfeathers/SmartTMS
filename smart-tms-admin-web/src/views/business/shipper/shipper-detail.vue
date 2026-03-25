<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-08 16:05:46
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 17:53:52
-->
<template>
  <a-card size="small">
    <a-page-header>
      <template #title>
        {{ detail.consignor }}<SmartCopyIcon v-if="detail.consignor" :value="detail.consignor" />
      </template>
      <template #extra>
        <a-button @click="updateShipper" v-privilege="'shipper:edit'">编辑</a-button>
        <a-button @click="addTrack" type="primary" v-privilege="'shipper:addTrack'">添加跟进记录</a-button>
      </template>
      <basic-info :detail="detail"/>
    </a-page-header>
  </a-card>

  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="track" tab="跟进记录">
        <TrackManage ref="trackManageRef"/>
      </a-tab-pane>
      <a-tab-pane key="contract" tab="合同列表">
        <ContractList :signerType="CONTRACT_SIGNER_TYPE_ENUM.SHIPPER.value" :signerBelongId="detail.shipperId" :showOperate="true"/>
      </a-tab-pane>
      <a-tab-pane key="receiveOrder" tab="应收款">
        <ReceiveOrderManage/>
      </a-tab-pane>
      <a-tab-pane key="contact" tab="联系人信息">
        <ContactManage :shipperId="detail.shipperId" :actionFlag="false"/>
      </a-tab-pane>
      <a-tab-pane key="invoice" tab="开票信息">
        <InvoiceManage :shipperId="detail.shipperId" :actionFlag="false"/>
      </a-tab-pane>
      <a-tab-pane key="paymentType" tab="收付款信息">
        <PaymentTypeManage :shipperId="detail.shipperId" :actionFlag="false"/>
      </a-tab-pane>
      <a-tab-pane key="mailAddress" tab="邮寄地址信息">
        <MailAddressManage :shipperId="detail.shipperId" :actionFlag="false"/>
      </a-tab-pane>
      <a-tab-pane key="dataTracker" tab="操作记录" >
        <DataTracerList
            :business-id="detail.shipperId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.SHIPPER.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <add-track-modal ref="trackModalRef" @reloadList="refresh"/>
</template>
<script setup>
import {ref, onMounted, reactive} from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { shipperApi } from '/@/api/business/shipper/shipper-api';

import BasicInfo from './shipper-detail/basic-info.vue';
import TrackManage from './components/track-manage.vue';
import ContactManage from './components/contact-manage.vue';
import InvoiceManage from './components/invoice-manage.vue';
import PaymentTypeManage from './components/payment-type-manage.vue';
import MailAddressManage from './components/mail-address-manage.vue';
import ContractList from '/@/views/business/contract/contract-list.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import AddTrackModal from './components/add-track-modal.vue';
import ReceiveOrderManage from './components/receive-order-manage.vue';

import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import {CONTRACT_SIGNER_TYPE_ENUM} from "/@/constants/business/contract-const";
import { useRoute, useRouter } from 'vue-router';

onMounted(() => {
  getDetail();
});

const route = useRoute();
let detail = reactive({});
async function getDetail () {
  if (!route.query.shipperId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await shipperApi.getShipper(route.query.shipperId);
    Object.assign(detail, data);
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

const trackManageRef = ref();
function refresh () {
  if(!trackManageRef.value){
    return;
  }
  trackManageRef.value.ajaxQuery();
}

// ----------- 添加跟进记录 -----------
const trackModalRef = ref();

function addTrack() {
  trackModalRef.value.showModal(detail.shipperId);
}

// ----------- 更新 -----------
const router = useRouter();
function updateShipper() {
  router.push({
    path: '/shipper/operate',
    query: {
      shipperId: detail.shipperId
    }
  });
}



</script>
