<!--
 * @Description: 车辆的油卡下拉选择
 * @Author: lidoudou
 * @Date: 2023-10-30
 * @LastEditTime: 2023-10-30 09:20:40
 * @LastEditors: lidoudou
-->
<template>
  <a-select
      v-model:value="selectValue"
      :allowClear="true"
      :disabled="disabled"
      :mode="multiple ? 'multiple' : ''"
      :placeholder="props.placeholder"
      :showSearch="true"
      :size="size"
      :style="`width: ${width}`"
      optionFilterProp="label"
      @change="handleChange"
  >
    <a-select-option v-for="item in dataList" :key="item.oilCardId" :label="item.oilCardNo">
      {{ item.oilCardNo }}<span v-show="item.disabledFlag"> (禁用) </span>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { inject, onMounted, ref, watch } from 'vue';
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
  vehicleId: {
    type: [Number, null],
    default: null,
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

async function queryData () {
  let res = await oilApi.queryOilCardByVehicleId(props.vehicleId);
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
    selected = dataList.value.filter(e => value.includes(e));
  } else {
    selected = dataList.value.find(e => e.oilCardId == value);
  }
  emit('change', value, selected);
}


onMounted(queryData);
</script>
