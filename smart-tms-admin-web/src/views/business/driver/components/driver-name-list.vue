<template>
  <span v-if="tableData.length > 0" class="tips">存在{{ tableData.length }}位重名司机，<a @click="showModal">点击查看</a></span>
  <a-modal v-model:open="visible" :footer="null" :width="720" title="重名司机列表" @cancel="onClose">
    <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        bordered
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'businessMode'">
          {{
            $smartEnumPlugin.getDescByValue('VEHICLE_BUSINESS_MODE_ENUM', text)
          }}
        </template>
      </template>
    </a-table>
  </a-modal>
</template>
<script setup>
import { reactive, ref } from 'vue';
import { driverApi } from '/@/api/business/driver/driver-api';
import _ from "lodash"

defineExpose({
  ajaxQuery
});

// ----------------------  显示、关闭弹窗 ----------------------

// 是否展示弹窗
const visible = ref(false);

function showModal () {
  visible.value = true;
}

function onClose () {
  visible.value = false;
}

// 表格列
let columns = reactive([
  {
    title: '司机姓名',
    dataIndex: 'driverName',
  },
  {
    title: '司机电话',
    dataIndex: 'telephone',
  },
  {
    title: '经营方式',
    dataIndex: 'businessMode'
  },
  {
    title: '所属公司',
    dataIndex: 'enterpriseName',
  },
]);

const tableData = ref([]);

async function ajaxQuery (driverName) {
  try {
    let responseModel = await driverApi.queryByName(driverName);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  }
}

</script>
<style lang="less" scoped>
.tips {
  color: #ff4d4f
}
</style>
