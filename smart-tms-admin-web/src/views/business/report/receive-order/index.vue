<template>
  <a-form v-privilege="'receiveOrderReport:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="货主/收款单号">
        <a-input v-model:value="queryForm.keyWords" placeholder="货主/收款单号" style="width: 200px"/>
      </a-form-item>

      <a-form-item label="销售" name="managerId">
        <employee-select
            ref="managerSelectRef"
            v-model:value="queryForm.saleIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value"
            mode="multiple"
            placeholder="请选择业务负责人"
            width="200px"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="帐期">
        <a-range-picker v-model:value="createDateRange" :presets="defaultTimeRanges" style="width: 300px"
                        @change="changeCreateDate"/>
      </a-form-item>


      <a-form-item label="仅显示未回款数据" name="existWaitVerificationFlag">
        <a-checkbox v-model:checked="queryForm.existWaitVerificationFlag" @change="search"/>
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

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="exportExcel()" v-privilege="'receiveOrderReport:export'" size="small">导出</a-button>
      </div>
    </a-row>
    <ReceiveOrderTable ref="tableRef" :pageSize="PAGE_SIZE"/>
  </a-card>

</template>
<script setup>
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { reactive, ref } from 'vue';
import { PAGE_SIZE } from '/@/constants/common-const';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import ReceiveOrderTable from './components/receive-order-table.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SmartLoading } from '/@/components/smart-loading';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';

const queryFormState = {
  pageSize: PAGE_SIZE,
  saleIdList: [],
  existWaitVerificationFlag: null,
  //关键字
  keyWords: null,
  startDate: null,
  endDate: null
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

const tableRef = ref();

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  tableRef.value.ajaxQuery(queryForm);
}

function search () {
  queryForm.pageNum = 1;
  tableRef.value.ajaxQuery(queryForm);
}

// ------------ 导出 Excel -----------
function exportExcel () {
  SmartLoading.show();
  receiveOrderApi.downloadExcel('应收帐款.xlsx', queryForm);
  SmartLoading.hide();
}


//创建时间
const createDateRange = ref([]);

function changeCreateDate (dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}

</script>
<style lang="less" scoped>
</style>
