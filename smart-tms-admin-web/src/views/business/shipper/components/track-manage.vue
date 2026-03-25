<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-07 17:40:21
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 17:55:45
-->
<template>
  <a-card class="smart-margin-top10">
    <a-table :columns="columns" :dataSource="tableData" :pagination="false" :scroll="{x:1000}" bordered size="small">
      <template #trackWay="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("TRACK_WAY_ENUM", text) }}</span>
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
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from "vue";
import { shipperTrackApi } from "/@/api/business/shipper/shipper-track-api";
import { PAGE_SIZE_OPTIONS } from "/@/constants/common-const";
import { useRoute } from 'vue-router';

const route = useRoute();
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const queryFormState = {
  pageNum: 1,
  pageSize: 10,
  shipperId: route.query.shipperId,
};
const queryForm = reactive({ ...queryFormState });
// ------- 列表相关 start --------
const columns = reactive([
  {
    title: "跟进方式",
    dataIndex: "trackWay",
    slots: { customRender: "trackWay" },
  },
  {
    title: "跟进人",
    dataIndex: "employeeName",
  },
  {
    title: "跟进人部门",
    dataIndex: "departmentName",
  },
  {
    title: "跟进日期",
    dataIndex: "trackTime",
    width: 160
  },
  {
    title: "联系人",
    dataIndex: "intervieweeName",
  },
  {
    title: "跟进内容",
    dataIndex: "trackContent",
    ellipsis: true,
  },
  {
    title: "下次跟进日期",
    dataIndex: "nextTrackTime",
  },
  {
    title: "备注",
    dataIndex: "remark",
  },
]);
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await shipperTrackApi.queryTrackList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}
// ------- 列表相关 end --------

onMounted(() => {
  ajaxQuery();
});

defineExpose({
  ajaxQuery
})
</script>
