<template>
  <a-modal :getContainer="getContainer" :open="visible" :width="600" cancel-text="取消" ok-text="保存" title="快速新建"
    @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef" :label-col="{ span: 8 }" :model="form" :rules="rules">
      <a-row>
        <a-col :span="12">
          <a-form-item label="创建类型" name="createType">
            <a-radio-group v-model:value="form.createType">
              <a-radio-button :value="QUICK_CREATE_TYPE_ENUM.DRIVER.value">司机</a-radio-button>
              <a-radio-button :value="QUICK_CREATE_TYPE_ENUM.VEHICLE.value">车辆</a-radio-button>
              <a-radio-button :value="QUICK_CREATE_TYPE_ENUM.BRACKET.value">挂车</a-radio-button>
              <a-radio-button :value="QUICK_CREATE_TYPE_ENUM.ALL.value">全部</a-radio-button>

            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row>
        <a-col :span="12">
          <a-form-item label="经营方式" name="businessMode">
            <SmartEnumSelect v-model:value="form.businessMode" enumName="VEHICLE_BUSINESS_MODE_ENUM" width="100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <template
        v-if="form.createType == QUICK_CREATE_TYPE_ENUM.ALL.value || form.createType == QUICK_CREATE_TYPE_ENUM.DRIVER.value">
        <a-row>
          <a-col :span="12">
            <a-form-item label="司机姓名" name="driverName">
              <a-input v-model:value="form.driverName" placeholder="请输入司机姓名" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="司机电话" name="telephone">
              <a-input v-model:value="form.telephone" placeholder="请输入司机电话" />
            </a-form-item>
          </a-col>
        </a-row>
      </template>

      <template v-if="form.createType == QUICK_CREATE_TYPE_ENUM.ALL.value || form.createType == QUICK_CREATE_TYPE_ENUM.VEHICLE.value">
        <a-row>
          <a-col :span="12">
            <a-form-item label="车牌号" name="vehicleNumber">
              <a-input v-model:value="form.vehicleNumber" placeholder="请输入车牌号" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
      </template>
      <template v-if="form.createType == QUICK_CREATE_TYPE_ENUM.ALL.value || form.createType == QUICK_CREATE_TYPE_ENUM.BRACKET.value">
        <a-row>
          <a-col :span="12">
            <a-form-item label="挂车车牌号" name="bracketNo">
              <a-input v-model:value="form.bracketNo" placeholder="请输入挂车车牌号" style="width:100%" />
            </a-form-item>
          </a-col>
        </a-row>
      </template>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick, computed } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { vehicleApi } from '/@/api/business/vehicle/vehicle-api';
import { getContainer } from '/@/utils/container-util';
import { regular } from '/@/constants/regular-const';
import { VEHICLE_BUSINESS_MODE_ENUM, QUICK_CREATE_TYPE_ENUM } from '/@/constants/business/vehicle-const';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});
// 内管车、挂靠车显示所属公司
const showEnterpriseFlag = computed(() => {
  let typeList = [
    VEHICLE_BUSINESS_MODE_ENUM.INNER_MANAGEMENT.value,
    VEHICLE_BUSINESS_MODE_ENUM.RELY.value
  ];
  return typeList.includes(form.businessMode);
})
// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose() {
  formRef.value.clearValidate();
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(type) {
  Object.assign(form, formDefault);
  if(type){
    form.createType = type
  }
  visible.value = true;
}

// ----------------------- 表单 ------------------------
const formDefault = {
  businessMode: VEHICLE_BUSINESS_MODE_ENUM.ASSIGNMENT.value,
  enterpriseId: null,
  driverName: null,
  telephone: null,
  driverShorthandCode: null,
  vehicleNumber: null,
  vehicleShorthandCode: null,
  bracketNo: null,
  bracketShorthandCode: null,
  createType: QUICK_CREATE_TYPE_ENUM.DRIVER.value
};
let form = reactive({ ...formDefault });
const rules = {
  enterpriseId: [{ required: true, message: '请选择所属公司' }],
  businessMode: [{ required: true, message: '请选择经营方式' }],
  driverName: [{ required: true, message: '请输入司机姓名' }],
  bracketNo: [{ required: true, message: '请输入挂车车牌号' }],
  telephone: [
    { required: true, message: '请输入司机电话', },
    { pattern: regular.phone, message: '司机电话格式不正确', trigger: 'blur' }],
  vehicleNumber: [{ required: true, message: '请输入车牌号' },],
};

// ----------------------- 提交表单 ------------------------
const formRef = ref();

function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      SmartLoading.show();
      try {
        await vehicleApi.quickCreate(form);
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
