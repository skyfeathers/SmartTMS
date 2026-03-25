<template>
  <a-modal v-model:open="visible" :footer="null" :width="720" title="司机车辆" @cancel="onClose">
    <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        bordered
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }" :scroll="{ y: 400 }">
        <template v-if="column.dataIndex === 'driverName'">
          <template v-if="!disableLink">
              <a-button type="link" @click="driverDetail(record.driverId)">{{ record.driverName }}</a-button>
          </template>
        </template>
        <template v-if="column.dataIndex === 'vehicleNumber'">
          <template v-if="!disableLink">
              <a-button type="link" @click="vehicleDetail(record.vehicleId)">{{ record.vehicleNumber }}</a-button>
          </template>
        </template>
      </template>
    </a-table>
  </a-modal>
</template>
<script setup>
import { reactive, ref } from 'vue';
import {useRouter} from "vue-router";
// ----------------------  显示、关闭弹窗 ----------------------

// 是否展示弹窗
const visible = ref(false);
const disableLink = ref(true);


// 表格列
let columns = reactive([
  {
    title: '司机ID',
    dataIndex: 'driverId',
  },
  {
    title: '司机姓名',
    dataIndex: 'driverName',
  },
  {
    title: '司机电话',
    dataIndex: 'telephone',
  },
  {
    title: '车牌号',
    dataIndex: 'vehicleNumber'
  }
]);

const tableData = ref([]);

function showModal(list, disable) {
  visible.value = true;
  tableData.value = list;
  disableLink.value = disable;
}

function onClose () {
  tableData.value = [];
  disableLink.value = true;
  visible.value = false;
}
let router = useRouter();
function driverDetail(driverId){
  router.push({path:'/driver/detail',query:{driverId}})
  onClose();
}
function vehicleDetail(vehicleId){
  router.push({path:'/vehicle/vehicle-detail',query:{vehicleId}})
  onClose();
}

defineExpose({
  showModal
})
</script>
<style lang="less" scoped>

</style>
