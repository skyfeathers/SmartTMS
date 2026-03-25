<template>
  <a-modal width="600px" :open="visible" title="导出">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <a-form-item class="smart-query-form-item" label="交易时间" name="startTime">
        <a-range-picker v-model:value="form.createTime" :ranges="defaultTimeRanges"
                        @change="changeCreateTime" style="width:80%"/>
      </a-form-item>
      <a-form-item label="性质" name="typeList">
        <SmartEnumSelect v-model:value="form.typeList" mode="multiple"
                         enum-name="OIL_CARD_BALANCE_RECORD_TYPE_ENUM" style="width:80%"/>
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button @click="exportExcel" type="primary">确认</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { oilApi } from '/@/api/business/card/oil-api';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

// ----------------------- 弹窗展示关闭 ------------------------
// 是否展示
const visible = ref(false);

function showModal () {
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

// ----------------------- 表单操作 ------------------------
//  组件
const formRef = ref();

const formDefault = {
  startTime: null,
  endTime: null,
  typeList: [],
  createTime: []
};
let form = reactive({ ...formDefault });
const rules = {
  startTime: [{ required: true, message: '请选择交易时间' }]
};

function changeCreateTime (dates, dateStrings) {
  form.startTime = dateStrings[0];
  form.endTime = dateStrings[1];
}

function exportExcel () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        let { startTime, endTime } = form;
        let typeList = form.typeList.join(',');
        oilApi.downloadExcel({ startTime, endTime, typeList });
        onClose();
        SmartLoading.hide();
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
