<!--
 * @Description: file content
 * @Author: baijuhui
 * @Date: 2023-10-18 11:48:22
-->
<template>
  <a-modal :open="visible" :title="form.reviewId ? '编辑费用' : '新建费用'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="车辆" name="vehicleId">
            <VehicleSelect v-model:value="form.vehicleId" width="100%" :disabled="form.disabled"/>
          </a-form-item>
      <a-form-item label="审车时间" name="reviewDate">
        <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.reviewDate" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="审车费用" name="reviewAmount">
        <a-input-number v-model:value="form.reviewAmount" :min="0" :max="999999" :precision="2" placeholder="请输入审车费用" style="width: 100%" />
      </a-form-item>
      <a-form-item label="审车人" name="reviewPerson">
        <a-input v-model:value="form.reviewPerson" placeholder="请输入审车人" />
      </a-form-item>
      <a-form-item label="审车地点" name="reviewPlant">
        <a-input v-model:value="form.reviewPlant" placeholder="请输入审车地点" />
      </a-form-item>
      <a-form-item label="审车备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入审车备注" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { vehicleReviewApi } from '/@/api/business/vehicle-review/vehicle-review-api';
import dayjs from 'dayjs';
import VehicleSelect from '/@/components/vehicle-select/index.vue';

//  组件
const formRef = ref();
const rules = {
  vehicleId: [{ required: true, message: '请选择车辆' }],
  reviewDate: [{ required: true, message: '请输入审车时间' }],
  reviewAmount: [{ required: true, message: '请输入审车费用' }],
};

// 默认值
const formDefault = {
  vehicleId: undefined,
  reviewPerson: undefined,
  reviewAmount: undefined,
  reviewPlant: undefined,
  remark: undefined,
  reviewDate: undefined,
  reviewId: undefined
};
let form = reactive({ ...formDefault });

// 日期
// const reviewDateRef = ref(dayjs());
// 日期控件
// function dateChange(dates, dateStrings) {
//   form.reviewDate = dateStrings;
// }

// 是否展示
const visible = ref(false);
function showModal(rowData, vehicleId) {
  Object.assign(form, formDefault);
  if (rowData) {
    Object.assign(form, rowData);
    // reviewDateRef.value = dayjs(rowData.reviewDate, 'YYYY-MM-DD');
  } else {
    form.reviewDate = dayjs().format('YYYY-MM-DD');
  }
  if(vehicleId) {
    form.vehicleId = vehicleId;
    form.disabled = true;
  }
  visible.value = true;
}
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
  formRef.value.resetFields()
}

// emit
const emit = defineEmits(['reloadList']);
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        if (form.reviewId) {
          await vehicleReviewApi.update(form);
        } else {
          await vehicleReviewApi.create(form);
        }
        message.success(`${form.reviewId ? '修改' : '添加'}成功`);
        emit('reloadList');
        onClose();
      } catch (error) {
        console.log(error);
      } finally {
        useSpinStore().hide();
      }
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
