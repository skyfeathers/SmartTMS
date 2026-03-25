<!--
  * 固定资产-报废
  *
  * @Author:    卓大
  * @Date:      2023-03-23 15:01:51
  * @Copyright  1024创新实验室 （ https://1024lab.net ）
-->
<template>
  <a-modal
    :title="form.repairId ? '编辑' : '报废登记'"
    width="1000px"
    :open="visibleFlag"
    @cancel="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-row>
        <a-col :span="12">
          <a-form-item label="报废时间" name="scrapTime">
            <a-date-picker  valueFormat="YYYY-MM-DD HH:mm:ss" format="YYYY-MM-DD HH:mm:ss"  show-time v-model:value="form.scrapTime" style="width: 200px" placeholder="报废日期" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="报废说明" name="scrapExplain">
            <a-textarea style="width: 100%" v-model:value="form.scrapExplain" placeholder="报废说明" />
          </a-form-item>
        </a-col>
      </a-row>

        <!-- 资产列表 -->
        <AssetTableSelect ref="assetTableSelectRef" :operateFlag="true"/>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { computed, nextTick, provide, reactive, ref } from 'vue';
import { scrapApi } from '/@/api/fixed-asset/asset/scrap-api';
import AssetTableSelect from '/@/components/fixed-asset/asset-table-select/index.vue';
import { SmartLoading } from '/@/components/smart-loading';
import { smartSentry } from '/@/lib/smart-sentry';
import { ASSET_STATUS_ENUM } from '/@/constants/fixed-asset/asset-const';

import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show(rowData) {
  provide('setCustomQueryForm', {
    statusList: [
      ASSET_STATUS_ENUM.UNUSED.value
    ]
  });
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  visibleFlag.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

function onClose() {
  Object.assign(form, formDefault);
  visibleFlag.value = false;
}

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();
// 选择固定资产ref
const assetTableSelectRef = ref();

const formDefault = {
  scrapId: undefined,
  scrapTime: undefined, //报废时间
  scrapExplain: undefined, //说明
  assetIdList:[],//资产id列表
};

let form = reactive({ ...formDefault });

const rules = {
  scrapExplain: [{ required: true, message: '报废说明 必填' }],
  scrapTime: [{ required: true, message: '报废日期 必填' }],
};


// 点击确定，验证表单
async function onSubmit() {
  try {
    await formRef.value.validateFields();

    let assetList = assetTableSelectRef.value.tableData;
    if (_.isEmpty(assetList)) {
      message.error('请选择需要报废的固定资产');
      return;
    }
    form.assetIdList = assetList.map( e => e.assetId);
    save();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

// 新建、编辑API
async function save() {
  SmartLoading.show();
  try {
    if (form.scrapId) {
      await scrapApi.update(form);
    } else {
      await scrapApi.add(form);
    }
    message.success('操作成功');
    emits('reloadList');
    onClose();
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    SmartLoading.hide();
  }
}

let customQueryForm = computed(() => {
  return {};
});
provide('setCustomQueryForm', customQueryForm);

// 由于需要验证调出位置，所以增加自定义验证方法
function validateParam (param) {
  if (!param) {
    return false;
  }
  return true;
}

provide('setValidateParam', validateParam);


defineExpose({
  show,
});
</script>
