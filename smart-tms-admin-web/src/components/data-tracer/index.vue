<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-27 17:55:48
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-21
-->
<template>
  <a-card size="small" :bordered="false">
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
        <template v-else-if="column.dataIndex === 'action'">
          <a-button v-if="record.diff" @click="showDetail(record.diff)" type="link">详情 </a-button>
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
    <a-modal v-model:open="visibleDiff" :width="800" title="数据比对" :footer="null">
      <div v-html="prettyHtml"></div>
    </a-modal>
  </a-card>
</template>
<script setup>
import { reactive, onMounted, ref, watch, nextTick } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { dataTracerApi } from '/@/api/support/data-tracer/data-tracer-api';
import * as Diff2Html from 'diff2html';
import * as Diff from 'diff';
import 'diff2html/bundles/css/diff2html.min.css';

let props = defineProps({
  businessId: {
    type: [Number, String],
  },
  businessType: {
    type: Number,
  },
});

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
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 80,
  },
]);

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true,
  businessId: props.businessId,
  businessType: props.businessType,
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

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

// ========= 定义 watch 监听 ===============
watch(
  () => props.businessId,
  (e) => {
    if (e) {
      queryForm.businessId = e;
      ajaxQuery();
    }
  },
  { immediate: true }
);

// diff
const visibleDiff = ref(false);
let prettyHtml = ref('');
function showDetail(diffContent) {
  visibleDiff.value = true;
  let diffContentJson = JSON.parse(diffContent);
  console.log(diffContentJson);

  const originContent = diffContentJson.originContent;
  const newContent = diffContentJson.newContent;

  const args = ['', originContent, newContent, '变更前', '变更后'];

  let diffPatch = Diff.createPatch(...args);
  let html = Diff2Html.html(diffPatch, {
    drawFileList: false,
    matching: 'words',
    diffMaxChanges: 1000,
    outputFormat: 'side-by-side',
  });

  prettyHtml.value = html;
  nextTick(() => {
    let diffDiv = document.querySelectorAll('.d2h-file-side-diff');
    if (diffDiv.length > 0) {
      let left = diffDiv[0],
        right = diffDiv[1];
      left.addEventListener('scroll', function (e) {
        if (left.scrollLeft != right.scrollLeft) {
          right.scrollLeft = left.scrollLeft;
        }
      });
      right.addEventListener('scroll', function (e) {
        if (left.scrollLeft != right.scrollLeft) {
          left.scrollLeft = right.scrollLeft;
        }
      });
    }
  });
}

</script>
<style scoped lang="less">
.operate-content {
  line-height: 20px;
  margin: 5px 0px;
}
</style>
