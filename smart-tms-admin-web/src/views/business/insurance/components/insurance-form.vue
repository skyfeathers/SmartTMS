<!--
 * @Description:保险表单
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-16
-->
<template>
  <a-modal
    :width="700"
    :open="visible"
    :title="form.insuranceId ? '编辑' : '添加'"
    ok-text="保存"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 8 }">
      <a-row>
        <a-col :span="12">
          <a-form-item label="保险对象" name="moduleType">
            <SmartEnumSelect width="200px" v-model:value="form.moduleType" @change="changeModuleType" placeholder="保险对象"
                             :disabled="form.disabled" enum-name="INSURANCE_MODULE_TYPE_ENUM"/>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="选择对象" name="moduleId">
            <VehicleSelect v-model:value="form.moduleId" width="200px" v-if="vehicleFlag" :disabled="form.disabled"/>
            <BracketSelect v-model:value="form.moduleId" width="200px" v-else :disabled="form.disabled"/>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="保险公司" name="insuranceCompanyCode">
            <SmartDictSelect keyCode="insuranceCompanyCode" v-model:value="form.insuranceCompanyCode" width="200px" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="保单号" name="policyNumber">
            <a-input v-model:value="form.policyNumber" placeholder="" style="width: 200px" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="保险类型" name="insuranceType">
            <SmartEnumSelect v-model:value="form.insuranceType" enum-name="INSURANCE_TYPE_ENUM" width="200px" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="保额（万元）" name="insuranceAmount">
            <a-input-number v-model:value="form.insuranceAmount" style="width: 200px" :min="0"/>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="保险费用（元）" name="policyInsuranceAmount">
            <a-input-number v-model:value="form.policyInsuranceAmount" style="width: 200px" :min="0" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="投保时间" name="effectDate">
            <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.effectDate" style="width: 200px" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="到期时间" name="expireDate">
            <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.expireDate" style="width: 200px" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="登记日期" name="enrollDate">
            <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.enrollDate" style="width: 200px" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="备注" name="remark">
            <a-input v-model:value="form.remark" placeholder="请输入备注" style="width: 200px" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="附件">
            <Upload :default-file-list="form.attachment"
                    :folder="FILE_FOLDER_TYPE_ENUM.INSURANCE.value"
                    @change="changeAttachment"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import BracketSelect from '/@/components/bracket-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { ref, reactive, nextTick, computed } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { INSURANCE_MODULE_TYPE_ENUM } from '/@/constants/business/insurance-const';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { insuranceApi } from '/@/api/business/insurance/insurance-api';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const formDefault = {
  moduleType: INSURANCE_MODULE_TYPE_ENUM.VEHICLE.value,
  moduleId: null,
  insuranceCompanyCode: null,
  policyNumber: undefined,
  insuranceType: undefined,
  insuranceAmount: undefined,
  policyInsuranceAmount: undefined,
  expireTime: undefined,
  attachment: [],
  insuranceId: null,
  effectDate: null,
  expireDate: null,
  enrollDate: null,
  remark: undefined
};
let form = reactive({ ...formDefault });
const rules = {
  moduleType: [{ required: true, message: '请选择保险对象' }],
  moduleId: [{ required: true, message: '请选择保险对象' }],
  insuranceCompanyCode: [{ required: true, message: '请选择保险公司' }],
  insuranceType: [{ required: true, message: '请选择保险类型' }],
  policyNumber: [{ required: true, message: '请输入保单号' }],
  insuranceAmount: [{ required: true, message: '请输入保额' }],
  policyInsuranceAmount: [{ required: true, message: '请输入保险费用' }],
  expireDate: [{ required: true, message: '请选择到期时间' }],
  effectDate: [{ required: true, message: '请选择投保时间' }],
};
function changeAttachment(e) {
  form.attachment = e;
}
// ----------------------- 提交表单 ------------------------
const formRef = ref();
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        if (form.insuranceId) {
          await insuranceApi.update(form);
        } else {
          await insuranceApi.save(form);
        }
        message.success(`${form.insuranceId ? '修改' : '添加'}成功`);
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

// 保险对象类型发生变化，将ID设置为null
function changeModuleType () {
  form.moduleId = null;
}

// 保险对象是否为车辆
const vehicleFlag = computed(() => {
  return form.moduleType == INSURANCE_MODULE_TYPE_ENUM.VEHICLE.value;
});
</script>
