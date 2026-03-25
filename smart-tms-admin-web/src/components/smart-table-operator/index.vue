<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-08-20
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
-->
<template>
  <span>
     <a-tooltip title="左移">
      <a-button type="text" @click="move(-150)" size="small">
        <template #icon><left-outlined /></template>
      </a-button>
    </a-tooltip>
     <a-tooltip title="右移">
      <a-button type="text" @click="move(150)" size="small">
        <template #icon><right-outlined /></template>
      </a-button>
    </a-tooltip>
    <a-tooltip title="刷新">
      <a-button type="text" @click="props.refresh" size="small">
        <template #icon><redo-outlined /></template>
      </a-button>
    </a-tooltip>
    <a-tooltip title="列设置">
      <a-button type="text" @click="showModal" size="small">
        <template #icon><setting-outlined /></template>
      </a-button>
    </a-tooltip>

    <SmartTableColumnModal ref="smartTableColumnModal" @change="updateColumn" />
  </span>
</template>

<script setup>
import _ from 'lodash';
import { tableColumnApi } from '/@/api/support/table/table-column-api';
import { onMounted, reactive, ref, watch } from 'vue';
import SmartTableColumnModal from './smart-table-column-modal.vue';
import { mergeColumn } from './smart-table-column-merge';
const props = defineProps({
  // 表格列数组
  modelValue: {
    type: Array,
    default: new Array(),
  },
  // 刷新表格函数
  refresh: {
    type: Function,
    required: true,
  },
  // 表格id
  tableId: {
    type: Number,
    require: true,
  },
});

const emit = defineEmits(['update:modelValue']);

// 原始表格列数据（复制一份最原始的columns集合，以供后续各个地方使用）
let originalColumn = _.cloneDeep(props.modelValue);

onMounted(buildUserTableColumns);

//构建用户的数据列
async function buildUserTableColumns() {
  let userTableColumnArray = [];
  try {
    let res = await tableColumnApi.getColumns(props.tableId);
    if (res.data) {
      try {
        userTableColumnArray = JSON.parse(res.data);
      } catch (e1) {
        console.log(e1);
      }
    }
  } catch (e) {
    console.log(e);
  }

  updateColumn(userTableColumnArray);
}

// ----------------- 弹窗 修改表格列 -------------------

const smartTableColumnModal = ref();
function showModal() {
  smartTableColumnModal.value.show(originalColumn, props.tableId);
}

// 将弹窗修改的列数据，赋值给原表格 列数组
function updateColumn(changeColumnArray) {
  //合并列
  const newColumns = mergeColumn(_.cloneDeep(originalColumn), changeColumnArray);
  emit(
    'update:modelValue',
    newColumns.filter((e) => e.showFlag)
  );
}

function move(value){
  let scroll = document.getElementsByClassName('ant-table-body')[0];
  if(!scroll){
    scroll = document.getElementsByClassName('ant-table-content')[0];
  }
  if(scroll){
    scroll.scrollLeft = scroll.scrollLeft + value;
  }
}

// ========= 定义 watch 监听 ===============
watch(
  () => props.tableId,
  (e) => {
    if (e) {
      originalColumn = _.cloneDeep(props.modelValue);
      buildUserTableColumns();
    }
  },
  { immediate: true }
);
</script>
