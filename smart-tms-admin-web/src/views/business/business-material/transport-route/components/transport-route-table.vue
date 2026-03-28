<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 14:40:01
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-19
-->
<template>
  <a-table
    :scroll="{ x: '100%' }"
    size="small"
    :dataSource="tableData"
    :columns="columns"
    rowKey="transportRouteId"
    :pagination="false"
    bordered
  >
    <template #bodyCell="{ text, record, index, column }">
      <!-- 提箱地点 -->
      <template v-if="column.dataIndex === 'containerLocation'">
        <TextEllipsis :text="getAddress(record.pathList, PATH_TYPE_ENUM.CONTAINER_LOCATION.value)" classKey="containerLocation"/>
      </template>
      <!-- 装货地点 -->
      <template v-if="column.dataIndex === 'placingLoading'">
        <TextEllipsis :text="getAddress(record.pathList, PATH_TYPE_ENUM.PLACING_LOADING.value)" classKey="placingLoading"/>
      </template>
      <!-- 卸货地点 -->
      <template v-if="column.dataIndex === 'unloadingLocation'">
        <TextEllipsis :text="getAddress(record.pathList, PATH_TYPE_ENUM.UNLOADING_LOCATION.value)" classKey="unloadingLocation"/>
      </template>
      <!-- 还箱地点 -->
      <template v-if="column.dataIndex === 'returnContainerLocation'">
        <TextEllipsis :text="getAddress(record.pathList, PATH_TYPE_ENUM.RETURN_CONTAINER_LOCATION.value)" classKey="returnContainerLocation"/>
      </template>
      <!-- 状态 -->
      <template v-else-if="column.dataIndex === 'disabledFlag'">
        <a-tag v-show="record.disabledFlag" color="error">禁用</a-tag>
        <a-tag v-show="!record.disabledFlag" color="success">启用</a-tag>
      </template>
      <template v-else-if="column.dataIndex === 'action'">
        <div class="smart-table-operate">
          <a-button @click="update(record)" v-privilege="'transportRoute:edit'" type="link">编辑</a-button>
          <a-button @click="confirmDelete(record.transportRouteId)" v-privilege="'transportRoute:delete'" type="link">删除
          </a-button>
        </div>
      </template>
    </template>
  </a-table>
</template>
<script setup>
// ------------- 定义props -------
import { transportRouteTableSetup } from './trabsport-route-table-setup';
import { computed } from 'vue';
import { TRANSPORTATION_TYPE_ENUM, PATH_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import { Modal } from 'ant-design-vue';
import TextEllipsis from '/@/components/text-ellipsis/index.vue';

let props = defineProps({
  transportRouteType: {
    type: Number,
    require: true,
  },
  tableData: {
    type: Object,
    require: true,
  },
});

// 表格列，以及获取地点
let { containerTransportColumns, generalGoodsTransportColumns, getAddress } = transportRouteTableSetup();
const columns = computed(() => {
  if (TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value == props.transportRouteType) {
    return containerTransportColumns;
  }
  return generalGoodsTransportColumns;
});

// emit
const emit = defineEmits(['update', 'delete']);
function update(record) {
  emit('update', record);
}

function confirmDelete(transportRouteId) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该运输路线吗？',
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      emit('delete', transportRouteId);
    },
  });
}

// ------------- 定义计算属性 end -------
</script>
<style scoped lang="less">
p {
  margin-bottom: 0;
}

.table-btn {
  padding: 4px 8px;
}
</style>
