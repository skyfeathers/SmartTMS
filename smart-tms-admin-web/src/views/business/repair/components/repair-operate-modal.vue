<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-14
-->
<template>
  <a-modal :getContainer="getContainer" :title="form.repairId ? '编辑' : '添加'" :open="visible" cancel-text="取消" ok-text="确认"
           width="800px" @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef"
            :label-col="{ span: 5 }"
            :model="form"
            :rules="rules"
            :wrapper-col="{ span: 13 }">
      <a-form-item label="维修类型" name="moduleType">
            <SmartEnumRadio width="100%" v-model:value="form.moduleType" @change="changeModuleType" placeholder="维修类型"
                             :disabled="form.disabled" isButton enum-name="INSURANCE_MODULE_TYPE_ENUM"/>
      </a-form-item>
      <a-form-item label="维修对象" name="moduleId">
            <VehicleSelect v-model:value="form.moduleId" width="100%" v-if="vehicleFlag" :disabled="form.disabled"/>
            <BracketSelect v-model:value="form.moduleId" width="100%" v-else :disabled="form.disabled"/>
          </a-form-item>
      <a-form-item label="维修厂家" name="repairPlantId" style="position: relative;">
        <RepairPlantSelect v-model:value="form.repairPlantId" placeholder="请选择维修厂家" style="width:100%"/>
        <!-- 点击新建维修厂家 -->
      <a-button type="primary" style="position: absolute;right: -70px;" @click="addRepairPlant()">新建</a-button>
      </a-form-item>
      <a-form-item label="维修人" name="repairPerson">
        <a-input v-model:value="form.repairPerson" placeholder="请输入维修人" />
      </a-form-item>
      <a-form-item label="维修时间" name="repairDate">
        <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.repairDate" style="width: 100%"/>
      </a-form-item>

      <a-col :span="24">
          <a-row>
            <a-col :span="5" style="text-align: right">
              <span><span style="color: red">*</span>维修内容：</span>
            </a-col>
            <a-col :span="19">
              <div>
                <a-button class="smart-margin-bottom5" type="primary"  @click="addContentFormList" style="margin-bottom: 8px;">新增</a-button>
              </div>
              <a-space v-for="(item,index) in form.contentFormList" :key="index" align="baseline">
                <a-form-item :name="['contentFormList', index, 'repairContent']"
                             :rules="{required: true, message: '请输入维修费用'}">
                        <a-input v-model:value="item.repairContent" placeholder="请输入维修内容" style="width: 190px"/>
                </a-form-item>
                <a-form-item :name="['contentFormList', index, 'repairAmount']"
                             :rules="{required: true, message: '请输入费用'}">
                        <a-input-number v-model:value="item.repairAmount" :min="0" :max="999999" :precision="2" placeholder="请输入维修费用"  class="smart-margin-left10" style="width: 130px"/>
                </a-form-item>
                <a-button v-if="index != 0" type="primary" ghost class="smart-margin-left10" @click="removeContentFormList(item)">移除
                </a-button>
              </a-space>
            </a-col>
          </a-row>
        </a-col>

      <a-form-item label="维修配件附件" name="attachment">
        <Upload
          accept=".jpg,.jpeg,.png,.gif"
          listType="picture-card"
          :maxUploadSize="1"
          buttonText="点击上传维修配件附件"
          :default-file-list="form.attachment"
          @change="attachmentChange"
        />
      </a-form-item>
    </a-form>

  </a-modal>

    <!-- 新建维修厂家 -->
    <RepairPlaceOperateModal ref="repairPlaceOperateRef"/>
</template>
<script setup>
import { ref, reactive, nextTick, computed } from 'vue';
import { message } from 'ant-design-vue';
import Upload from '/@/components/upload/index.vue';
import RepairPlantSelect from '/@/components/repair-plant-select/index.vue';
import RepairPlaceOperateModal from '/@/views/business/business-material/repair-place/components/repair-place-operate-modal.vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { repairApi } from '/@/api/business/repair/repair-api';
import { getContainer } from '/@/utils/container-util';
import dayjs from 'dayjs';

import SmartEnumRadio from '/@/components/smart-enum-radio/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import BracketSelect from '/@/components/bracket-select/index.vue';
import { INSURANCE_MODULE_TYPE_ENUM } from '/@/constants/business/insurance-const';

//  组件
const formRef = ref();
const rules = {
  moduleType: [{ required: true, message: '请选择维修类型' }],
  moduleId: [{ required: true, message: '请选择维修对象' }],
  repairPlantId: [{ required: true, message: '请输入维修厂家' }],
  repairPerson: [{ required: true, message: '请输入维修人' }],
  repairDate: [{ required: true, message: '请输入维修时间' }],
};

// 默认值
const formDefault = {
  repairId: undefined,
  moduleId: undefined,
  moduleType: INSURANCE_MODULE_TYPE_ENUM.VEHICLE.value,
  repairDate: undefined,
  repairPerson: undefined,
  repairPlantId: undefined,
  contentFormList: [{repairContent:undefined,repairAmount:undefined}],
  attachment: undefined,
};
let form = reactive({ ...formDefault });

// 日期
// const repairDateRef = ref(dayjs());
// 日期控件
// function dateChange(dates, dateStrings) {
//   form.repairDate = dateStrings;
// }

// 是否展示
const visible = ref(false);
function showModal(rowData, moduleId, moduleType) {
  Object.assign(form, formDefault);
  if (rowData) {
    rowData.contentFormList=rowData.contentVOList
    delete rowData.contentVOList
    Object.assign(form, rowData);
    console.log(form)
    // repairDateRef.value = dayjs(rowData.repairDate, 'YYYY-MM-DD');
  } else {
    form.repairDate = dayjs().format('YYYY-MM-DD');
  }
  
  if(moduleId && moduleType) {
    form.moduleId = moduleId;
    form.moduleType = moduleType;
    form.disabled = true;
  }
  visible.value = true;
}

function onClose() {
  formRef.value.clearValidate()
  Object.assign(form, formDefault);
  emit('reloadList')
  if (!form.repairId) {
      form.contentFormList=formDefault.contentFormList=[
      {repairContent:undefined,repairAmount:undefined}]
      }
  visible.value = false;

}
// 新建维修厂家
const repairPlaceOperateRef = ref();
function addRepairPlant(rowData = null) {
  repairPlaceOperateRef.value.showModal(rowData);
}

//新增维修内容
function addContentFormList(){
  form.contentFormList.push({
    repairContent:undefined,
    repairAmount:undefined
  })
}
//删除维修内容
function removeContentFormList(item){
  const index = form.contentFormList.indexOf(item);
  if (index !== -1) {
    form.contentFormList.splice(index, 1);
  }
}

// emit
const emit = defineEmits(['reloadList']);
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        if (form.repairId) {
          await repairApi.update(form);
        } else {
          await repairApi.create(form);
        }
        message.success(`${form.repairId ? '修改' : '添加'}成功`);
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

function attachmentChange(fileList) {
  form.attachment = fileList;
}

const vehicleFlag = computed(() => {
  return form.moduleType == INSURANCE_MODULE_TYPE_ENUM.VEHICLE.value;
});


// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
