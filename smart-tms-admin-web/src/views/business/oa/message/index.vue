<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.searchWord" placeholder="标题/内容"/>
      </a-form-item>

      <a-form-item label="消息类型" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.msgType" placeholder="请选择消息类型"
                           enum-name="MSG_TYPE_ENUM"/>
      </a-form-item>

      <a-form-item label="消息子类型" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.msgSubType" placeholder="请选择消息子类型"
                           enum-name="MSG_SUB_TYPE_ENUM"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="是否已读" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.readFlag" @change="quickQuery">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button :value="false">未读</a-radio-button>
          <a-radio-button :value="true">已读</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="quickQuery">
          <template #icon>
            <SearchOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">

    <a-table
        :scroll="{ x: '100%' }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="companyId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'msgType'">
          <span>{{ $smartEnumPlugin.getDescByValue('MSG_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'msgSubType'">
          <span>{{ $smartEnumPlugin.getDescByValue('MSG_SUB_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'readFlag'">
          <span>{{ record.readFlag ? '已读' : '未读' }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button type="link" @click="toHandle(record)">去处理</a-button>
          </div>
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
  </a-card>
</template>
<script setup>
import {reactive, ref, onMounted} from 'vue';
import {messageApi} from '/@/api/business/message/message-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import {MSG_SUB_TYPE_ENUM} from '/@/constants/business/message-const';
import SmartEnumSelect from '/@/components/smart-enum-select//index.vue';
import {messageSetup} from "/@/views/business/oa/message/message-handle-setup";

const columns = reactive([
  {
    title: 'ID',
    width: 70,
    dataIndex: 'msgId',
  },
  {
    title: '消息类型',
    width: 90,
    dataIndex: 'msgType',
  },
  {
    title: '消息子类型',
    width: 160,
    dataIndex: 'msgSubType',
    ellipsis: true,
  },
  {
    title: '是否已读',
    width: 80,
    dataIndex: 'readFlag',
  },
  {
    title: '消息标题',
    dataIndex: 'title',
    width: 150,
    ellipsis: true,
  },
  {
    title: '消息内容',
    dataIndex: 'content',
    width: 200,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 80
  },
]);

const queryFormState = {
  searchWord: '',
  msgType: null,
  msgSubType: null,
  dataId: null,
  readFlag: null,
  endDate: null,
  startDate: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const operateModal = ref();

// 日期选择
let searchDate = ref();

function dateChange(dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}

function resetQuery() {
  searchDate.value = [];
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function quickQuery() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

// 查询 管理员查全部
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await messageApi.queryMessage(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

let {toHandle} = messageSetup();




onMounted(ajaxQuery);
</script>
