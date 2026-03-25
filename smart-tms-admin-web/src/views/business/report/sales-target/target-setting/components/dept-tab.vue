<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="年份"
        class="smart-query-form-item">
        <a-date-picker :value="chooseYear"
          :allowClear="false"
          mode="year"
          format="YYYY"
          :open="openYearFlag"
          @openChange="handleOpenChange"
          @panelChange="handlePanelChange" />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary"
            @click="queryList">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small"
    :bordered="false"
    :hoverable="true">
    <a-table class="table-list"
      :scroll="{ x: 1500, y:600 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      :pagination="false"
      :loading="tableLoading"
      rowKey="departmentId"
      bordered
      :customRow="setCustomRow">
      <template #action="{ record }">
        <a-button v-if="record.canUpdateFlag"
          @click="edit(record)"
          v-privilege="'deptSalesTarget:update'"
          type="primary"
          size="small">
          修改
        </a-button>
      </template>
    </a-table>
    <DeptSalesTargetOperateModal ref="operateModal"
      @refresh="queryList" />
  </a-card>
</template>
<script setup>
import { reactive, onMounted, ref } from 'vue';
import dayjs from 'dayjs';
import _ from 'lodash';
import DeptSalesTargetOperateModal from './components/dept-operate-modal.vue';
import { departmentBusinessApi } from '/@/api/business/report/department-business-api';
import { formatMoney } from '/@/utils/money-util';

onMounted(() => {
  initTable();
  queryList();
});

const queryFormState = {
  year: dayjs().format('YYYY'),
  departmentId: undefined,
  searchWord: undefined,
};

const queryForm = reactive({
  ...queryFormState,
});

const columns = reactive([
  {
    title: '校区/小组',
    dataIndex: 'departmentName',
    fixed: 'left',
    width: 240,
  },
]);

const tableData = ref([]);
const tableLoading = ref(false);
const chooseYear = ref(dayjs());
const openYearFlag = ref<Boolean>(false);

function initTable() {
  for (let i = 0; i < 12; i++) {
    let title = dayjs().year(queryForm.year).month(i).format('YYYY-MM');
    let month = dayjs().month(i).format('MM');
    columns.push({
      title,
      dataIndex: title,
      customRender: ({ record }) => {
        let monthList = record.monthList;
        let find = monthList.find((e) => e.month == month);
        if (find && find.target) {
          return formatMoney(find.target);
        }
        return '-';
      },
    });
  }
  columns.push({
    title: '操作',
    slots: { customRender: 'action' },
    width: 100,
    align: 'center',
    fixed: 'right',
  });
}

function setCustomRow(record) {
  return {
    style: {
      'background-color': record.isTotalRow ? '#fafafa' : 'inherit',
    },
  };
}

// 查询
async function queryList() {
  try {
    tableLoading.value = true;
    const responseModel = await departmentBusinessApi.queryDeptSalesTargetList(queryForm);
    const listData = responseModel.data;
    if (!_.isEmpty(listData)) {
      handleListMap(listData);
      const totalRowData = setTotalRow(listData);
      listData.push(totalRowData);
    }
    tableData.value = listData;
  } catch (e) {
    console.log(e)
  } finally {
    tableLoading.value = false;
  }
}

// 为每一层添加合计行
function handleListMap(listData) {
  for (let i = 0, len = listData.length; i < len; i++) {
    const children = listData[i].children;
    if (_.isEmpty(children)) {
      listData[i].children = null;
    } else {
      const totalData = setTotalRow(children, listData[i].departmentId);
      listData[i].children.push(totalData);
      handleListMap(children);
    }
  }
}

// 添加合计行
const TOTAL_ROW_ID = 'total';
function setTotalRow(listData, depId) {
  const departmentId = depId ? TOTAL_ROW_ID + depId : TOTAL_ROW_ID;
  const totalRowData = {
    departmentId,
    departmentName: '合计',
    monthList: [],
    isTotalRow: true,
  };
  for (let i = 0, len = 12; i < len; i++) {
    const month = dayjs().month(i).format('MM');
    totalRowData.monthList.push({
      month,
      target: 0,
    });
    for (let j = 0, jLen = listData.length; j < jLen; j++) {
      const monthList = listData[j].monthList;
      if (!_.isEmpty(monthList)) {
        const find = monthList.find((e) => e.month == month);
        if (find) {
          totalRowData.monthList[i].target += find.target;
        }
      }
    }
  }
  return totalRowData;
}

// 弹出日历和关闭日历的回调
function handleOpenChange(open) {
  openYearFlag.value = open;
}

// 选择年份改变
function handlePanelChange(value) {
  queryForm.year = value.format('YYYY');
  chooseYear.value = value;
  openYearFlag.value = false;
}

// 点击重置
function resetQuery() {
  for (const key of Object.keys(queryFormState)) {
    queryForm[key] = queryFormState[key];
  }
  chooseYear.value = dayjs(queryForm.year);
  queryList();
}

// 点击修改
const operateModal = ref();
function edit(record) {
  operateModal.value.showModal(queryForm.year, record?.departmentId, record?.monthList);
}
</script>

<style scoped lang="less">
.table-list {
  :deep(.ant-table-tbody) {
    tr[data-row-key='total'] {
      td {
        position: sticky !important;
        bottom: 0 !important;
        border-top: 1px solid #f0f0f0;
        background-color: #fafafa !important;
      }
    }
  }
}
</style>
