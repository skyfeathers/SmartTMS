<template>
  <a-modal :open="visible" title="修改" ok-text="保存" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-form-item label="结算方式">
        <smart-enum-select v-model:value="form.settleMode" enumName="SETTLE_MODE_ENUM" placeholder="请选择结算方式" />
      </a-form-item>
      <a-form-item label="结算类型">
        <smart-enum-select v-model:value="form.settleType" enumName="SETTLE_TYPE_ENUM" placeholder="请选择结算类型" />
      </a-form-item>
      <a-form-item label="运输方式">
        <smart-enum-select v-model:value="form.transportMode" enumName="WAYBILL_TRANSPORT_MODE_ENUM" placeholder="请选择运输方式" />
      </a-form-item>
      <a-form-item v-if="fleetFlag" label="车队" name="fleetId">
        <FleetSelect width="200px" v-model:value="form.fleetId" placeholder="请选择车队" />
      </a-form-item>
      <a-form-item label="司机" name="driverId">
        <DriverSelect width="200px" v-model:value="form.driverId" placeholder="请选择司机" />
      </a-form-item>
      <a-form-item label="车辆" name="vehicleId">
        <VehicleSelect width="200px" v-model:value="form.vehicleId" placeholder="请选择车辆" />
      </a-form-item>
      <a-form-item label="业务日期" name="businessDate">
        <a-date-picker v-model:value="form.businessDate" format="YYYY-MM" picker="month" placeholder="业务日期"
                       style="width: 200px" valueFormat="YYYY-MM-01"/>
      </a-form-item>
      <a-form-item label="装货时间" name="loadTime">
        <a-date-picker v-model:value="form.loadTime" :show-time="{ format: 'HH:mm' }"
                       format="YYYY-MM-DD HH:mm:00"
                       placeholder="请选择装货时间" valueFormat="YYYY-MM-DD HH:mm:00"/>
      </a-form-item>
      <a-form-item label="卸货时间" name="unloadTime">
        <a-date-picker v-model:value="form.unloadTime" :show-time="{ format: 'HH:mm' }"
                       format="YYYY-MM-DD HH:mm:00"
                       placeholder="请选择卸货时间" valueFormat="YYYY-MM-DD HH:mm:00"/>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea :rows="2" v-model:value="form.remark" style="width: 200px" type="textarea" placeholder="请输入备注"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import {ref, reactive, nextTick, computed} from 'vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import DriverSelect from '/@/components/driver-select/index.vue';
import FleetSelect from '/@/components/fleet-select/index.vue';
import { SETTLE_TYPE_ENUM } from '/@/constants/business/waybill-const';
import { message } from 'ant-design-vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SmartLoading } from '/@/components/smart-loading';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

// -------------表单字段------------------
const fleetFlag = computed(() => {
  return form.settleType == SETTLE_TYPE_ENUM.FLEET.value;
});

const formDefault = {
  waybillId: null,
  remark: null,
  settleMode: null,
  settleType: null,
  vehicleId: null,
  driverId: null,
  fleetId: null,
  transportMode: null,
  businessDate: null,
  loadTime: null,
  unloadTime: null,
};

const form = reactive({ ...formDefault });
const formRef = ref();

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(rowData) {
  Object.assign(form, rowData);
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const rules = {
  settleMode: [{ required: true, message: '请选择结算方式' }],
  settleType: [{ required: true, message: '请选择结算类型' }],
  fleetId: [{ required: true, message: '请选择车队' }],
  driverId: [{ required: true, message: '请选择司机' }],
  vehicleId: [{ required: true, message: '请选择车辆' }],
  transportMode: [{ required: true, message: '请选择运输方式' }],
  businessDate: [{ required: true, message: '请选择业务日期' }],
  loadTime: [{ required: true, message: '请选择装货时间' }],
  unloadTime: [{ required: true, message: '请选择卸货时间' }],
};

// ----------------------- 提交表单 ------------------------
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      SmartLoading.show();
      try {
        await waybillApi.update(form);
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
