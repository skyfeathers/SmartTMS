<!--
 * @Description: 根据角色查询员工列表
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
      :mode="multiple ? 'multiple' : ''"
  >
    <a-select-option v-for="item in employeeList" :key="item.employeeId" :label="item.actualName"
                     :value="item.employeeId">
      {{ item.actualName }}
      <template v-if="item.departmentName"> （{{ item.departmentName }}）</template>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { message } from 'ant-design-vue';

import { employeeApi } from '/@/api/system/employee/employee-api';
import { useSpinStore } from '/@/store/modules/system/spin';
import { configApi } from '/@/api/support/config/config-api';

// =========== 属性定义 和 事件方法暴露 =============

const props = defineProps({
  value: [Number, Array],
  // SYSTEM_CONFIG_KEY_ENUM
  systemConfigKey: {
    type: String,
    required: true,
  },
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
  multiple: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:value', 'change']);

// =========== 业务逻辑 =============
let roleCode = null;
//员工列表数据
const employeeList = ref([]);

async function query () {
  try {
    let resp = await employeeApi.queryAll({ roleCode, disabledFlag: false});
    employeeList.value = resp.data;
  } catch (e) {
    console.log(e);
  } finally {

  }
}

async function queryRoleIdBySystemConfig () {
  try {
    useSpinStore().show();
    const { data } = await configApi.queryByKey(props.systemConfigKey);
    roleCode = data.configValue;
    await query();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

onMounted(queryRoleIdBySystemConfig);

// 监听value变化
const selectValue = ref(props.value);
watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    }
);

function handleChange (value) {
  emit('update:value', value);
  emit('change', value);
}
</script>
