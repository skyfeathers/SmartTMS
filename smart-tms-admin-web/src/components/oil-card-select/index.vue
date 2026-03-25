<!--
 * @Description: 油卡下拉选择
 * @Author: lidoudou
 * @Date: 2022-07-17
 * @LastEditTime: 2022-07-17 18:20:40
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
    <a-select-option v-for="item in dataList" :key="item.oilCardId" :label="item.oilCardNo">
      {{ item.oilCardNo }}<span v-show="item.disabledFlag"> (禁用) </span>
    </a-select-option>
  </a-select>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import {oilApi} from '/@/api/business/card/oil-api';
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
  multiple: {
    type: Boolean,
    default: false,
  },
  type: {
    type: Number,
    default: null
  },
  // 油卡的禁用状态
  disabledFlag:{
    type: Boolean,
    default: null
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


const dataList = ref([]);

async function queryData() {
  let params = { type: props.type, disabledFlag: props.disabledFlag};
  let res = await oilApi.queryList(params);
  dataList.value = res.data;
}

function handleChange(value) {
  emit('update:value', value);
  if (!value) {
    emit('change', value);
    return;
  }

  let selected = null;
  if (Array.isArray(value)) {
    selected = dataList.value.filter(e => value.includes(e.bankId));
  } else {
    selected = dataList.value.find(e => e.oilCardId == value);
  }
  emit('change', value, selected);
}


onMounted(queryData);
</script>
