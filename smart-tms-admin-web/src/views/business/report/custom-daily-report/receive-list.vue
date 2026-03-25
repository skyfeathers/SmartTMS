<template>
  <a-modal width="1300px" :open="visible" title="应收列表" ok-text="提交" cancel-text="取消" @ok="onClose" @cancel="onClose">
    <a-form  class="smart-query-form">
      <a-row class="smart-query-form-row">
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
      <ReceiveOrderTable ref="tableRef" :pageSize="PAGE_SIZE"/>
    </a-card>
  </a-modal>
</template>
<script setup>
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { reactive, ref } from 'vue';
import { PAGE_SIZE } from '/@/constants/common-const';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import ReceiveOrderTable from '../receive-order/components/receive-order-table.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';

// ----------------------- 对外暴露 ------------------------
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  visible.value = false;
  shipperIdRef.value = 0;
}

const shipperIdRef = ref(0);
function showModal(shipperId) {
  shipperIdRef.value = shipperId;
  visible.value = true;
  search();
}

const queryFormState = {
  pageSize: PAGE_SIZE,
  saleIdList: [],
  existWaitVerificationFlag: null,
  shipperId: null,
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
  queryForm.shipperId = shipperIdRef.value;
  tableRef.value.ajaxQuery(queryForm);
}

function search () {
  queryForm.pageNum = 1;
  queryForm.shipperId = shipperIdRef.value;
  tableRef.value.ajaxQuery(queryForm);
}


//创建时间
const createDateRange = ref([]);

function changeCreateDate (dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}


</script>
<style scoped lang="less">
</style>
