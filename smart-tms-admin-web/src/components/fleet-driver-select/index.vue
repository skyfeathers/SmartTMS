<!--
 * @Description:
 * @Author: lidoudou
 * @Date: 2022-07-19
 * @LastEditTime: 2022-07-19
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
    :mode="multiple ? 'multiple' : ''"
    @change="handleChange"
    :disabled="disabled"
    optionFilterProp="label"
  >
    <a-select-option v-for="item in driverList" :key="item.driverId" :label="`${item.driverName}${item.telephone}${item.shorthandCode}`">
      {{ `${item.driverName}-${item.telephone}` }}
      <span v-show="item.shorthandCode"> ({{ item.shorthandCode }}) </span>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { fleetApi } from '/@/api/business/fleet/fleet-api';

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
  // 车队ID
  fleetId: {
    type: Number
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

// 箭头fleetId 车队ID的变化
watch(
    () => props.fleetId,
    (newValue) => {
      queryFleetDriverList();
    }
);


function handleChange(value) {
  emit('update:value', value);
  emit('change', value);
}

const driverList = ref([]);

async function queryFleetDriverList () {
  if (!props.fleetId) {
    driverList.value = [];
    return;
  }
  let res = await fleetApi.queryFleetDriverList(props.fleetId);
  driverList.value = res.data;
}

onMounted(queryFleetDriverList);
</script>
