<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12 18:23:56
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
    optionFilterProp="label"
  >
    <a-select-option v-for="item in employeeList" :key="item.employeeId" :label="item.actualName"
                     :value="item.employeeId">
      {{ item.actualName }}
      <template v-if="item.departmentName"> （{{ item.departmentName }}） </template>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { employeeApi } from '/@/api/system/employee/employee-api';

// =========== 属性定义 和 事件方法暴露 =============

const props = defineProps({
  value: [Number, Array],
  placeholder: {
    type: String,
    default: '请选择',
  },
  width: {
    type: String,
    default: '100%',
  },
  size: {
    type: String,
    default: 'default',
  },
  // 角色编码，可为空
  roleCode: {
    type: String,
    default: null,
  },
});

const emit = defineEmits(['update:value', 'change']);

// =========== 业务逻辑 =============

//员工列表数据
const employeeList = ref([]);
async function query() {
  try {
    let resp = await employeeApi.queryAll();
    employeeList.value = resp.data;
  } catch (e) {
    console.log(e);
  } finally {
  }
}
onMounted(query);

// 监听value变化
const selectValue = ref(props.value);
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
