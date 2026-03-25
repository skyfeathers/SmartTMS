<!--
 * @Description: 维修地点新建、编辑
 * @version:
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-14 08:33:22
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-19 10:24:17
-->
<template>
  <a-modal :open="visible"
    title="添加"
    :width="640"
    forceRender
    ok-text="确认"
    cancel-text="取消"
    :zIndex="1001"
    @ok="onSubmit"
    @cancel="onClose">
    <a-form ref="formRef"
      :model="formData"
      :rules="formRules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }">
      <a-form-item label="车辆维修厂家简称"
        name="repairPlantName">
        <a-input v-model:value="formData.repairPlantName"
          placeholder="请输入" />
      </a-form-item>
      <a-form-item label="维修厂地址">
        <SmartAreaCascader v-model:value="areaCodeList"
          type="province_city_district"
          width="100%"
          @change="changeArea"
          placeholder="请选择所在地区" />
      </a-form-item>
      <a-form-item label="详细地址">
        <a-input v-model:value="formData.addressDetail"
          placeholder="请输入" />
      </a-form-item>
      <a-form-item label="联系人">
        <a-input v-model:value="formData.contactName"
          placeholder="请输入" />
      </a-form-item>
      <a-form-item label="联系方式">
        <a-input v-model:value="formData.contactPhone"
          placeholder="请输入" />
      </a-form-item>
      <a-form-item label="状态"
        name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, watch } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import BooleanFlagSelect from '/@/components/boolean-flag-select/index.vue';
import { repairPlantApi } from '/@/api/business/business-material/repair-plant';
import emitter from '/@/utils/repair-place-mitt';
const emits = defineEmits(['reloadList']);

const visible = ref(false);
const formRef = ref();
const areaCodeList = ref([]);

const defaultFormData = {
  repairPlantId: undefined,
  repairPlantName: undefined,
  provinceCode: undefined, //省份code
  areaCode: undefined, //城市code
  cityCode: undefined, //区县code
  addressArea: undefined, // 地区名称
  addressDetail: undefined, // 详细地址
  contactName: undefined,
  contactPhone: undefined,
  disabledFlag: false,
};

const formData = reactive({ ...defaultFormData });
const enabledChecked = ref(true);

const formRules = {
  repairPlantName: [{ required: true, message: '请输入' }],
  disabledFlag: [{ required: true, message: '请选择' }],
};

function showModal(rowData = null) {
  if (rowData) {
    if(rowData.provinceCode){
      const codeList = [{ value: rowData.provinceCode }, { value: rowData.cityCode }];
      if (rowData.areaCode) {
        codeList.push({ value: rowData.areaCode });
      }
      areaCodeList.value = codeList;
    }
    Object.assign(formData, rowData);
    enabledChecked.value = !rowData.disabledFlag;
  }
  visible.value = true;
}


function enabledCheckedChange(checked) {
  formData.disabledFlag = !checked;
}

function onClose() {
  visible.value = false;
  enabledChecked.value = true;
}

watch(visible, (newVal) => {
  if (!newVal) {
    formRef.value.resetFields();
    areaCodeList.value = [];
    Object.assign(formData, defaultFormData);
  }
});

async function onSubmit() {
  try {
    await formRef.value.validateFields();
    console.log(formData);
    addUpdateRepairPlant();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

async function addUpdateRepairPlant() {
  try {
    useSpinStore().show();
    if (formData.repairPlantId) {
      await repairPlantApi.updateRepairPlant(formData);
    } else {
      await repairPlantApi.addRepairPlant(formData);
      emitter.emit("createRepairPlant")
    }
    message.success('保存成功');
    emits('reloadList');
    onClose();
  } catch (err) {
    console.log(err);
  } finally {
    useSpinStore().hide();
  }
}

// 选择地区改变
function changeArea(value, selectedOptions) {
  if (selectedOptions) {
    formData.provinceCode = selectedOptions[0].value;
    formData.cityCode = selectedOptions[1].value;
    formData.areaCode = selectedOptions[2] ? selectedOptions[2].value : undefined;
    const addressArea = selectedOptions.map((item) => item.label);
    formData.addressArea = addressArea.join('');
    return;
  }
  formData.provinceCode = 0;
  formData.areaCode = 0;
  formData.cityCode = 0;
  formData.addressArea = '';
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
