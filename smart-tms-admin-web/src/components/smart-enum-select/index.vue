<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-07-09
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
    optionFilterProp="label"
    :mode="multiple ? 'multiple' : ''"
  >
    <a-select-option v-for="item in $smartEnumPlugin.getValueDescList(props.enumName)" :key="item.value"
                     :label="item.desc" :value="item.value">
      {{ item.desc }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  enumName: String,
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
</script>
