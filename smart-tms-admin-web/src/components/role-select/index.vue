<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-14 14:34:53
 * @LastEditors:
 * @LastEditTime: 2022-07-14 14:34:53
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
    <a-select-option v-for="item in dataList" :key="item.roleId" :label="item.roleName">
      {{ item.roleName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import {roleApi} from "/@/api/system/role/role-api";

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
  },
  api: {
    type: Function,
    default: roleApi.queryAll,
  },
  params: {
    type: String,
    default: '',
  },
});

const dataList = ref([]);
async function queryData() {
  let res = await props.api(props.params);
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
