<!--
 * @Description: 货主下拉
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
    :dropdownStyle="{ minWidth: '450px'}"
    optionFilterProp="label"
  >
    <a-select-option v-for="item in dataList" :key="item.shipperId" :label="`${item.consignor}${item.shortName}`">
      {{ item.shortName }}（{{item.consignor}}）
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { shipperApi } from '/@/api/business/shipper/shipper-api';

const props = defineProps({
  value: [Number, String, Object],
  width: {
    type: String,
    default: '300px',
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

function handleChange (value) {
  let obj = null;
  if (value) {
    obj = dataList.value.find(e => e.shipperId == value);
  }
  emit('update:value', value);
  emit('change', value, obj);
}

const dataList = ref([]);
async function queryData() {
  let res = await shipperApi.queryList();
  dataList.value = res.data;
}
onMounted(queryData);

defineExpose({
  queryData
})
</script>
