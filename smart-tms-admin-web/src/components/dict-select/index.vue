<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-14 21:13:34
 * @LastEditors:
 * @LastEditTime: 2022-07-14 21:13:34
-->
<template>
  <div>
    <a-select
        v-model:value="selectValue"
        :style="`width: ${width}`"
        :placeholder="props.placeholder"
        :allowClear="true"
        :size="size"
        :mode="mode"
        @change="handleChange"
        :disabled="disabled"
        optionFilterProp="label"
    >
      <a-select-option v-for="item in dictValueList" :key="item.valueCode" :label="item.valueName"
                       :value="item.valueCode">
        {{ item.valueName }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue';
import { dictApi } from '/@/api/support/dict/dict-api';

const props = defineProps({
  keyCode: String,
  value: [Array],
  mode: {
    type: String,
    default: 'combobox',
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
});

// 重新修改展示字段
const values = computed(() => {
  if (!props.value) {
    return [];
  }
  if (!Array.isArray(props.value)) {
    console.error('valueList is not array!!!');
    return [];
  }
  let res = [];
  if (props.value && props.value.length > 0) {
    props.value.forEach((element) => {
      res.push(element.valueCode);
    });
    return res;
  }
  return res;
});



const selectValue = ref();
watch(
    values,
    (value) => {
      selectValue.value = value;
    }
);

const dictValueList = ref([]);
async function queryDict() {
  let res = await dictApi.valueList(props.keyCode);
  dictValueList.value = res.data;
}

const emit = defineEmits(['update:value', 'change']);
function handleChange(value) {
  let selected = [];
  if(!value){
    emit('update:value', selected);
    emit('change', selected);
    return selected;
  }
  if (Array.isArray(value)) {
    selected = dictValueList.value.filter(e=>value.includes(e.valueCode));
  }else {
    let findValue = dictValueList.value.find(e=>e.valueCode == value);
    selected.push(findValue);
  }
  emit('update:value', selected);
  emit('change', selected);
}

onMounted(queryDict);
</script>
