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
    <a-select-option v-for="item in driverList" :key="item.driverId" :label="`${item.driverName}${item.telephone}${item.shorthandCode}`">
      {{ `${item.driverName}-${item.telephone}` }}
      <span v-show="item.shorthandCode"> ({{ item.shorthandCode }}) </span>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { driverApi } from '/@/api/business/driver/driver-api';

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
  multiple: {
    type: Boolean,
    default: false,
  },
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

function handleChange(value) {
  emit('update:value', value);
  emit('change', value);
}

const driverList = ref([]);
async function queryDict() {
  let res = await driverApi.queryDriverSelect();
  driverList.value = res.data;
}
onMounted(queryDict);
</script>
