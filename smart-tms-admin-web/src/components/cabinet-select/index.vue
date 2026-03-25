<!--
 * @Description: 柜型下拉选择
 * @Author: lidoudou
 * @Date: 2022-07-15
 * @LastEditTime: 2022-07-15 09:45:40
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
    <a-select-option v-for="item in dataList" :key="item.cabinetId" :label="item.cabinetName">
      {{ item.cabinetName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { cabinetApi } from '/@/api/business/business-material/cabinet-api';
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
  // 是否默认显示第一个
  default: {
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

function handleChange(value) {
  emit('update:value', value);
  emit('change', value);
}

const dataList = ref([]);
async function queryData() {
  let res = await cabinetApi.queryList();
  dataList.value = res.data;
  if (props.default && !_.isEmpty(res.data)) {
    handleChange(res.data[0].cabinetId);
  }
}
onMounted(queryData);
</script>
