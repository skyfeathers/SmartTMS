<template>
  <a-card size="small"
          :bordered="false">
    <a-table size="small"
             :dataSource="tableData"
             :columns="columns"
             :rowKey="(record,index)=>{return index}"
             :pagination="false"
             bordered>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'address'">
          {{getAddress(record)}}
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="deleteTransport(record)" v-privilege="'waybill:splitTransport:delete'" type="link">删除</a-button>
          </div>
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup>
import {reactive, onMounted, ref, watch} from 'vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';

let props = defineProps({
  waybillId: {
    type: Number,
  }
});

const columns = reactive([
  {
    title: '司机',
    dataIndex: 'driverName',
  },
  {
    title: '司机电话',
    width: 180,
    dataIndex: 'driverTelephone',
  },
  {
    title: '本段结束地址',
    dataIndex: 'address',
  },
  {
    title: '创建时间',
    width: 180,
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 90,
  },
]);
const tableLoading = ref(false);
const tableData = ref([]);

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    if (props.waybillId) {
      let responseModel = await waybillApi.getSplitTransport(props.waybillId);
      tableData.value = responseModel.data;
    }else{
      tableData.value = [];
    }
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

async function deleteTransport(record) {
  try {
    tableLoading.value = true;
    await waybillApi.splitTransportDelete(record.splitTransportId);
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}
// 地址
function getAddress(path) {
  let address = "";
  if (path.provinceName) {
    address = address + path.provinceName;
  }
  if (path.cityName) {
    address = address + path.cityName;
  }
  if (path.districtName) {
    address = address + path.districtName;
  }
  if (path.address) {
    address = address + path.address;
  }
  return address;
}

// ========= 定义 watch 监听 ===============
watch(
    () => props.waybillId,
    (e) => {
      if (e) {
        ajaxQuery();
      }else {
        tableData.value = [];
      }
    },
    {immediate: true}
);

onMounted(ajaxQuery);
</script>
