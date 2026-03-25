<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-07-14
 * @LastEditors: zhuoda
-->
<template>
  <a-checkbox-group :style="`width: ${width}`" v-model:value="selectValue" :options="optionList" @change="handleChange" />
</template>

<script setup>
import { ref, watch, getCurrentInstance, onMounted } from 'vue';

const props = defineProps({
  enumName: String,
  value: Array,
  width: {
    type: String,
    default: '200px',
  },
});

const optionList = ref([]);
function buildOptionList() {
  const internalInstance = getCurrentInstance(); // 有效  全局
  const smartEnumPlugin = internalInstance.appContext.config.globalProperties.$smartEnumPlugin;
  const valueList = smartEnumPlugin.getValueDescList(props.enumName);
  optionList.value = valueList.map((e) => Object.assign({}, { value: e.value, label: e.desc }));
}

onMounted(buildOptionList);

const selectValue = ref(props.value);

watch(
  () => props.value,
  (newValue) => {
    selectValue.value = newValue;
  }
);

const emit = defineEmits(['update:value', 'change']);
function handleChange(value) {
  emit('update:value', value);
  emit('change', value);
}
</script>
