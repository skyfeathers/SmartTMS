<template>
  <a-form class="smart-query-form" v-privilege="'expire:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.searchWord" placeholder="编号/名称/关联订单/所属公司"/>
      </a-form-item>

      <a-form-item label="到期时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="所属模块" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.moduleType" enumName="EXPIRED_CERTIFICATE_MODULE_TYPE_ENUM" allowClear/>
      </a-form-item>

      <a-form-item label="证件类型" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.type" enumName="EXPIRED_CERTIFICATE_TYPE_ENUM" allowClear/>
      </a-form-item>

      <a-form-item label="到期状态" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.status" enumName="EXPIRED_CERTIFICATE_STATUS_ENUM" allowClear/>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
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
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="reminderTimeUpdate()" v-privilege="'expire:updateReminderTime'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          到期时间提醒设置
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.EXPIRED_CERTIFICATE" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-table
        :scroll="{ x: 1300,y:500 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="companyId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'moduleType'">
          {{ $smartEnumPlugin.getDescByValue('EXPIRED_CERTIFICATE_MODULE_TYPE_ENUM', record.moduleType) }}
        </template>
        <template v-if="column.dataIndex === 'status'">
          <span v-if="record.status == EXPIRED_CERTIFICATE_STATUS_ENUM.EXPIRED.value" style="color:red">
            已到期
          </span>
          <span v-else>
            {{ $smartEnumPlugin.getDescByValue('EXPIRED_CERTIFICATE_STATUS_ENUM', record.status) }}
          </span>
        </template>
        <template v-if="column.dataIndex === 'type'">
          {{ $smartEnumPlugin.getDescByValue('EXPIRED_CERTIFICATE_TYPE_ENUM', record.type) }}
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
    <!--新建编辑modal-->
    <ExpireOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import ExpireOperateModal from './expire-operate-modal.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import {reactive, ref, onMounted} from 'vue';
import {expireApi} from '/@/api/business/expire/expire-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import { EXPIRED_CERTIFICATE_STATUS_ENUM } from '/@/constants/business/exipre-const';

const columns = ref([
  {
    title: 'ID',
    width: 50,
    dataIndex: 'id',
  },
  {
    title: '所属模块',
    dataIndex: 'moduleType',
  },
  {
    title: '模块名称',
    dataIndex: 'moduleName',
  },
  {
    title: '证件类型',
    dataIndex: 'type',
  },
  {
    title: '到期时间',
    dataIndex: 'expiredTime',
  },
  {
    title: '到期状态',
    dataIndex: 'status',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
]);

const queryFormState = {
  searchWord: '',
  moduleType: null,
  type: null,
  status: null,
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
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await expireApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}


function reminderTimeUpdate(rowData) {
  operateModal.value.showModal(rowData);
}

onMounted(ajaxQuery);
</script>
