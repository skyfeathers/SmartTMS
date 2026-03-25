<!--
 * @Description: 企业列表下拉
 * @Author: lidoudou
 * @Date: 2022-07-14
 * @LastEditTime: 2022-07-14
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
    <a-select-option v-for="item in dataList" :key="item.enterpriseId" :label="item.enterpriseName">
      {{ item.enterpriseName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { enterpriseApi } from '/@/api/business/oa/enterprise-api';
import _ from 'lodash';

const props = defineProps({
  value: {
    type: [Number, String, Object],
    default: null
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
  multiple:{
    type: Boolean,
    default: false,
  },
  // 类型 ENTERPRISE_TYPE_ENUM
  type:{
    type: Number,
  },
  // 是否查询网络货运企业，true 查询
  nftFlag: {
    type: Boolean,
    default: false
  },
  // 是否设置默认值
  defaultFlag: {
    type: Boolean,
    default: false
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

function handleChange(value) {
  emit('update:value', value);
  emit('change', value);
}

const dataList = ref([]);

async function queryData () {
  if (props.nftFlag) {
    let res = await enterpriseApi.queryNftList();
    dataList.value = res.data;
  } else {
    let res = await enterpriseApi.queryList(props.type);
    dataList.value = res.data;
  }

  if(!props.value && !_.isEmpty(dataList.value) && props.defaultFlag) {
    let defaultValue = dataList.value[0].enterpriseId;
    if(props.multiple) {
      selectValue.value = [defaultValue];
    }else {
      selectValue.value = defaultValue;
    }
    handleChange(selectValue.value);


  }
}
onMounted(queryData);
</script>
