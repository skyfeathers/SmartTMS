<!--
 * @Description: 意见反馈
 * @version:
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-06-28 14:14:28
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-13 14:54:41
-->
<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字"
        class="smart-query-form-item"
        style="margin-right: 20px">
        <a-input style="width: 240px"
          v-model:value.trim="queryForm.searchWord"
          placeholder="反馈内容/创建人" />
      </a-form-item>
      <a-form-item label="创建日期"
        class="smart-query-form-item"
        style="margin-right: 20px">
        <a-range-picker v-model:value="chooseTimeRange"
          @change="changeCreateDate"
          :ranges="defaultTimeRanges"
          format="YYYY-MM-DD"
          :placeholder="['请选择开始日期', '请选择结束日期']"
          style="width: 240px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button-group v-privilege="'feedback:query'">
          <a-button type="primary"
            @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="onReset">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>

    </a-row>
  </a-form>

  <a-card size="small">
    <a-table rowKey="feedbackId"
      :dataSource="tableData"
      :columns="tableColumns"
      :pagination="false"
      :loading="tableLoading"
      :scroll="{ x: 1300, y: 600 }"
      size="small"
      bordered >
      <template #feedbackAttachment="{text}">
        <file-preview :fileList="text"/>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="queryList"
        @showSizeChange="queryList"
        :show-total="(total) => `共${total}条`" />
    </div>
  </a-card>

</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { feedbackApi } from '/@/api/business/feedback/feedback-api';
import FilePreview from '/@/components/file-preview/index.vue'

// ----------------------- 表格列 --------------------------------------
const tableColumns = reactive([
  {
    title: '编号',
    dataIndex: 'feedbackId',
    width: 80,
  },
  {
    title: '反馈内容',
    dataIndex: 'feedbackContent',
  },
  {
    title: '反馈图片',
    dataIndex: 'feedbackAttachment',
    slots: { customRender: 'feedbackAttachment' }
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 120,
  },
  {
    title: '创建人部门',
    dataIndex: 'departmentName',
    width: 200,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
]);

// ----------------------- 查询参数 ------------------------------------
const defaultQueryForm={
  startDate: undefined,
  endDate: undefined,
  searchWord: undefined,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({ ...defaultQueryForm });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

onMounted(() => {
  queryList();
});

async function queryList() {
  try {
    tableLoading.value = true;
    const result = await feedbackApi.queryFeedback(queryForm);
    tableData.value = result.data.list;
    total.value = result.data.total;
  } catch (e) {
    console.log(e)
  } finally {
    tableLoading.value = false;
  }
}

// 处理选择日期范围
const chooseTimeRange = ref([]);

function changeCreateDate(value, dateString) {
  queryForm.startDate = dateString[0];
  queryForm.endDate = dateString[1];
}

// 点击查询
function onSearch() {
  queryForm.pageNum = 1;
  queryList();
}

// 点击重置
function onReset() {
  Object.assign(queryForm, defaultQueryForm);
  chooseTimeRange.value = [];
  queryList();
}

// ----------------------- 分页方法 ------------------------------------
</script>
