<!--
 * @Description: 字典表下拉框
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-07-11
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
    @change="handleChange"
    :disabled="disabled"
    :mode="multiple ? 'multiple' : ''"
    optionFilterProp="label"
  >
    <a-select-option v-for="item in dataList" :key="item.vehicleId" :label="`${item.vehicleNumber}${item.shorthand}`">
      {{ item.vehicleNumber }}
      <span v-show="item.shorthand"> ({{ item.shorthand }}) </span>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { vehicleApi } from '/@/api/business/vehicle/vehicle-api';

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
  carCostFlag: {
    type: Boolean,
    default: false,
  },
  multiple:{
    type: Boolean,
    default: false,
  },
  // 司机ID // 如果存在，则根据司机ID查询车辆列表
  driverId: {
    type: Number,
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
// 箭头value变化
watch(
    () => props.driverId,
    (newValue) => {
      queryData();
    }
);

function handleChange (value) {
  emit('update:value', value);
  emit('change', value);
}

const dataList = ref([]);

async function queryData () {
  let res = await vehicleApi.getAll(props.driverId)
  dataList.value = res.data;
}

onMounted(queryData);

defineExpose({
  queryData
})
</script>
