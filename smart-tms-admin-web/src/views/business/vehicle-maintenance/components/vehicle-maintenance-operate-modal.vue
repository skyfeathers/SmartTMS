<!--
 * @Description: file content
 * @Author: baijuhui
 * @Date: 2023-10-18 11:48:22
-->
<template>
  <a-modal :title="form.maintenanceId ? '编辑保养' : '新建保养'" :open="visible" :width="800" cancel-text="取消" ok-text="确认"
           @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef" :label-col="{ span: 6 }" :model="form"
            :rules="rules">
      <a-row>
        <a-col :span="12">
          <a-form-item label="保养车辆" name="vehicleId">
            <VehicleSelect v-model:value="form.vehicleId" width="100%" :disabled="form.disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="保养时间" name="maintenanceDate">
            <a-date-picker v-model:value="form.maintenanceDate" valueFormat="YYYY-MM-DD" style="width: 100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下次保养时间" name="nextMaintenanceDate">
            <a-date-picker v-model:value="form.nextMaintenanceDate" valueFormat="YYYY-MM-DD" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="保养里程" name="mileage">
            <a-input-number v-model:value="form.mileage" :max="999999" :min="0" :precision="2" placeholder="请输入保养里程" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="下次保养里程" name="nextMaintenanceMileage">
            <a-input-number v-model:value="form.nextMaintenanceMileage" :max="999999" :min="0" :precision="2" placeholder="请输入保养里程" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="保养人" name="maintenancePerson">
            <a-input v-model:value="form.maintenancePerson" placeholder="请输入保养人" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="保养地点" name="maintenancePlant">
            <a-input v-model:value="form.maintenancePlant" placeholder="请输入保养地点" />
          </a-form-item>
        </a-col>


        <a-col :span="24">
          <a-row>
            <a-col :span="3" style="text-align: right">
              <span><span style="color: red">*</span>保养内容：</span>
            </a-col>
            <a-col :span="21">
              <a-space v-for="(item,index) in form.contentFormList" :key="index" align="baseline">
                <a-form-item :name="['contentFormList', index, 'maintenanceContent']"
                             :rules="{required: true, message: '请输入保养费用'}">
                  <a-input v-model:value="item.maintenanceContent" placeholder="请输入保养内容" style="width: 190px"/>
                </a-form-item>
                <a-form-item :name="['contentFormList', index, 'maintenanceAmount']"
                             :rules="{required: true, message: '请输入费用'}">
                  <a-input-number v-model:value="item.maintenanceAmount" :max="999999" :min="0" :precision="2" class="smart-margin-left10"  placeholder="请输入维修费用" style="width: 130px"/>
                </a-form-item>
                <a-button v-if="index == 0" class="smart-margin-bottom5" style="margin-bottom: 8px;"  type="primary" @click="addContentFormList">新增</a-button>
                <a-button v-if="index != 0" class="smart-margin-left10" ghost type="primary" @click="removeContentFormList(item)">移除
                </a-button>
              </a-space>
            </a-col>
          </a-row>
        </a-col>


        <a-col :span="12">
          <a-form-item label="保养备注" name="remark">
            <a-textarea v-model:value="form.remark" placeholder="请输入保养备注" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { vehicleMaintenanceApi } from '/@/api/business/vehicle-maintenance/vehicle-maintenance-api';
import dayjs from 'dayjs';
import VehicleSelect from '/@/components/vehicle-select/index.vue';

//  组件
const formRef = ref();
const rules = {
  vehicleId: [{ required: true, message: '请选择保养车辆' }],
  maintenanceDate: [{ required: true, message: '请输入保养时间' }],
  maintenanceAmount: [{ required: true, message: '请输入保养费用' }],
  mileage: [{ required: true, message: '请输入保养里程' }],
};

// 默认值
const formDefault = {
  vehicleId: undefined,
  maintenancePerson: undefined,
  maintenanceAmount: undefined,
  maintenancePlant: undefined,
  remark: undefined,
  maintenanceDate: undefined,
  maintenanceId: undefined,
  mileage: undefined,
  nextMaintenanceDate: undefined,
  nextMaintenanceMileage: undefined,
  contentFormList: [{
    maintenanceContent: undefined,
    maintenanceAmount: undefined
  }]
};
let form = reactive({ ...formDefault });

// 日期
// const maintenanceDateRef = ref(dayjs());
// 日期控件
// function dateChange(dates, dateStrings) {
//   form.maintenanceDate = dateStrings;
// }
// 日期
// const nextMaintenanceDateRef = ref();
// 日期控件
// function nextDateChange(dates, dateStrings) {
//   form.nextMaintenanceDate = dateStrings;
// }

//新增维修内容
function addContentFormList(){
  form.contentFormList.push({
    maintenanceContent:undefined,
    maintenanceAmount:undefined
  })
}
//删除维修内容
function removeContentFormList(item){
  const index = form.contentFormList.indexOf(item);
  if (index !== -1) {
    form.contentFormList.splice(index, 1);
  }
}


// 是否展示
const visible = ref(false);
function showModal(rowData, vehicleId) {
  Object.assign(form, formDefault);
  if (rowData) {
    rowData.contentFormList = rowData.contentVOList;
    delete rowData.contentVOList;
    Object.assign(form, rowData);
    // maintenanceDateRef.value = dayjs(rowData.maintenanceDate, 'YYYY-MM-DD');
  } else {
    form.maintenanceDate = dayjs().format('YYYY-MM-DD');
  }
  if (vehicleId) {
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
        if (form.maintenanceId) {
          await vehicleMaintenanceApi.update(form);
        } else {
          await vehicleMaintenanceApi.create(form);
        }
        message.success(`${form.maintenanceId ? '修改' : '添加'}成功`);
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
