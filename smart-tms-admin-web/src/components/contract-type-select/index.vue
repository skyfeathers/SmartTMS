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
    <a-select-option v-for="item in dataList" :key="item.contractTypeId" :label="item.name">
      {{ item.name }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import { contractTypeApi } from '/@/api/business/business-material/contract-type-api';

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
  }
});

const dataList = ref([]);
async function queryData() {
  let res = await contractTypeApi.queryAll();
  dataList.value = res.data;
}

onMounted(queryData);

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
</script>
