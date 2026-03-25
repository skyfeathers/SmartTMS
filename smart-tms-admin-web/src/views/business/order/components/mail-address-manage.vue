<template>
  <a-card title="收货人信息" class="smart-margin-top10">
    <a-form ref="formRef" :model="form" :rules="rules" labelAlign="left">
      <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }">
        <a-descriptions-item label="收货人姓名" class="required">
          <a-form-item name="consignee">
            <a-input v-model:value="form.consignee" @change="changeConsignee" placeholder="请输入收货人姓名"/>
          </a-form-item>
        </a-descriptions-item>
        <a-descriptions-item label="收货人联系电话">
          <a-form-item name="telephone">
            <a-input v-model:value="form.telephone" placeholder="请输入收货人联系电话"/>
          </a-form-item>
        </a-descriptions-item>
        <a-descriptions-item label="收货单位">
          <a-form-item name="customerName">
            <a-input v-model:value="form.customerName" placeholder="请输入收货单位"/>
          </a-form-item>
        </a-descriptions-item>
        <a-descriptions-item label="收货人识别号">
          <a-form-item name="identificationNumberOfTheTaxpayer">
            <a-input v-model:value="form.identificationNumberOfTheTaxpayer" placeholder="请输入收货人识别号"/>
          </a-form-item>
        </a-descriptions-item>
        <a-descriptions-item>
        </a-descriptions-item>
      </a-descriptions>
    </a-form>
  </a-card>
</template>
<script setup>
import { reactive, ref, watch } from 'vue';
import { message } from 'ant-design-vue';

const props = defineProps({
  // 货主详情
  detail: {
    type: Object,
  },
  defaultConsignee: {
    type: String,
  },
});

watch(
    () => props.detail.mailAddress,
    (e) => {
      if (!e) {
        return;
      }
      Object.assign(form, props.detail.mailAddress);
    }
);
watch(
    () => props.defaultConsignee,
    (e) => {
      if (!e) {
        return;
      }
      form.consignee = props.defaultConsignee
    },
);
// ------- 新增、编辑联系人 start --------

const defaultForm = {
  consignee: null,
  telephone: null,
  customerName: null,
  identificationNumberOfTheTaxpayer: null,
};
let form = reactive({ ...defaultForm });

const rules = {
  consignee: [{ required: true, message: '请输入收货人姓名' }],
  // telephone: [{ required: true, message: '请输入收货人联系电话' }],
  // customerName: [{ required: true, message: '请输入收货单位' }],
  // identificationNumberOfTheTaxpayer: [{ required: true, message: '请输入收货人识别号' }],
};
const formRef = ref();
const editFlag = ref(true);

// 保存
function onSave () {
  formRef.value.validate()
}

function getMailAddress () {
  if (form.consignee || form.telephone || form.telephone || form.identificationNumberOfTheTaxpayer) {
    return form;
  }
  return null;
}

function changeConsignee () {
  form.consignee = (form.consignee || '').replace(/\s*/g, '');
}

defineExpose({
  formRef,
  getMailAddress
});
</script>
