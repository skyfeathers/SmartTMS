<!-- 钉钉配置 -->
<template>
  <a-card :bordered="false" :hoverable="true" size="small">
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-form-item label="CorpId" name="corpId">
        <a-input v-model:value="form.corpId" placeholder="请输入钉钉CorpId" style="width:300px"/>
      </a-form-item>
      <a-form-item label="AppKey" name="appKey">
        <a-input v-model:value="form.appKey" placeholder="请输入AppKey" style="width:300px"/>
      </a-form-item>
      <a-form-item label="AppSecret" name="appSecret">
        <a-input v-model:value="form.appSecret" placeholder="请输入AppSecret" style="width:300px"/>
      </a-form-item>
      <a-form-item label="加密 aes_key" name="aesKey">
        <a-input v-model:value="form.aesKey" placeholder="请输入加密 aes_key" style="width:500px"/>
      </a-form-item>
      <a-form-item label="签名 token" name="aesToken">
        <a-input v-model:value="form.aesToken" placeholder="请输入签名 aesToken" style="width:300px"/>
      </a-form-item>
      <a-form-item label="同步指定部门下" name="departmentId">
        <DepartmentTreeSelect ref="departmentTreeSelect" width="100%" :init="false" placeholder="请选择同步部门"
                              v-model:value="form.departmentId" style="width:200px"/>
      </a-form-item>
    </a-form>
    <a-button class="smart-margin-left10" size="small"
              type="primary" @click="saveConfig()">保存
    </a-button>
  </a-card>
</template>
<script setup>
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import { reactive,onMounted } from 'vue';
import { enterpriseApi } from '/@/api/business/oa/enterprise-api';
import { SmartLoading } from '/@/components/smart-loading';
import { BUSINESS_SETTING_ENUM } from '/@/constants/business/enterprise-const';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';

const props = defineProps({
  enterpriseId: {
    type: Number,
    default: null,
  }
});

const rules = {
  corpId: [{ required: true, message: '请输入钉钉CorpId' }],
  appKey: [{ required: true, message: '请输入AppKey' }],
  appSecret: [{ required: true, message: '请输入AppSecret' }],
  aesKey: [{ required: true, message: '请输入加密 aes_key' }],
  aesToken: [{ required: true, message: '请输入签名 aesToken' }],
  departmentId: [{ required: true, message: '请选择同步部门' }],
}
const formDefault = {
  corpId: null,
  appKey: null,
  appSecret: null,
  aesKey: null,
  aesToken: null,
  departmentId: null
};

const form = reactive({ ...formDefault });

async function getConfig () {
  try {
    SmartLoading.show();
    let result = await enterpriseApi.getDingDingConfig(props.enterpriseId);
    Object.assign(form, result.data);
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

async function saveConfig(){
  try {
    SmartLoading.show();
    let params = Object.assign(form, { enterpriseId: props.enterpriseId });
    await enterpriseApi.saveDingDingConfig(params);
    getConfig();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

onMounted(() => {
  getConfig()
});


</script>
