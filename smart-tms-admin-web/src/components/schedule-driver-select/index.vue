<!--
 * @Description: 分配司机选择组件
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
    <a-select-option v-for="item in driverList" :key="item.telephone"
                     :label="`${item.driverName}${item.telephone}${item.shorthandCode}`">
      {{ `${item.driverName}-${item.telephone}` }}
      <span v-show="item.shorthandCode"> ({{ item.shorthandCode }}) </span>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { driverApi } from '/@/api/business/driver/driver-api';

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
  // 如果存在，则根据车辆ID查询司机列表
  vehicleId: {
    type: Number,
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

watch(
    () => props.vehicleId,
    (newValue) => {
      queryData();
    }
);


function handleChange (value) {
  let driver = driverList.value.find(e => e.telephone == value);
  emit('change', driver);
}

const driverList = ref([]);

async function queryData () {
  driverList.value = [];
  let res = await driverApi.queryDriverSelect(props.vehicleId);
  driverList.value = res.data;
}

onMounted(queryData);

defineExpose({
  queryData
})

</script>
