<template>
  <a-tree-select
      :value="selectValue"
      :treeData="dataList"
      :style="`width: ${width}`"
      placeholder="请选择分类"
      show-search
      style="width: 100%"
      :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
      allow-clear
      tree-default-expand-all
      @change="treeSelectChange"
  >
  </a-tree-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { categoryApi } from '/@/api/business/category/category-api';
import _ from 'lodash';

const props = defineProps({
  categoryType: {
    type: Number,
    required: true
  },
  value: [Number, String, Object],
  width: {
    type: String,
    default: '200px',
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

function treeSelectChange(e) {
  emit('update:value', e);
}

const dataList = ref([]);

async function queryData () {
  if (!props.categoryType) {
    return;
  }
  let res = await categoryApi.queryCategoryTree({
    categoryType: props.categoryType
  });
  let treeList = res.data;
  setDisabled(treeList)
  dataList.value = treeList;
}

function setDisabled (list) {
  list.forEach(item=>{
    if (!_.isEmpty(item.children)) {
      item.disabled = true;
    }
    setDisabled(item.children);
  })
}

onMounted(queryData);
</script>
