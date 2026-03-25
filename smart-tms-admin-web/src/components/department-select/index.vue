<!--
 * @Description: 所有部门
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
    <a-select-option v-for="item in dataList" :key="item.departmentId" :label="item.name">
      {{ item.name }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { departmentApi } from '/@/api/system/department/department-api';
import _ from 'lodash';

const props = defineProps({
  value: {
    type: [Number, String, Object],
    default: null
  },
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
  // 类型 ENTERPRISE_TYPE_ENUM
  type:{
    type: Number,
  },
  // 是否查询网络货运企业，true 查询
  nftFlag: {
    type: Boolean,
    default: false
  },
  // 是否设置默认值
  defaultFlag: {
    type: Boolean,
    default: false
  },
  api: {
    type: Function,
    default: departmentApi.queryAllDepartment,
  },
  // 企业id
  enterpriseId: {
    type: Number,
    default: null
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

const dataList = ref([]);

async function queryData () {
    let res = await props.api(props.enterpriseId);
    dataList.value = res.data;
}
onMounted(queryData);
</script>
