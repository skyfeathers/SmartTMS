<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-07-08
 * @LastEditors: zhuoda
-->
<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
  >
    <a-select-option :value="FLAG_NUMBER_ENUM.TRUE.value">
      {{ trueDesc }}
    </a-select-option>
    <a-select-option :value="FLAG_NUMBER_ENUM.FALSE.value">
      {{ falseDesc }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import _ from 'lodash';
import { ref, watch } from 'vue';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';

const props = defineProps({
  value: Number,
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
  trueDesc: {
    type: String,
    default: FLAG_NUMBER_ENUM.TRUE.desc
  },
  falseDesc: {
    type: String,
    default: FLAG_NUMBER_ENUM.FALSE.desc
  }
});

const emit = defineEmits(['update:value', 'change']);

function convertBoolean2number(value) {
  let result = null;
  if (_.isNaN(value) || _.isNull(value) || _.isUndefined(value)) {
    result = null;
  } else {
    result = value ? 1 : 0;
  }
  return result;
}
const selectValue = ref(convertBoolean2number(props.value));
// 箭头value变化
watch(
  () => props.value,
  (newValue) => {
    selectValue.value = convertBoolean2number(newValue);
  }
);

const handleChange = (value) => {
  console.log('boolean enum select', value);
  let booleanResult = null;
  if (!_.isUndefined(value)) {
    booleanResult = value === 1 ? true : false;
  }
  emit('update:value', booleanResult);
  emit('change', booleanResult);
};
</script>
