<!--
 * @Description: 货主的开票信息下拉
 * @Author: lidoudou
 * @Date: 2022-07-15
 * @LastEditTime: 2022-07-15
 * @LastEditors: lidoudou
-->
<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
    :disabled="disabled"
    :mode="multiple ? 'multiple' : ''"
    optionFilterProp="label"
  >
    <a-select-option v-for="item in dataList" :key="item.invoiceId" :label="item.invoiceName">
      {{ item.invoiceName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { shipperApi } from '/@/api/business/shipper/shipper-api';
import _ from 'lodash';

const props = defineProps({
  value: [Number, String, Object],
  width: {
    type: String,
    default: '200px',
  },
  placeholder: {
    type: String,
    default: '请选择',
  },
  size: {
    type: String,
    default: 'default',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  multiple:{
    type: Boolean,
    default: false,
  },
  shipperId: {
    type: Number,
  },
  // 是否默认选中第一个，默认【是】
  showDefaultFlag: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(['update:value', 'change']);

const selectValue = ref(props.value);

// 箭头value变化
watch(
  () => props.value,
  (newValue) => {
    selectValue.value = newValue;
  }
);

// 箭头货主ID变化
watch(
    () => props.shipperId,
    (newValue) => {
      queryData();
    }
);

function handleChange (value) {
  emit('update:value', value);
  emit('change', value, dataList.value.find(e => e.invoiceId == value));
}

const dataList = ref([]);

async function queryData () {
  if (!props.shipperId) {
    return;
  }
  let res = await shipperApi.queryInvoiceList(props.shipperId);
  dataList.value = res.data;
  if(!props.value && !_.isEmpty(dataList.value) && props.showDefaultFlag) {
    selectValue.value = res.data[0].invoiceId;
    handleChange(res.data[0].invoiceId);
  }
}

onMounted(queryData);

defineExpose({
  queryData
})
</script>
