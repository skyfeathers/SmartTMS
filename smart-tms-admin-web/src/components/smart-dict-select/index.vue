<!--
 * @Description: 字典表下拉框
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-07-05
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
    <a-select-option v-for="item in dictValueList" :key="item.valueCode" :label="item.valueName" :value="item.valueCode">
      {{ item.valueName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { dictApi } from '/@/api/support/dict/dict-api';
import lodash from "lodash";

const props = defineProps({
  keyCode: String,
  value: [Number, Array, String],
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
  selectLabel: {
    type: String,
    default: null
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

watch(
    () => props.selectLabel,
    () => {
      selectByLabel();
    }
)

function handleChange (value) {
  let obj = null;
  if(value){
    obj = dictValueList.value.find(e => e.valueCode == value);
  }
  emit('update:value', value);
  emit('change', value, obj);
}


const dictValueList = ref([]);
async function queryDict() {
  let res = await dictApi.valueList(props.keyCode);
  dictValueList.value = res.data;
  selectByLabel();
}

// 根据指定label选中
function selectByLabel() {
  if (!props.selectLabel) {
    return;
  }
  let filter = dictValueList.value.filter(e => e.valueName === props.selectLabel);
  if (lodash.isEmpty(filter)) {
    return;
  }
  let filterElement = filter[0];
  handleChange(filterElement.valueCode);
}

onMounted(queryDict);
</script>
