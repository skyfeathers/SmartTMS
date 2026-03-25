<template>
  <a-card :bordered="false" size="small">
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-form-item name="content">
        <Wangeditor ref="contentRef" :height="300" :modelValue="form.configValue" />
      </a-form-item>
    </a-form>
    <a-space>
      <a-button type="primary" @click="onSubmit">保存</a-button>
    </a-space>
  </a-card>
</template>
<script setup>
import { reactive, ref, watch } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { configApi } from '/@/api/support/config/config-api';
import Wangeditor from '/@/components/smart-wangeditor/index.vue';
import _ from 'lodash';

let props = defineProps({
  configKey: {
    type: String,
  },
});

//  组件
const formRef = ref();

const formDefault = {
  configId: undefined,
  configKey: '',
  configName: '',
  configValue: '',
  remark: '',
};

let form = reactive({ ...formDefault });
const rules = {
};


watch(
  () => props.configKey,
  (e) => {
    if (e) {
      getContent(e);
    }
  },
  { immediate: true }
);

async function getContent(config){
  SmartLoading.show();
  try {
    let result = await configApi.queryByKey(config);
    Object.assign(form, result.data)
  } catch (err) {
  } finally {
    SmartLoading.hide();
  }
}


const contentRef = ref();
async function onSubmit () {
  try {
    await formRef.value.validateFields();
    let params = _.cloneDeep(form)
    params.configValue = contentRef.value.getHtml();
    save(params);
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

//更新
async function save (params) {
  SmartLoading.show();
  try {
    await configApi.updateConfig(params);
    message.success('操作成功');
  } catch (err) {
  } finally {
    SmartLoading.hide();
  }
}

</script>
<style lang="less" scoped>
  .operate-content {
    line-height: 20px;
    margin: 5px 0px;
  }
</style>
