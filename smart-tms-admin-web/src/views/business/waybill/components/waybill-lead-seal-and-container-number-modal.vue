<!--
 * @Description:填写箱号/铅封号表单
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-28
-->
<template>
  <a-modal :open="visible" title="填写铅封号" ok-text="保存" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-row>
        <a-col :span="24">
          <a-form-item label="运单号">
            <a-input v-model:value="form.waybillNumber" disabled placeholder="" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="司机">
            <a-input v-model:value="form.driver" disabled placeholder="" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="车辆">
            <a-input v-model:value="form.vehicle" disabled placeholder="" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="箱号" name="containerNumber">
            <a-space v-for="(item, index) in form.containerNumberList" :key="index" style="display: flex; margin-bottom: 8px">
              <a-input v-model:value="form.containerNumberList[index]"
                       :rules="[ { required: true, message: '请填写箱号' },
                       { pattern: /^[A-Z]{4}[0-9]{7}$/, message: '请根据正确格式填写【前4位大写字母，后7位数字】'}]"
                       :key="index" placeholder="" style="width: 100%" />
              <a-button type="link" block @click="removeContainerNumberItem(index)">
                移除
              </a-button>
            </a-space>
            <a-button @click="addContainerNumber" type="link">新增箱号</a-button>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="填写铅封号" name="leadSealNumber">
            <a-input v-model:value="form.leadSealNumber" placeholder="" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SmartLoading } from '/@/components/smart-loading';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});
function addContainerNumber () {
  form.containerNumberList.push('');
}

function removeContainerNumberItem(index) {
  let containerNumberList = form.containerNumberList;
  containerNumberList.splice(index, 1);
  Object.assign(form, { containerNumberList });
}


// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(rowData) {
  form.waybillNumber = rowData.waybillNumber;
  form.waybillId = rowData.waybillId;
  form.containerNumber = rowData.containerNumber;
  form.leadSealNumber = rowData.leadSealNumber;
  if (form.containerNumber) {
    form.containerNumberList = form.containerNumber.split(",")
  }else {
    form.containerNumberList = [];
  }
  form.driver = `${rowData.driverName}(${rowData.driverTelephone})`;
  form.vehicle = `${rowData.vehicleNumber}`;
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const formDefault = {
  orderNo: null,
  driver: null,
  vehicle: null,
  containerNumber: undefined,
  containerNumberList: [],
  leadSealNumber: undefined,
  waybillId: undefined,
};
let form = reactive({ ...formDefault });
const rules = {
  // leadSealNumber: [{ required: true, message: '请填写铅封号' }],

};

// ----------------------- 提交表单 ------------------------
const formRef = ref();
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      SmartLoading.show();
      try {
        form.containerNumber = form.containerNumberList.join(",")
        await waybillApi.updateLeadSealAndContainerNumber(form);
        message.success('更新成功');
        emit('reloadList');
        onClose();
      } catch (error) {
        console.log(error);
      } finally {
        SmartLoading.hide();
      }
    })
    .catch((error) => {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    });
}
</script>
