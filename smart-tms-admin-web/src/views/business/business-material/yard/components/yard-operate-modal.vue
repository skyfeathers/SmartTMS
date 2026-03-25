<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :title="form.yardId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="堆场名称" name="yardName">
        <a-input v-model:value="form.yardName" placeholder="请输入堆场名称"/>
      </a-form-item>
      <a-form-item label="堆场编号" name="yardCode">
        <a-input v-model:value="form.yardCode" placeholder="请输入堆场编号" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="堆场地区" name="province">
        <smart-area-cascader type="province_city_district" style="width: 100%"
                             v-model:value="area"
                             @change="changeArea"/>
      </a-form-item>
      <a-form-item label="详细地址" name="address">
        <a-input v-model:value="form.address" placeholder="请输入详细地址"/>
      </a-form-item>
      <a-form-item label="联系人" name="contact">
        <a-input v-model:value="form.contact" placeholder="请输入联系人"/>
      </a-form-item>
      <a-form-item label="联系人电话" name="contactPhone">
        <a-input v-model:value="form.contactPhone" placeholder="请输入联系人电话"/>
      </a-form-item>
      <a-form-item label="堆场状态" name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import {ref, reactive, nextTick} from 'vue';
import {message} from 'ant-design-vue';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {yardApi} from '/@/api/business/business-material/yard-api';
import _ from 'lodash';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  yardId: undefined,
  yardCode: '',
  yardName: '',
  contact: '',
  contactPhone: '',
  province: undefined,
  provinceName: undefined,
  city: undefined,
  cityName: undefined,
  district: undefined,
  districtName: undefined,
  address: undefined,
  disabledFlag: false,
};
let form = reactive({...formDefault});

const rules = {
  yardName: [{required: true, message: '请输入堆场名称'}],
  yardCode: [{required: true, message: '请输入堆场编号'}],
  province: [{ required: true, message: '请选择堆场地区' }],
  address: [{required: true, message: '请输入详细地址'}],
  contactPhone: [{required: true, message: '请输入联系人电话'}],
  contact: [{required: true, message: '请输入联系人'}],
};
const enabledChecked = ref(true);
// 是否展示
const visible = ref(false);
// 地区
const area = ref([]);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function enabledCheckedChange(checked) {
  form.disabledFlag = !checked;
}

function showModal(rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    Object.assign(form, rowData);
    enabledChecked.value = !rowData.disabledFlag;
    nextTick(() => {
      area.value = [
        {
          value: rowData.province,
          label: rowData.provinceName,
        },
        {
          value: rowData.city,
          label: rowData.cityName,
        },
        {
          value: rowData.district,
          label: rowData.districtName,
        },
      ]
    });
  } else {
    area.value = [];
  }
  visible.value = true;
}

function onClose() {
  Object.assign(form, formDefault);
  formRef.value.resetFields();
  enabledChecked.value = true;
  area.value = [];
  visible.value = false;
}

function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.yardId) {
            await yardApi.update(form);
          } else {
            await yardApi.create(form);
          }
          message.success(`${form.yardId ? '修改' : '添加'}成功`);
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

function changeArea(value, selectedOptions) {
  if (!_.isEmpty(selectedOptions)) {
    // 地区信息
    form.province = area.value[0].value;
    form.provinceName = area.value[0].label;

    form.city = area.value[1].value;
    form.cityName = area.value[1].label;
    if (area.value[2]) {
      form.district = area.value[2].value;
      form.districtName = area.value[2].label;
    }
  }
}


// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
