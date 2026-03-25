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
    <a-select-option v-for="item in listenerList" :key="item.name" :label="item.desc"
                     :value="item.name">
      {{ item.desc }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { flowApi } from '/@/api/business/flow/flow-api';

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
});

const emit = defineEmits(['update:value', 'change']);

// =========== 业务逻辑 =============

//员工列表数据
const listenerList = ref([]);
async function query() {
  try {
    let resp = await flowApi.getFlowListener();
    listenerList.value = resp.data;
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
