<template>
  <a-form class="smart-query-form" v-privilege="'shipperNewReport:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="年份" class="smart-query-form-item">
        <a-date-picker :value="chooseYear"
                       :allowClear="false"
                       picker="year"
                       :open="openYearFlag"
                       @openChange="handleOpenChange"
                       @change="handlePanelChange"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="销售">
        <employee-select ref="salesRef" multiple v-model:value="queryForm.managerIdList"
                         :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value" placeholder="请选择销售员" width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary"
                    @click="init">
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
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <LineCharts :chartsData="chartsData"/>
    <a-table class="table-list"
             :scroll="{ x: 1500}"
             size="small"
             :dataSource="tableData"
             :columns="columns"
             :pagination="false"
             :loading="tableLoading"
             rowKey="shipperId"
             bordered>
    </a-table>
  </a-card>
</template>
<script setup>
import { reactive, onMounted, ref } from 'vue';
import { shipperReportApi } from '/@/api/business/report/shipper-report-api';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import dayjs, { Dayjs } from 'dayjs';
import LineCharts from './components/line-charts.vue'
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
onMounted(() => {
  init();
});

let year = dayjs().format('YYYY')

const columns = reactive([]);

const tableData = ref([]);
const chartsData = ref({ xAxis: [], yAxis: [] });
const tableLoading = ref(false);
const chooseYear = ref(dayjs());
const openYearFlag = ref(false);

function init(){
  initTable();
  queryList();
}

function initTable () {
  columns.length = 0;
  for (let i = 0; i < 12; i++) {
    let title = dayjs().year(year).month(i).format('YYYY-MM');
    columns.push({
      title,
      dataIndex: title,
      customRender: ({ record }) => {
        let find = record[title];
        if (find) {
          return find;
        }
        return 0;
      },
    });
  }
}


const queryFormState = {
  managerIdList: [],
  enterpriseId: null,
  year: year,
};

const queryForm = reactive({ ...queryFormState });

// 查询
async function queryList () {
  try {
    tableLoading.value = true;
    const responseModel = await shipperReportApi.queryShipperNewStatistic(queryForm);
    const listData = responseModel.data;
    let data = {};
    listData.forEach(item => {
      data[item.month] = item.count;
    });
    tableData.value = [data];
    let xAxis = listData.map(e => e.month);
    let yAxis = listData.map(e => e.count);
    chartsData.value = {
      xAxis, yAxis
    };
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// 弹出日历和关闭日历的回调
function handleOpenChange (open) {
  openYearFlag.value = open;
}

// 选择年份改变
function handlePanelChange (value) {
  year = value.format('YYYY');
  chooseYear.value = value;
  openYearFlag.value = false;
}

// 点击重置
function resetQuery () {
  year = dayjs().format('YYYY')
  chooseYear.value = dayjs();
  init();
}

</script>

<style scoped lang="less">
</style>
