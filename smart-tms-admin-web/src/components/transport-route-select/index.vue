<!--
 * @Description: 运输路线下拉选择
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
    <a-select-option v-for="item in dataList" :key="item.transportRouteId" :label="item.transportRouteName">
      {{ item.transportRouteName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { transportRouteApi } from '/@/api/business/business-material/transport-route-api';

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
  transportRouteType: {
    type: Number,
    required: true
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

// transportRouteType类型发生变化，重新查询运输路线下拉
watch(
    () => props.transportRouteType,
    (newValue) => {
      queryData();
    }
);

function handleChange (value) {
  let obj = null;
  if (value) {
    obj = dataList.value.find(e => e.transportRouteId == value);
  }
  emit('update:value', value);
  emit('change', value, obj);
}

const dataList = ref([]);
async function queryData() {
  if(!props.transportRouteType){
    return;
  }
  let res = await transportRouteApi.queryList(props.transportRouteType);
  dataList.value = res.data;
}
onMounted(queryData);

defineExpose({
  queryData,
  selectValue
})
</script>
