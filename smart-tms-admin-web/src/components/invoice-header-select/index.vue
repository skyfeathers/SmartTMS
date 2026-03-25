<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-11
 * @LastEditTime: 2022-07-14
 * @LastEditors: zhuoda
-->
<template>
  <a-select
      v-model:value="selectValue"
      :style="`width: ${width}`"
      :placeholder="props.placeholder"
      :showSearch="true"
      :allowClear="true"
      :size="size"
      :mode="multiple ? 'multiple' : ''"
      @change="handleChange"
      :disabled="disabled"
      optionFilterProp="label"
  >
    <a-select-option v-for="item in headList" :key="item.invoiceId" :value="item.invoiceId"
                     :label="item.invoiceName">
      {{item.invoiceName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { driverApi } from '/@/api/business/driver/driver-api';
import {shipperApi} from "/@/api/business/shipper/shipper-api";

const props = defineProps({
  value: [Number, String, Object, Array],
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
  multiple: {
    type: Boolean,
    default: false,
  },
  shipperId: {
    type: Number,
    default: null,
  },
});
const emit = defineEmits(['update:value', 'change']);

const selectValue = ref(props.value);

const headList = ref([]);

async function queryList () {
  let result = await shipperApi.queryInvoiceList(props.shipperId);
  headList.value = result.data;
}
function handleChange (value) {
  emit('update:value', value);
  let findValue = headList.value.find(e=>e.invoiceId == value);
  emit('change', value, findValue);
}

onMounted(queryList);
</script>
