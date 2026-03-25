<!--
 * @Description: 订单的企业列表下拉
 * @Author: lidoudou
 * @Date: 2022-07-14
 * @LastEditTime: 2022-07-14
 * @LastEditors: lidoudou
-->
<template>
  <a-select
    :dropdownMatchSelectWidth="false"
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
    <a-select-option v-for="item in dataList" :key="item.enterpriseId" :label="item.enterpriseName">
      {{ item.enterpriseName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { enterpriseApi } from '/@/api/business/oa/enterprise-api';

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
  // 货主ID
  shipperId: {
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
    () => props.shipperId,
    (newValue) => {
      queryData();
    }
);

function handleChange(value) {
  emit('update:value', value);
  emit('change', value);
}

const dataList = ref([]);

async function queryData () {
  if (!props.shipperId) {
    return;
  }
  let res = await enterpriseApi.queryByShipperId(props.shipperId);
  dataList.value = res.data;
}
onMounted(queryData);
</script>
