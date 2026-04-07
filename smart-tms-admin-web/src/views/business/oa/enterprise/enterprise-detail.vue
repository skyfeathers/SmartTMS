<template>
  <div class="detail-header">
    <a-page-header :title="detail.enterpriseName" :avatar="{ src: logo }">
      <div>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="企业简称">{{ detail.enterpriseShortName }}</a-descriptions-item>
          <a-descriptions-item label="统一社会信用代码">{{ detail.unifiedSocialCreditCode }}</a-descriptions-item>
          <a-descriptions-item label="道路运输经营许可证">{{ detail.networkFreightTransportCode }}</a-descriptions-item>
          <a-descriptions-item label="类型">{{ $smartEnumPlugin.getDescByValue('ENTERPRISE_TYPE_ENUM', detail.type) }}</a-descriptions-item>
          <a-descriptions-item label="联系人">{{ detail.contact }}</a-descriptions-item>
          <a-descriptions-item label="联系人电话">{{ detail.contactPhone }}</a-descriptions-item>
          <a-descriptions-item label="邮箱">{{ detail.email }}</a-descriptions-item>
          <a-descriptions-item label="所在城市">{{ area }}</a-descriptions-item>
          <a-descriptions-item label="详细地址">{{ detail.address }}</a-descriptions-item>
          <a-descriptions-item label="站点名称">{{ detail.websiteName }}</a-descriptions-item>
          <a-descriptions-item label="创建人">{{ detail.createUserName }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
          <a-descriptions-item label="创建人">{{ detail.createUserName }}</a-descriptions-item>
          <a-descriptions-item label="营业执照">
            <Upload :default-file-list="detail.businessLicense" :show-upload-btn="false"/>
          </a-descriptions-item>

        </a-descriptions>
      </div>
      <template #extra>
        <a-button @click="updateEnterprise" v-privilege="'enterprise:edit'">编辑</a-button>
        <a-button type="primary" @click="updateFlowConfig" v-privilege="'enterprise:flowConfig'">流程配置</a-button>
      </template>
    </a-page-header>
  </div>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
<!--      <a-tab-pane key="employee" tab="员工信息">-->
<!--        <EmployeeList :enterpriseId="enterpriseId"/>-->
<!--      </a-tab-pane>-->
      <a-tab-pane key="bank" tab="银行信息">
        <BankList :enterpriseId="enterpriseId"/>
      </a-tab-pane>
      <a-tab-pane key="invoice" tab="发票信息">
        <InvoiceList :enterpriseId="enterpriseId"/>
      </a-tab-pane>
      <a-tab-pane key="setting" tab="业务设置">
        <BusinessSetting :enterpriseId="enterpriseId"/>
      </a-tab-pane>
      <a-tab-pane key="dingConfig" tab="钉钉设置">
        <DingConfig :enterpriseId="enterpriseId"/>
      </a-tab-pane>
      <a-tab-pane key="domainConfig" tab="域名设置">
        <DomainConfig :enterpriseId="enterpriseId" :domain-name="detail.domainName" @reload="getDetail(enterpriseId)"/>
      </a-tab-pane>
      <a-tab-pane key="websiteConfig" tab="站点描述">
        <WebsiteConfig :enterpriseId="enterpriseId" :website-desc="detail.websiteDesc" @reload="getDetail(enterpriseId)"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <EnterpriseOperate ref="operateRef" @refresh="getDetail(enterpriseId)"/>
  <EnterpriseFlowConfig ref="flowConfigRef"/>
</template>

<script setup>

import {reactive, onMounted, ref, computed,} from 'vue';
import Upload from "/@/components/upload/index.vue";
import BankList from "./components/enterprise-bank-list.vue";
import InvoiceList from "./components/enterprise-invoice-list.vue";
import EmployeeList from "./components/enterprise-employee-list.vue";
import BusinessSetting from './components/enterprise-business-setting.vue';
import DingConfig from './components/enterprise-ding-config.vue';
import DomainConfig from './components/enterprise-domain-config.vue';
import WebsiteConfig from './components/enterprise-website-config.vue';
import EnterpriseOperate from './components/enterprise-operate-modal.vue';
import EnterpriseFlowConfig from './components/enterprise-flow-config.vue';

import { useUserStore } from '/@/store/modules/system/user';
import {useSpinStore} from '/@/store/modules/system/spin';
import {enterpriseApi} from '/@/api/business/oa/enterprise-api';
import {useRoute} from "vue-router";
import _ from 'lodash';

const route = useRoute();
let enterpriseId = ref();
onMounted(() => {
  if (route.query.enterpriseId) {
    enterpriseId.value = Number(route.query.enterpriseId);
  }else {
    enterpriseId.value = useUserStore().enterpriseId;
  }
  getDetail(enterpriseId.value);
});

// ----------------------- 以下是方法 ------------------------
// 详情
let detail = ref({});

async function getDetail(enterpriseId) {
  try {
    let result = await enterpriseApi.detail(enterpriseId);
    detail.value = result.data;
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

// 地区
const area = computed(() => {
  let area = "";
  if (!detail.value) {
    return area;
  }
  if (detail.value.provinceName) {
    area = area + detail.value.provinceName;
  }
  if (detail.value.cityName) {
    area = area + detail.value.cityName;
  }
  if (detail.value.districtName) {
    area = area + detail.value.districtName;
  }
  return area;
});

// logo
const logo = computed(() => {
  if (!detail.value) {
    return "";
  }
  if (!_.isEmpty(detail.value.enterpriseLogo)) {
    return detail.value.enterpriseLogo[0].fileUrl;
  }
});

const operateRef = ref();
function updateEnterprise() {
  operateRef.value.showModal(enterpriseId.value, false);
}

// 流程配置
const flowConfigRef = ref();
function updateFlowConfig() {
  flowConfigRef.value.showDrawer(enterpriseId.value);
}
</script>

<style lang='less' scoped>

.detail-header {
  background-color: #fff;
  padding: 10px
}

</style>
