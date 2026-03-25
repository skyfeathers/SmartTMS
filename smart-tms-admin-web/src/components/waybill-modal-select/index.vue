<!--
 * @Description: 运单列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-06
-->
<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="确认" title="选择运单" :width="900" @cancel="onClose">
    <a-form class="smart-query-form">

      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item" label="关键字">
          <a-input v-model:value="queryForm.keywords" placeholder="运单号/创建人/订单号/箱号/铅封号" style="width: 200px"/>
        </a-form-item>
        <a-form-item class="smart-query-form-item" label="创建时间">
          <a-range-picker v-model:value="createDateRange" :presets="defaultTimeRanges" style="width: 200px"
                          @change="changeCreateDate"/>
        </a-form-item>
        <a-form-item class="smart-query-form-item" label="业务时间">
          <a-range-picker v-model:value="businessDateRange" picker="month" @change="changeBusinessDate"/>
        </a-form-item>

      </a-row>
      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item" label="车辆">
          <VehicleSelect v-model:value="queryForm.vehicleId" width="150px"/>
        </a-form-item>
        <a-form-item class="smart-query-form-item" label="司机">
          <DriverSelect v-model:value="queryForm.driverId" width="150px"/>
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
      <a-table
          :columns="columns"
          :dataSource="tableData"
          :loading="tableLoading"
          :pagination="false"
          :scroll="{ x: 4500, y: 400 }"
          :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange ,type:multiple?'checkbox':'radio'}"
          bordered
          rowKey="waybillId"
          size="small"
      >
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.dataIndex === 'receiveOrderNumberList'">
            {{ getReceiveOrderNumber(record) }}
          </template>
          <template v-if="column.dataIndex === 'payOrderCount'">
            <span v-if="record.submitPayFlag">已提交(<a type="link" @click="showPayOrderModal(record.waybillId)">{{
                text
              }}</a>)</span>
            <span v-else>未提交</span>
          </template>
          <template v-if="column.dataIndex === 'submitReceiveFlag'">
            <span v-if="(record.receiveOrderNumberList||[]).length == 0">未提交</span>

            <span v-else>已提交(<a @click="showReceiveOrderModal(record.waybillId)">{{
                record.receiveOrderNumberList.length
              }}</a>)</span>
          </template>
          <template v-if="column.dataIndex === 'confirmReceiveFlag'">
          <span v-if="record.submitReceiveFlag">
            <a @click="showReceiveOrderModal(record.waybillId)">已确认</a>
          </span>
            <span v-else>
            <span v-if="(record.receiveOrderNumberList||[]).length == 0">未确认</span>
            <span v-else><a @click="showReceiveOrderModal(record.waybillId)">部分确认</a></span>
          </span>
          </template>
          <template v-if="column.dataIndex === 'orderType'">
            {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
          </template>
          <template v-if="column.dataIndex === 'settleMode'">
            {{ $smartEnumPlugin.getDescByValue('SETTLE_MODE_ENUM', record.settleMode) }}
          </template>
          <template v-if="column.dataIndex === 'settleType'">
            {{ $smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', record.settleType) }}
          </template>
          <template v-if="column.dataIndex === 'auditStatus'">
            <a-tag :color="$smartEnumPlugin.getFieldByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus, 'color')">{{
                $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus)
              }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'waybillStatus'">
            <a-tag :color="$smartEnumPlugin.getFieldByValue('WAYBILL_STATUS_ENUM', record.waybillStatus, 'color')">{{
                $smartEnumPlugin.getDescByValue('WAYBILL_STATUS_ENUM', record.waybillStatus)
              }}
            </a-tag>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
            v-model:current="queryForm.pageNum"
            v-model:pageSize="queryForm.pageSize"
            :defaultPageSize="queryForm.pageSize"
            :pageSizeOptions="PAGE_SIZE_OPTIONS"
            :show-total="(total) => `共${total}条`"
            :total="total"
            show-less-items
            showQuickJumper
            showSizeChanger
            @change="ajaxQuery"
            @showSizeChange="ajaxQuery"
        />
      </div>
    </a-card>
    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="selectWaybill">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
import DriverSelect from '/@/components/driver-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import ShipperSelect from '/@/components/shipper-select/index.vue';
import SmartBooleanSelect from '/@/components/smart-boolean-select/index.vue';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { FLAG_NUMBER_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { reactive, ref, onMounted } from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { message } from 'ant-design-vue';

import _ from 'lodash';
import dayjs from 'dayjs';

const props = defineProps({
  multiple: {
    type: Boolean,
    default: false
  },
  // 自有车标识
  carCostFlag: {
    type: Boolean,
    default: false
  },
  // 挂靠车标识
  carPayCostFlag: {
    type: Boolean,
    default: false
  }
});

// ----------------- 显示附件弹窗 --------------------
let visible = ref(false);
let customParams = {};

function showModal (params, selectedWaybillIdList = [], selectedWaybillList = []) {
  customParams = params;
  visible.value = true;
  selectedRowKeyList.value = selectedWaybillIdList;
  selectedDataList.value = selectedWaybillList;
  ajaxQuery();
}

function onClose () {
  visible.value = false;
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  businessDateRange.value = [];
  clearSelected();
}

const emit = defineEmits(['changeWaybill']);
const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  //关键字
  keywords: null,
  //结算类型
  settleType: null,
  //车队
  fleetId: null,
  //司机
  driverId: null,
  //车辆
  vehicleId: null,
  //状态
  waybillStatusList: [],
  //隐藏作废单据
  hideCancelFlag: true,
  //运单类型
  orderType: null,
  //运单-开始时间
  startTime: null,
  //运单-结束时间
  endTime: null,
  // 或者ID
  shipperId: null,
  // 结算方式
  settleMode: null,
  // 运输方式
  transportMode: null,
  // 业务时间开始时间
  businessStartDate: null,
  // 业务时间开始时间
  businessEndDate: null
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  businessDateRange.value = [];
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

let searchParams = {};

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let params = Object.assign({}, queryForm, customParams);
    let responseModel = await waybillApi.query(params);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// -------------表单字段------------------

//运单时间
const createDateRange = ref([]);

function changeCreateDate (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

// 业务时间
const businessDateRange = ref([]);

function changeBusinessDate (dates, dateStrings) {
  if (_.isEmpty(dates)) {
    queryForm.businessStartDate = null;
    queryForm.businessEndDate = null;
    return;
  }
  queryForm.businessStartDate = dayjs(dates[0]).format('YYYY-MM-01');
  queryForm.businessEndDate = dayjs(dates[1]).format('YYYY-MM-01');
}

function getReceiveOrderNumber (record) {
  if (_.isEmpty(record.receiveOrderNumberList)) {
    return '';
  }
  let receiveOrderNumberArray = record.receiveOrderNumberList;
  let size = receiveOrderNumberArray.length;
  if (size > 1) {
    return receiveOrderNumberArray[0] + '等' + size + '条';
  }
  return receiveOrderNumberArray[0];
}

// ----------- table 批量操作 start -----------

const selectedRowKeyList = ref([]);
const selectedDataList = ref([]);

function onSelectChange (keyArray, dataArray) {
  selectedRowKeyList.value = keyArray;
  selectedDataList.value = dataArray;
}

function clearSelected () {
  selectedRowKeyList.value = [];
  selectedDataList.value = [];
}

function selectWaybill () {
  try {
    if (_.isEmpty(selectedRowKeyList.value)) {
      message.error('请选择运单');
      return;
    }
    emit('changeWaybill', _.cloneDeep(selectedDataList.value));
    onClose();
  } catch (e) {
    console.log(e);
  }
}

const columns = reactive([
  {
    title: '运单号',
    dataIndex: 'waybillNumber',
    width: 170,
    fixed: 'left',
  },
  {
    title: '订单号',
    dataIndex: 'orderNo',
    width: 150,
    fixed: 'left',
  },
  {
    title: '箱号',
    dataIndex: 'containerNumber',
    ellipsis: true,
  },
  {
    title: '铅封号',
    dataIndex: 'leadSealNumber',
    ellipsis: true,
  },
  {
    title: '柜型',
    dataIndex: 'cabinetName',
    ellipsis: true,
  },
  {
    title: '最迟提箱时间',
    dataIndex: 'latestPackingTime',
  },
  {
    title: '货主',
    dataIndex: 'shortName',
    ellipsis: true,
  },
  {
    title: '司机',
    dataIndex: 'driverName',
  },
  {
    title: '车辆',
    dataIndex: 'vehicleNumber',
    width: 100
  },
  {
    title: '合同金额',
    dataIndex: 'contractAmount',
    width: 100
  },
  {
    title: '货物总重',
    dataIndex: 'totalWeight',
    width: 100
  },
  {
    title: '应收金额',
    dataIndex: 'receiveAmount',
    width: 120
  },
  {
    title: '应付金额',
    dataIndex: 'payableAmount',
    width: 120
  },
  {
    title: '税点',
    dataIndex: 'taxPoint',
    width: 80
  },
  {
    title: '税金',
    dataIndex: 'taxAmount',
    width: 100
  },
  {
    title: '本单利润',
    dataIndex: 'profitAmount',
    width: 100
  },
  {
    title: '已付金额',
    dataIndex: 'paidAmount',
    width: 120
  },
  {
    title: '付款',
    dataIndex: 'payOrderCount',
    width: 120
  },

  {
    title: '装货时间',
    dataIndex: 'loadTime',
    width: 160
  },
  {
    title: '卸货时间',
    dataIndex: 'unloadTime',
    width: 160
  },
  {
    title: '提空/提重时间',
    dataIndex: 'deliverGoodsTime',
    width: 160
  },
  {
    title: '落重/还空时间',
    dataIndex: 'receiveGoodsTime',
    width: 160
  },
  {
    title: '审核状态',
    dataIndex: 'auditStatus',
    width: 80
  },
  {
    title: '运单状态',
    dataIndex: 'waybillStatus',
    width: 80
  },
  {
    title: '运单类型',
    dataIndex: 'orderType',
    width: 80
  },
  {
    title: '装箱地点',
    dataIndex: 'containerLocation',
    ellipsis: true,
  },
  {
    title: '装货地点',
    dataIndex: 'placingLocation',
    ellipsis: true,
  },
  {
    title: '卸货地点',
    dataIndex: 'unloadingLocation',
    ellipsis: true,
  },
  {
    title: '还箱地点',
    dataIndex: 'returnContainerLocation',
    ellipsis: true,
  },
  {
    title: '所属企业',
    dataIndex: 'enterpriseName',
    ellipsis: true,
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
]);

onMounted(() => {
  if (props.carCostFlag) {
    queryForm.carCostFlag = FLAG_NUMBER_ENUM.FALSE.value;
  }
  if (props.carPayCostFlag) {
    queryForm.carPayCostFlag = FLAG_NUMBER_ENUM.FALSE.value;
  }
});

defineExpose({
  showModal
});
</script>
<style lang="less" scoped>
.waybill-code {
  display: flex;
  justify-content: space-between;
}
</style>
