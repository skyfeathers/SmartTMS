<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">

      <a-form-item label="年份"
                   class="smart-query-form-item">
        <a-date-picker :value="chooseYear"
                       picker="year"
                       :allowClear="false"
                       :open="openYearFlag"
                       @openChange="handleOpenChange"
                       @change="handlePanelChange"/>
      </a-form-item>

      <a-form-item label="销售人员"
                   class="smart-query-form-item">
        <employee-select
            ref="managerSelectRef"
            v-model:value="queryForm.employeeIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value"
            multiple
            placeholder="请选择业务负责人"
            width="200px"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary"
                    @click="queryList">
            <template #icon>
              <ReloadOutlined/>
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <SearchOutlined/>
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
             :scroll="{ y:600,x:1300}"
             size="small"
             :dataSource="tableData"
             :columns="columns"
             :pagination="false"
             :loading="tableLoading"
             rowKey="employeeId"
             bordered>
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'action'">
            <template v-if="record.employeeId !== TOTAL_ROW_ID">
              <a-button @click="edit(record)"
                        v-privilege="'employeeSalesTarget:update'"
                        type="primary"
                        size="small">修改
              </a-button>
              <!-- 优先级不高，暂时不做删除 -->
              <!-- <a-button class="smart-margin-left5" size="small">删除</a-button> -->
            </template>
          </template>
        </template>
    </a-table>
    <EmployeeSalesTargetOperateModal ref="operateModal"
                                     @refresh="queryList"/>
  </a-card>
</template>
<script setup>
import { reactive, onMounted, ref, watch } from 'vue';
import dayjs from 'dayjs';
import _ from 'lodash';
import EmployeeSalesTargetOperateModal from './components/employee-operate-modal.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { employeeBusinessApi } from '/@/api/business/report/employee-business-api';
import { formatMoney } from '/@/utils/money-util';

onMounted(() => {
  initTable();
  queryList();
});

const columns = ref([]);

function initTable () {
  columns.value = [];
  columns.value.push({
    title: '销售',
    dataIndex: 'employeeName',
    width: 100,
  });
  for (let i = 0; i < 12; i++) {
    const title = dayjs().year(queryForm.year).month(i).format('YYYY-MM');
    const month = dayjs().month(i).format('MM');
    columns.value.push({
      title,
      width: 100,
      customRender: ({ record }) => {
        const monthList = record.monthList;
        const find = monthList.find((e) => e.month == month);
        if (find) {
          return formatMoney(find.target);
        }
        return '-';
      },
    });
  }
  columns.value.push(
      {
        title: '合计',
        width: 100,
        align: 'center',
        fixed: 'right',
        customRender: ({ record }) => {
          const monthList = record.monthList;
          let targetList = monthList.map(e => e.target || 0);
          targetList.push(0);
          let totalAmount = targetList.reduce((a, b) => {
            return a + b;
          });
          return totalAmount.toFixed(2);
        }
      }, {
        title: '操作',
        width: 100,
        align: 'center',
        fixed: 'right',
      }
  );
}

const tableLoading = ref(false);
const tableData = ref([]);

const queryFormState = {
  year: dayjs().format('YYYY'),
  enterpriseId: undefined,
  employeeIdList: [],
  searchWord: undefined,
};

const queryForm = reactive({
  ...queryFormState,
});

const chooseYear = ref(dayjs(queryForm.year));
const openYearFlag = ref(false);

const TOTAL_ROW_ID = 'total';

// 查询
async function queryList () {
  try {
    initTable();
    tableLoading.value = true;
    const responseModel = await employeeBusinessApi.queryEmployeeSalesTargetList(queryForm);
    const listData = responseModel.data;
    if (!_.isEmpty(listData)) {
      // 添加合计行
      const totalRowData = {
        employeeId: TOTAL_ROW_ID,
        employeeName: '合计',
        enterpriseName: '',
        monthList: [],
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
      listData.push(totalRowData);
    }
    tableData.value = listData;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// 点击重置
function resetQuery () {
  for (const key of Object.keys(queryFormState)) {
    queryForm[key] = queryFormState[key];
  }
  chooseYear.value = dayjs(queryForm.year);
  queryList();
}

// 弹出日历和关闭日历的回调
function handleOpenChange (open) {
  openYearFlag.value = open;
}

// 选择年份改变
function handlePanelChange (value) {
  queryForm.year = value.format('YYYY');
  chooseYear.value = value;
  openYearFlag.value = false;
}

// 点击修改
const operateModal = ref();

function edit (record) {
  operateModal.value.showModal(queryForm.year, record?.enterpriseId, record?.employeeId, record?.monthList);
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
