<template>
  <a-alert :closable="true" message="当未选择任何车辆时，系统默认查询所有自有车的费用情况" type="warning" show-icon>
    <template #icon><smile-outlined /></template>
  </a-alert>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker :allowClear="false" :value="createDateList" picker="month" @change="dateChange"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="车辆">
        <VehicleSelect multiple v-model:value="queryForm.vehicleIdList" placeholder="请选择车辆" width="200px"/>
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
        <a-button v-if="queryForm.vehicleIdList" size="small" @click="exportExcel()" v-privilege="'car-cost:report:day-statistic:export'">导出</a-button>
        <a-button v-if="queryForm.vehicleIdList" size="small" @click="exportFlowExcel()" v-privilege="'car-cost:report:day-statistic:export-flow'">导出车辆流水表</a-button>
      </div>
    </a-row>
    <a-table
        :columns="columns"
        :dataSource="tableData"
        :loading="tableLoading"
        :pagination="false"
        :scroll="{x:tableWidth, y: 500}"
        bordered
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'index'">
          {{ index + 1 }}
        </template>
        <!-- 如果当前值不存在，则使用默认值 -->
        <template v-if="column.default">
          <span v-if="record[column.dataIndex]">{{ record[column.dataIndex] }}</span>
          <span v-else>{{ column.defaultValue }}</span>
        </template>
        <template v-if="column.showType == 'custom'">
          {{ getColumnValue(column.dataIndex, record) }}
          <a-tooltip v-if="column.remarkDataIndex && getColumnValue(column.remarkDataIndex, record)" placement="top">
            <template #title>
              <span>
                {{ getColumnValue(column.remarkDataIndex, record) }}
              </span>
            </template>
            <question-circle-outlined/>
          </a-tooltip>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-button size="small" :disabled="!record.waybillId" type="link" @click="edit(record)" v-privilege="'car-cost:report:day-statistic:edit-salary'">编辑工资</a-button>
          <a-button size="small" :disabled="!record.waybillId" type="link" @click="showCostModal(record)" v-privilege="'car-cost:report:day-statistic:edit-cost'">编辑运费</a-button>
          <a-button size="small" v-if="record.carCostBasicInfoVO && !record.carCostBasicInfoVO.confirmFlag" :disabled="!record.waybillId" type="link" @click="showConfirmModal(record,true)" v-privilege="'car-cost:report:day-statistic:confirm'">确认</a-button>
          <a-button size="small" v-if="record.carCostBasicInfoVO && record.carCostBasicInfoVO.confirmFlag" :disabled="!record.waybillId" type="link" @click="showConfirmModal(record,false)" v-privilege="'car-cost:report:day-statistic:un-confirm'">反确认</a-button>
        </template>
      </template>
    </a-table>
  </a-card>
  <CarCostBasicModal ref="basicModalRef" @reloadList="ajaxQuery"/>
  <WaybillCostModal ref="waybillCostModal" @reloadList="ajaxQuery" />
</template>
<script setup>
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import dayjs from 'dayjs';
import WaybillCostModal from '/@/views/business/waybill/components/waybill-cost-modal.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import _ from 'lodash';
import { waybillCarCostReportApi } from '/@/api/business/report/car-cost-report-api';
import { carCostCategoryApi } from '/@/api/business/waybill/car-cost-category-api';
import { CAR_COST_CATEGORY_TYPE_ENUM } from '/@/constants/business/car-cost-const';
import CarCostBasicModal from './components/car-cost-basic-modal.vue';
import { carCostApi } from '/@/api/business/car-cost/car-cost-api';
import { Modal,message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import useDragTable from '/@/hook/use-drag-table/index';
import ellipsisRender from "/@/utils/ellipsis-render";

function showConfirmModal (item,status) {
  let content = '确认后司机将不能继续提交费用，以及不能再进行费用的审核、编辑操作。确定要继续吗？';
  if (!status) {
    content = '反确认后司机可提交费用，以及进行费用的审核编辑。确定要进行反确认吗？';
  }
  Modal.confirm({
    title: '提示',
    content,
    onOk: () => {
      requestUpdateStatus(item,status);
    }
  });
}

const waybillCostModal = ref();
function showCostModal(item) {
  waybillCostModal.value.showModal(item.waybillId);
}

async function requestUpdateStatus (item,status) {
  useSpinStore().show();
  try {
    await carCostApi.updateBasicConfirmFlag(item.carCostBasicInfoVO.basicInfoId, status);
    message.success('更新成功');
    ajaxQuery();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

const queryFormState = {
  startTime: null,
  endTime: null,
  vehicleIdList: [],
  waybillSettleMode: null,// 结算方式
};

const queryForm = reactive({ ...queryFormState });

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  createDateList.value = [dayjs(), dayjs()];
  queryForm.startTime = dayjs(createDateList.value[0]).format('YYYY-MM-01');
  queryForm.endTime = dayjs(createDateList.value[1]).endOf('month').format('YYYY-MM-DD');
  ajaxQuery();
}

function search () {
  ajaxQuery();
}

// ------------ 查询现金开支 -----------
let cashCategoryList = ref([]);

async function queryCashCategoryList () {
  try {
    tableLoading.value = true;
    const { data } = await carCostCategoryApi.queryByCostType(CAR_COST_CATEGORY_TYPE_ENUM.CASH_COST.value);
    cashCategoryList.value = data.map(e => {
      return {
        title: e.categoryName,
        dataIndex: ['cashInfoVO', e.categoryId.toString()],
        remarkDataIndex: ['cashInfoVO', `${e.categoryId}_remark`],
        defaultValue: 0,
        showType: 'custom'
      };
    });
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

const basicModalRef = ref()
function edit (item) {
  basicModalRef.value.showModal({
    waybillId:item.waybillId,
    ...item.carCostBasicInfoVO
  });
}

// 表格列
const { columnsData:columns, tableWidth } = useDragTable([
    {
      title: '序号',
      width: 80,
      dataIndex: 'index'
    },
    {
      title: '时间',
      width: 180,
      dataIndex: 'createTimeStr'
    },
    {
      title: '路线',
      width: 180,
      dataIndex: 'routeName'
    },
    {
      title: '箱号',
      width: 180,
      dataIndex: 'containerNumber',
      ellipsis: true,
      customRender: ellipsisRender,
    },
    {
      title: '司机姓名',
      dataIndex: 'driverName'
    },
    {
      title: '应收运费',
      width: 180,
      dataIndex: 'payableAmount',
      defaultValue: 0
    },
    {
      title: '高速里程',
      width: 180,
      dataIndex: ['carCostBasicInfoVO', 'highSpeedMileage'],
      defaultValue: 0
    },
    {
      title: '低速里程',
      width: 180,
      dataIndex: ['carCostBasicInfoVO', 'lowSpeedMileage'],
      defaultValue: 0
    },
    {
      title: '总里程',
      width: 180,
      dataIndex: ['carCostBasicInfoVO', 'gpsMileage'],
      defaultValue: 0,
    },
    {
      title: '提成工资',
      width: 120,
      dataIndex: ['carCostBasicInfoVO', 'commissionSalary'],
      defaultValue: 0,
    },
    {
      title: '基本工资',
      width: 120,
      dataIndex: ['carCostBasicInfoVO', 'basicSalary'],
      defaultValue: 0,
    },
    {
      title: '补助',
      width: 120,
      dataIndex: ['carCostBasicInfoVO', 'allowance'],
      defaultValue: 0,
    },
    {
      title: ' 出勤天数 ',
      width: 120,
      dataIndex: ['carCostBasicInfoVO', 'attendanceDays'],
      defaultValue: 0,
    },
    {
      title: '小计',
      width: 120,
      dataIndex: ['carCostBasicInfoVO', 'subtotal'],
      defaultValue: 0,
    },
    {
      title: '车辆油卡-专卡',
      children: [
        {
          title: '初始余额',
          dataIndex: ['oilCardInfoVOList', '0', 'initialAmount'],
          defaultValue: 0
        },
        {
          title: '油卡充值',
          dataIndex: ['oilCardInfoVOList', '0', 'oilCardReceiveAmount'],
          defaultValue: 0,
          remarkDataIndex: ['oilCardInfoVOList', '0', 'oilCardReceiveRemark'],
          showType: 'custom'
        },
        {
          title: '加油金额',
          dataIndex: ['oilCardInfoVOList', '0', 'oilCardPayAmount'],
          defaultValue: 0,
          remarkDataIndex: ['oilCardInfoVOList', '0', 'oilCardPayRemark'],
          showType: 'custom',
        },
        {
          title: '现金加油金额',
          dataIndex: ['oilCardInfoVOList', '0', 'cashPayAmount'],
          defaultValue: 0,
          remarkDataIndex: ['oilCardInfoVOList', '0', 'cashPayRemark'],
          showType: 'custom',
        },
        {
          title: '小计',
          dataIndex: ['oilCardInfoVOList', '0', 'subtotal'],
          defaultValue: 0,
        },
        {
          title: '油卡余额',
          dataIndex: ['oilCardInfoVOList', '0', 'endAmount'],
          defaultValue: 0,
        }
      ]
    },
    {
      title: '车辆油卡-普卡',
      children: [
        {
          title: '初始余额',
          dataIndex: ['oilCardInfoVOList', '1', 'initialAmount'],
          defaultValue: 0
        },
        {
          title: '油卡充值',
          dataIndex: ['oilCardInfoVOList', '1', 'oilCardReceiveAmount'],
          defaultValue: 0,
          showType: 'custom',
          remarkDataIndex: ['oilCardInfoVOList', '1', 'oilCardReceiveRemark'],
        },
        {
          title: '加油金额',
          dataIndex: ['oilCardInfoVOList', '1', 'oilCardPayAmount'],
          defaultValue: 0,
          showType: 'custom',
          remarkDataIndex: ['oilCardInfoVOList', '1', 'oilCardPayRemark'],
        },
        {
          title: '现金加油金额',
          dataIndex: ['oilCardInfoVOList', '1', 'cashPayAmount'],
          defaultValue: 0,
          showType: 'custom',
          remarkDataIndex: ['oilCardInfoVOList', '1', 'cashPayRemark'],
        },
        {
          title: '小计',
          dataIndex: ['oilCardInfoVOList', '1', 'subtotal'],
          defaultValue: 0,
        },
        {
          title: '油卡余额',
          dataIndex: ['oilCardInfoVOList', '1', 'endAmount'],
          defaultValue: 0,
        }
      ]
    },
    {
      title: '现金开支',
      children: [
        {
          title: '初始现金',
          dataIndex: ['cashInfoVO', 'initialAmount'],
          defaultValue: 0
        },
        {
          title: '领取出车款',
          dataIndex: ['cashInfoVO', 'rechargeAmount'],
          defaultValue: 0,
          showType: 'custom',
          remarkDataIndex: ['cashInfoVO', 'rechargeRemark'],
        },
        ...cashCategoryList.value,
        {
          title: '开支小计',
          dataIndex: ['cashInfoVO', 'subtotal'],
          defaultValue: 0
        },
        {
          title: '现金余额',
          dataIndex: ['cashInfoVO', 'endAmount'],
          defaultValue: 0
        }
      ]
    },
    {
      title: 'ETC',
      dataIndex: ['etcVO', 'etcAmount'],
      defaultValue: 0,
      remarkDataIndex: ['etcVO', 'remark'],
      showType: 'custom'
    },
    {
      title: '尿素',
      dataIndex: ['ureaVO', 'ureaAmount'],
      defaultValue: 0,
      remarkDataIndex: ['ureaVO', 'remark'],
      showType: 'custom'
    },
    {
      title: '维修费',
      dataIndex: 'repairAmount',
      defaultValue: 0
    },
    {
      title: '保险费',
      dataIndex: 'insuranceAmount',
      defaultValue: 0
    },
    {
      title: '审车费',
      dataIndex: 'reviewAmount',
      defaultValue: 0
    },
    {
      title: '保养费',
      dataIndex: 'maintenanceAmount',
      defaultValue: 0
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: 180,
      ellipsis: true,
      customRender: ellipsisRender,
    },
    {
      title: '用油量/升',
      dataIndex: 'oilConsumption',
      defaultValue: 0
    },
    {
      title: '油耗元/km',
      dataIndex: 'averageOilAmount',
      defaultValue: 0
    },
    {
      title: '尿素耗元/km',
      dataIndex: 'averageUreaAmount',
      defaultValue: 0
    },
    {
      title: '运费元/km',
      dataIndex: 'averageFrightAmount',
      defaultValue: 0
    },
    {
      title: '毛利',
      dataIndex: 'profitAmount',
      defaultValue: 0
    },
    {
      title: '油耗升/100km',
      dataIndex: 'averageHundredKmFrightAmount',
      defaultValue: 0
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width:250
    }
  ]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await waybillCarCostReportApi.queryCarCostDayStatistic(queryForm);
    const list = responseModel.data;
    buildShowList(list);
    tableData.value = list;
  } catch (e) {
    console.log(e);
    tableData.value = [];
  } finally {
    tableLoading.value = false;
  }
}

function buildShowList (list) {
  list.forEach(item => {
    let cashInfoVO = item.cashInfoVO;
    if (!cashInfoVO) {
      return;
    }
    let itemInfoVOList = cashInfoVO.itemInfoVOList || [];
    if (_.isEmpty(itemInfoVOList)) {
      return;
    }
    itemInfoVOList.forEach(category => {
      item.cashInfoVO[category.categoryId] = category.amount;
      if (category.remark) {
        item.cashInfoVO[`${category.categoryId}_remark`] = category.remark;
      }
    });
  });
}

//创建时间
let createDateList = ref([dayjs(), dayjs().endOf('month')]);
queryForm.startTime = dayjs(createDateList.value[0]).format('YYYY-MM-01');
queryForm.endTime = dayjs(createDateList.value[1]).format('YYYY-MM-DD');

function dateChange (dateList, dateStrings) {
  createDateList.value = dateList;
  queryForm.startTime = dayjs(dateList[0]).format('YYYY-MM-01');
  queryForm.endTime = dayjs(dateList[1]).endOf('month').format('YYYY-MM-DD');
}

// ------------ 导出 Excel -----------
function exportExcel () {
  let data = {
    ...queryForm,
    vehicleIdList:queryForm.vehicleIdList.join(',')
  }
  waybillCarCostReportApi.downloadDayList(data);
}

function exportFlowExcel () {
  let data = {
    ...queryForm,
    vehicleIdList:queryForm.vehicleIdList.join(',')
  }
  waybillCarCostReportApi.downloadFlowList(data);
}

let router = useRouter();
let route = useRoute();

function waybillDetail (waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

// ------------ 导出 Excel -----------
function getColumnValue (keyList, record) {
  let dataIndex = _.cloneDeep(keyList);
  if (!record) {
    return '';
  }
  let dataIndexType = typeof dataIndex;
  if (dataIndexType == 'string') {
    return record[dataIndex];
  }
  if (dataIndex.length == 1) {
    return record[dataIndex[0]];
  }

  let currentKey = dataIndex[0];
  dataIndex.shift(0);
  let value = record[currentKey];
  if (typeof value != 'object') {
    return value;
  }
  return getColumnValue(dataIndex, record[currentKey]);
}

function getValueByKey (key, record) {
  let value = record[key];
  let valueType = typeof value;
  if(valueType !== 'object'){
    return value;
  }
  getValueByKey();
}

onMounted(() => {
  if (route.query.vehicleId) {
    queryForm.vehicleIdList = [Number(route.query.vehicleId)];
  }
  if (route.query.enterpriseId) {
    queryForm.enterpriseId = Number(route.query.enterpriseId);
  }
  if (route.query.startTime && route.query.endTime) {
    createDateList.value = [dayjs(route.query.startTime), dayjs(route.query.endTime)];
    queryForm.startTime = route.query.startTime;
    queryForm.endTime = route.query.endTime;
  }
  queryCashCategoryList();
  ajaxQuery();
});

</script>
<style lang="less" scoped>
</style>
