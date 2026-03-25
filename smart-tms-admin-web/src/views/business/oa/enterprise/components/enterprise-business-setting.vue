<template>
  <a-card :bordered="false" :hoverable="true" size="small" title="合同提醒人设置">
    合同到期提醒人：
    <employee-select v-model:value="form[BUSINESS_SETTING_ENUM.CONTRACT_MESSAGE_RECEIVER.value]"
                     :disabledFlag="FLAG_NUMBER_ENUM.FALSE.value" :leaveFlag="false"
                     mode="multiple" placeholder="请选择合同到期提醒人" style="width:400px"/>
    <a-button class="smart-margin-left10" size="small"
              type="primary" @click="save(BUSINESS_SETTING_ENUM.CONTRACT_MESSAGE_RECEIVER.value)">保存
    </a-button>
  </a-card>

  <a-card :bordered="false" :hoverable="true" size="small" title="应收账款提醒人设置">
    应收账款提醒人：
    <employee-select v-model:value="form[BUSINESS_SETTING_ENUM.RECEIVE_ORDER_MESSAGE_RECEIVER.value]"
                     :disabledFlag="FLAG_NUMBER_ENUM.FALSE.value" :leaveFlag="false"
                     mode="multiple" placeholder="请选择应收账款提醒人" style="width:400px"/>
    <a-button class="smart-margin-left10" size="small"
              type="primary" @click="save(BUSINESS_SETTING_ENUM.RECEIVE_ORDER_MESSAGE_RECEIVER.value)">保存
    </a-button>
  </a-card>

</template>
<script setup>
import EmployeeSelect from '/@/components/employee-select/index.vue';

import { onMounted, reactive} from 'vue';
import { message } from 'ant-design-vue';
import { enterpriseSettingApi } from '/@/api/business/oa/enterprise-business-setting-api';
import { SmartLoading } from '/@/components/smart-loading';
import { BUSINESS_SETTING_ENUM } from '/@/constants/business/enterprise-const';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';

const props = defineProps({
  enterpriseId: {
    type: Number,
    default: null,
  }
});

const form = reactive({});

async function save (key) {
  try {
    SmartLoading.show();
    let settingValue = form[key] || [];
    settingValue = settingValue.join(',');
    let params = {
      enterpriseId: props.enterpriseId,
      settingKey: key,
      settingValue
    };
    await enterpriseSettingApi.saveSetting(params);
    message.info('保存成功');
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

async function queryByKey (key) {
  try {
    SmartLoading.show();
    let result = await enterpriseSettingApi.queryByKey(props.enterpriseId, key);
    let data = result.data;
    if (data) {
      let value = data.settingValue || '';
      form[key] = value.split(',').map(e => {
        return Number(e);
      });
    }
    console.log(data);
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

onMounted(() => {
  queryByKey(BUSINESS_SETTING_ENUM.CONTRACT_MESSAGE_RECEIVER.value);
  queryByKey(BUSINESS_SETTING_ENUM.RECEIVE_ORDER_MESSAGE_RECEIVER.value);
});


</script>
