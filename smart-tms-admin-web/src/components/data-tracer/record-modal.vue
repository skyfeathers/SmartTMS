<template>
  <a-modal v-model:open="recordModal" :width="1200" title="操作记录" :footer="null" @close="onClose">
    <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="dataTracerId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'userAgent'">
          <div>{{ record.browser }} / {{ record.os }} / {{ record.platform }}</div>
        </template>
        <template v-if="column.dataIndex === 'operateContent'">
          <div class="operate-content" v-html="record.operateContent"></div>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-modal>
</template>
<script setup>
import { reactive, ref} from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { dataTracerApi } from '/@/api/support/data-tracer/data-tracer-api';
import 'diff2html/bundles/css/diff2html.min.css';
const columns = reactive([
  {
    title: '操作时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作人',
    dataIndex: 'operatorName',
    width: 80,
  },
  {
    title: 'IP',
    dataIndex: 'ip',
    ellipsis: true,
    width: 100,
  },
  {
    title: '客户端',
    dataIndex: 'userAgent',
    ellipsis: true,
    width: 150,
  },
  {
    title: '操作类型',
    dataIndex: 'operateTypeDesc',
    width: 150,
  },
  {
    title: '操作内容',
    dataIndex: 'operateContent',
  },
]);

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true,
  businessId: null,
  businessType: null,
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await dataTracerApi.queryDataTracerLogList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}
let recordModal = ref(false);
function showModal(businessId, businessType){
  queryForm.businessId = businessId;
  queryForm.businessType = businessType;
  ajaxQuery();
  recordModal.value = true;
}

function onClose(){
  recordModal.value = false;
}


// ----------------------- 以下是暴露的方法内容 -----------------------
defineExpose({
  showModal
});
</script>
<style scoped lang="less">
.operate-content {
  line-height: 20px;
  margin: 5px 0px;
}
</style>
